package io.stealingdapenta.mc2048.persistence;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

public final class PlayerDataService {

    private static final int THREAD_COUNT = 2;
    private static final int QUEUE_CAPACITY = 128;

    private final PlayerDataStore store;
    private final ThreadPoolExecutor executor;
    private final Map<UUID, CompletableFuture<Void>> playerTails = new ConcurrentHashMap<>();
    private final java.util.Set<CompletableFuture<?>> pendingFutures = ConcurrentHashMap.newKeySet();
    private final List<Throwable> failures = java.util.Collections.synchronizedList(new ArrayList<>());
    private final AtomicBoolean accepting = new AtomicBoolean(true);
    private final AtomicInteger threadNumber = new AtomicInteger();

    public PlayerDataService(PlayerDataStore store) {
        this.store = store;
        this.executor = new ThreadPoolExecutor(
            THREAD_COUNT,
            THREAD_COUNT,
            0,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(QUEUE_CAPACITY),
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

    public ShutdownResult shutdown(Duration timeout) {
        accepting.set(false);
        executor.shutdown();
        boolean completed = false;
        try {
            completed = executor.awaitTermination(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            failures.add(exception);
        }
        int pending = pendingFutures.size();
        if (!completed) {
            failures.add(new IllegalStateException("Timed out with " + pending + " queued player data tasks."));
            executor.shutdownNow();
            pendingFutures.forEach(future -> future.completeExceptionally(
                new IllegalStateException("Player data service shutdown timed out.")));
        }
        List<Throwable> recordedFailures;
        synchronized (failures) {
            recordedFailures = List.copyOf(failures);
        }
        return new ShutdownResult(completed, pending, recordedFailures);
    }

    private synchronized <T> CompletableFuture<T> submit(UUID playerId, IoSupplier<T> operation) {
        if (!accepting.get()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Player data service is shutting down."));
        }
        CompletableFuture<Void> predecessor = playerTails.getOrDefault(
            playerId, CompletableFuture.completedFuture(null));
        CompletableFuture<T> result = predecessor
            .handle((ignored, failure) -> null)
            .thenApplyAsync(ignored -> {
                try {
                    return operation.get();
                } catch (IOException exception) {
                    throw new java.util.concurrent.CompletionException(exception);
                }
            }, executor);
        CompletableFuture<Void> tail = result.handle((ignored, failure) -> {
            if (failure != null) {
                failures.add(unwrap(failure));
            }
            return null;
        });
        pendingFutures.add(result);
        result.whenComplete((ignored, failure) -> pendingFutures.remove(result));
        playerTails.put(playerId, tail);
        tail.whenComplete((ignored, failure) -> playerTails.remove(playerId, tail));
        return result;
    }

    private Throwable unwrap(Throwable failure) {
        return failure.getCause() == null ? failure : failure.getCause();
    }

    @FunctionalInterface
    private interface IoSupplier<T> {
        T get() throws IOException;
    }

    public record ShutdownResult(boolean completed, int pendingTasks, List<Throwable> failures) {
    }
}
