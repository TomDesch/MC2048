package io.stealingdapenta.mc2048.persistence;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

/**
 * Runs player file operations off the server thread while preserving per-player submission order.
 *
 * <p>Different players may be processed concurrently. Shutdown rejects new work, drains every
 * accepted player chain, and reports failures or timeout rather than silently discarding saves.
 */
public final class PlayerDataService {

    private static final int THREAD_COUNT = 2;
    private static final long EXECUTOR_TERMINATION_TIMEOUT_SECONDS = 1;

    private final PlayerDataStore store;
    private final ThreadPoolExecutor executor;
    private final Map<UUID, CompletableFuture<Void>> playerTails = new ConcurrentHashMap<>();
    private final java.util.Set<CompletableFuture<?>> pendingFutures = ConcurrentHashMap.newKeySet();
    private final List<Throwable> failures = java.util.Collections.synchronizedList(new ArrayList<>());
    private final Object shutdownMonitor = new Object();
    private final AtomicInteger threadNumber = new AtomicInteger();
    private Lifecycle lifecycle = Lifecycle.ACCEPTING;

    public PlayerDataService(PlayerDataStore store) {
        this.store = store;
        this.executor = new ThreadPoolExecutor(
            THREAD_COUNT,
            THREAD_COUNT,
            0,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            runnable -> {
                Thread thread = new Thread(
                    runnable,
                    "mc2048-player-data-" + threadNumber.incrementAndGet()
                );
                thread.setDaemon(false);
                return thread;
            },
            new ThreadPoolExecutor.AbortPolicy()
        );
    }

    public CompletableFuture<PlayerData> load(UUID playerId, String playerName) {
        return submit(playerId, () -> store.read(playerId, playerName));
    }

    public CompletableFuture<PlayerData> update(
        UUID playerId,
        String playerName,
        UnaryOperator<PlayerData> update
    ) {
        return submit(playerId, () -> store.update(playerId, playerName, update));
    }

    public CompletableFuture<PlayerDataStore.PlayerDataBatch> loadAll() {
        return submit(new UUID(0, 0), store::readAll);
    }

    /**
     * Rejects new submissions, waits for every accepted per-player chain to settle, and only then
     * closes the executor. The supplied timeout bounds accepted work; executor termination has a
     * separate short bound after no persistence work remains.
     */
    public ShutdownResult shutdown(Duration timeout) {
        Objects.requireNonNull(timeout, "timeout");
        synchronized (shutdownMonitor) {
            return shutdownOnce(timeout);
        }
    }

    private ShutdownResult shutdownOnce(Duration timeout) {
        long timeoutNanos = toSaturatedNanos(timeout);
        long startedAt = System.nanoTime();
        List<CompletableFuture<Void>> acceptedChains;
        synchronized (this) {
            if (lifecycle == Lifecycle.ACCEPTING) {
                lifecycle = Lifecycle.DRAINING;
            }
            acceptedChains = List.copyOf(playerTails.values());
        }

        WaitOutcome chainOutcome = awaitAcceptedChains(acceptedChains, startedAt, timeoutNanos);
        WaitOutcome executorOutcome = WaitOutcome.TIMED_OUT;
        if (chainOutcome == WaitOutcome.COMPLETED) {
            executor.shutdown();
            executorOutcome = awaitExecutorTermination();
        }

        int pending = pendingFutures.size();
        if (chainOutcome == WaitOutcome.TIMED_OUT) {
            ShutdownTimeoutException timeoutFailure = new ShutdownTimeoutException(
                "Timed out with " + pending + " pending player data tasks.");
            failures.add(timeoutFailure);
            executor.shutdownNow();
            pendingFutures.forEach(future -> future.completeExceptionally(timeoutFailure));
        } else if (chainOutcome == WaitOutcome.INTERRUPTED) {
            ShutdownAbortedException aborted = new ShutdownAbortedException(
                "Player data shutdown was interrupted.");
            executor.shutdownNow();
            pendingFutures.forEach(future -> future.completeExceptionally(aborted));
        } else if (executorOutcome != WaitOutcome.COMPLETED) {
            if (executorOutcome == WaitOutcome.TIMED_OUT) {
                failures.add(new IllegalStateException(
                    "Player data executor did not terminate after accepted tasks settled."));
            }
            executor.shutdownNow();
        }

        synchronized (this) {
            lifecycle = Lifecycle.TERMINATED;
        }
        List<Throwable> recordedFailures;
        synchronized (failures) {
            recordedFailures = List.copyOf(failures);
        }
        return new ShutdownResult(
            chainOutcome == WaitOutcome.COMPLETED && executorOutcome == WaitOutcome.COMPLETED,
            pending,
            recordedFailures
        );
    }

    private synchronized <T> CompletableFuture<T> submit(UUID playerId, IoSupplier<T> operation) {
        if (lifecycle != Lifecycle.ACCEPTING) {
            return CompletableFuture.failedFuture(new IllegalStateException("Player data service is shutting down."));
        }
        CompletableFuture<Void> predecessor = playerTails.getOrDefault(
            playerId, CompletableFuture.completedFuture(null));
        CompletableFuture<T> result;
        try {
            result = predecessor
                .handle((ignored, failure) -> null)
                .thenApplyAsync(ignored -> {
                    try {
                        return operation.get();
                    } catch (IOException exception) {
                        throw new java.util.concurrent.CompletionException(exception);
                    }
                }, executor);
        } catch (RejectedExecutionException exception) {
            failures.add(exception);
            return CompletableFuture.failedFuture(exception);
        }
        CompletableFuture<Void> tail = result.handle((ignored, failure) -> {
            Throwable cause = failure == null ? null : unwrap(failure);
            if (cause != null && !(cause instanceof ShutdownAbortedException)) {
                failures.add(cause);
            }
            return null;
        });
        pendingFutures.add(result);
        result.whenComplete((ignored, failure) -> pendingFutures.remove(result));
        playerTails.put(playerId, tail);
        tail.whenComplete((ignored, failure) -> playerTails.remove(playerId, tail));
        return result;
    }

    private WaitOutcome awaitAcceptedChains(
        List<CompletableFuture<Void>> acceptedChains,
        long startedAt,
        long timeoutNanos
    ) {
        CompletableFuture<Void> settlement = CompletableFuture.allOf(
            acceptedChains.toArray(CompletableFuture[]::new));
        try {
            settlement.get(remainingNanos(startedAt, timeoutNanos), TimeUnit.NANOSECONDS);
            return WaitOutcome.COMPLETED;
        } catch (ExecutionException exception) {
            throw new IllegalStateException("Player data chain settlement failed unexpectedly.", exception);
        } catch (TimeoutException exception) {
            return WaitOutcome.TIMED_OUT;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            failures.add(exception);
            return WaitOutcome.INTERRUPTED;
        }
    }

    private WaitOutcome awaitExecutorTermination() {
        try {
            return executor.awaitTermination(EXECUTOR_TERMINATION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                ? WaitOutcome.COMPLETED
                : WaitOutcome.TIMED_OUT;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            failures.add(exception);
            return WaitOutcome.INTERRUPTED;
        }
    }

    private long toSaturatedNanos(Duration timeout) {
        if (timeout.isNegative()) {
            return 0;
        }
        try {
            return timeout.toNanos();
        } catch (ArithmeticException exception) {
            return Long.MAX_VALUE;
        }
    }

    private long remainingNanos(long startedAt, long timeoutNanos) {
        long elapsed = System.nanoTime() - startedAt;
        if (elapsed <= 0) {
            return timeoutNanos;
        }
        return Math.max(0, timeoutNanos - elapsed);
    }

    private Throwable unwrap(Throwable failure) {
        return failure.getCause() == null ? failure : failure.getCause();
    }

    private enum Lifecycle {
        ACCEPTING,
        DRAINING,
        TERMINATED
    }

    private enum WaitOutcome {
        COMPLETED,
        TIMED_OUT,
        INTERRUPTED
    }

    private static class ShutdownAbortedException extends IllegalStateException {

        private ShutdownAbortedException(String message) {
            super(message);
        }
    }

    private static final class ShutdownTimeoutException extends ShutdownAbortedException {

        private ShutdownTimeoutException(String message) {
            super(message);
        }
    }

    @FunctionalInterface
    private interface IoSupplier<T> {
        T get() throws IOException;
    }

    public record ShutdownResult(boolean completed, int pendingTasks, List<Throwable> failures) {
    }
}
