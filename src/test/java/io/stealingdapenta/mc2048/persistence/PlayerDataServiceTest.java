package io.stealingdapenta.mc2048.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PlayerDataServiceTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void updateWithSamePlayerSerializesOperationsInSubmissionOrder() throws Exception {
        UUID playerId = UUID.randomUUID();
        CountDownLatch firstWriteStarted = new CountDownLatch(1);
        CountDownLatch releaseFirstWrite = new CountDownLatch(1);
        AtomicFileWriter systemWriter = AtomicFileWriter.system();
        java.util.concurrent.atomic.AtomicInteger writes = new java.util.concurrent.atomic.AtomicInteger();
        PlayerDataStore store = new PlayerDataStore(temporaryDirectory, () -> 3, (target, content) -> {
            if (writes.incrementAndGet() == 1) {
                firstWriteStarted.countDown();
                await(releaseFirstWrite);
            }
            systemWriter.write(target, content);
        });
        PlayerDataService service = new PlayerDataService(store);

        CompletableFuture<PlayerData> first = service.update(
            playerId, "Alex", data -> data.withAnimationSpeed(4));
        assertTrue(firstWriteStarted.await(5, TimeUnit.SECONDS));
        CompletableFuture<PlayerData> second = service.update(
            playerId, "Alex", data -> data.withAnimationSpeed(5));

        assertFalse(second.isDone());
        releaseFirstWrite.countDown();
        assertEquals(4, first.get(5, TimeUnit.SECONDS).animationSpeed());
        assertEquals(5, second.get(5, TimeUnit.SECONDS).animationSpeed());
        assertEquals(5, store.read(playerId, "Alex").animationSpeed());
        service.shutdown(Duration.ofSeconds(5));
    }

    @Test
    void shutdownWithPendingWriteFlushesBeforeReturning() throws Exception {
        UUID playerId = UUID.randomUUID();
        CountDownLatch writeStarted = new CountDownLatch(1);
        CountDownLatch releaseWrite = new CountDownLatch(1);
        AtomicFileWriter systemWriter = AtomicFileWriter.system();
        PlayerDataStore store = new PlayerDataStore(temporaryDirectory, () -> 3, (target, content) -> {
            writeStarted.countDown();
            await(releaseWrite);
            systemWriter.write(target, content);
        });
        PlayerDataService service = new PlayerDataService(store);
        CompletableFuture<PlayerData> save = service.update(
            playerId, "Alex", data -> data.withAnimationSpeed(6));
        assertTrue(writeStarted.await(5, TimeUnit.SECONDS));

        CompletableFuture<PlayerDataService.ShutdownResult> shutdown = CompletableFuture.supplyAsync(
            () -> service.shutdown(Duration.ofSeconds(5)));
        releaseWrite.countDown();

        PlayerDataService.ShutdownResult result = shutdown.get(5, TimeUnit.SECONDS);
        assertTrue(result.completed());
        assertTrue(result.failures().isEmpty());
        assertEquals(6, save.get(5, TimeUnit.SECONDS).animationSpeed());
        assertEquals(6, store.read(playerId, "Alex").animationSpeed());
    }

    @Test
    void shutdownImmediatelyAfterChainedSavesDrainsAllAcceptedOperations() throws Exception {
        int playerCount = 40;
        int savesPerPlayer = 10;
        CountDownLatch workersBlocked = new CountDownLatch(2);
        CountDownLatch releaseWrites = new CountDownLatch(1);
        AtomicFileWriter systemWriter = AtomicFileWriter.system();
        PlayerDataStore store = new PlayerDataStore(temporaryDirectory, () -> 3, (target, content) -> {
            workersBlocked.countDown();
            await(releaseWrites);
            systemWriter.write(target, content);
        });
        PlayerDataService service = new PlayerDataService(store);
        List<CompletableFuture<PlayerData>> saves = new ArrayList<>();

        for (int playerIndex = 0; playerIndex < playerCount; playerIndex++) {
            UUID playerId = UUID.randomUUID();
            for (int saveIndex = 0; saveIndex < savesPerPlayer; saveIndex++) {
                int speed = saveIndex % 6 + 1;
                saves.add(service.update(
                    playerId,
                    "Player" + playerIndex,
                    data -> data.withAnimationSpeed(speed)
                ));
            }
        }
        assertTrue(workersBlocked.await(5, TimeUnit.SECONDS));

        CompletableFuture<PlayerDataService.ShutdownResult> shutdown = CompletableFuture.supplyAsync(
            () -> service.shutdown(Duration.ofSeconds(20)));
        assertNewWorkRejected(service);
        releaseWrites.countDown();

        CompletableFuture.allOf(saves.toArray(CompletableFuture[]::new)).get(20, TimeUnit.SECONDS);
        PlayerDataService.ShutdownResult result = shutdown.get(20, TimeUnit.SECONDS);
        assertTrue(result.completed());
        assertEquals(0, result.pendingTasks());
        assertTrue(result.failures().isEmpty());
        assertTrue(saves.stream().allMatch(future -> !future.isCompletedExceptionally()));
    }

    @Test
    void shutdownWithMoreDistinctPlayerChainsThanFormerQueueCapacityDrainsAllAcceptedOperations() throws Exception {
        int playerCount = 160;
        CountDownLatch workersBlocked = new CountDownLatch(2);
        CountDownLatch releaseWrites = new CountDownLatch(1);
        AtomicFileWriter systemWriter = AtomicFileWriter.system();
        PlayerDataStore store = new PlayerDataStore(temporaryDirectory, () -> 3, (target, content) -> {
            workersBlocked.countDown();
            await(releaseWrites);
            systemWriter.write(target, content);
        });
        PlayerDataService service = new PlayerDataService(store);
        List<CompletableFuture<PlayerData>> saves = new ArrayList<>();

        for (int playerIndex = 0; playerIndex < playerCount; playerIndex++) {
            UUID playerId = UUID.randomUUID();
            saves.add(service.update(
                playerId,
                "Player" + playerIndex,
                data -> data.withAnimationSpeed(6)
            ));
        }
        assertEquals(playerCount, saves.size());
        assertTrue(workersBlocked.await(5, TimeUnit.SECONDS));

        CompletableFuture<PlayerDataService.ShutdownResult> shutdown = CompletableFuture.supplyAsync(
            () -> service.shutdown(Duration.ofSeconds(20)));
        assertNewWorkRejected(service);
        releaseWrites.countDown();

        CompletableFuture.allOf(saves.toArray(CompletableFuture[]::new)).get(20, TimeUnit.SECONDS);
        PlayerDataService.ShutdownResult result = shutdown.get(20, TimeUnit.SECONDS);
        assertTrue(result.completed());
        assertEquals(0, result.pendingTasks());
        assertTrue(result.failures().isEmpty());
        assertTrue(saves.stream().allMatch(future -> !future.isCompletedExceptionally()));
    }

    @Test
    void shutdownWithExtremeTimeoutDoesNotOverflowIntoImmediateTimeout() throws Exception {
        CountDownLatch writeStarted = new CountDownLatch(1);
        CountDownLatch releaseWrite = new CountDownLatch(1);
        AtomicFileWriter systemWriter = AtomicFileWriter.system();
        PlayerDataStore store = new PlayerDataStore(temporaryDirectory, () -> 3, (target, content) -> {
            writeStarted.countDown();
            await(releaseWrite);
            systemWriter.write(target, content);
        });
        PlayerDataService service = new PlayerDataService(store);
        CompletableFuture<PlayerData> save = service.update(
            UUID.randomUUID(), "Alex", data -> data.withAnimationSpeed(6));
        assertTrue(writeStarted.await(5, TimeUnit.SECONDS));

        CompletableFuture<PlayerDataService.ShutdownResult> shutdown = CompletableFuture.supplyAsync(
            () -> service.shutdown(Duration.ofNanos(Long.MAX_VALUE)));
        assertNewWorkRejected(service);
        assertFalse(shutdown.isDone());
        releaseWrite.countDown();

        PlayerDataService.ShutdownResult result = shutdown.get(5, TimeUnit.SECONDS);
        assertTrue(result.completed());
        assertEquals(0, result.pendingTasks());
        assertTrue(result.failures().isEmpty());
        assertEquals(6, save.get(5, TimeUnit.SECONDS).animationSpeed());
    }

    @Test
    void shutdownWhenAcceptedWriteExceedsTimeoutReportsPendingFailure() throws Exception {
        CountDownLatch writeStarted = new CountDownLatch(1);
        CountDownLatch releaseWrite = new CountDownLatch(1);
        PlayerDataStore store = new PlayerDataStore(temporaryDirectory, () -> 3, (target, content) -> {
            writeStarted.countDown();
            await(releaseWrite);
        });
        PlayerDataService service = new PlayerDataService(store);
        CompletableFuture<PlayerData> save = service.update(
            UUID.randomUUID(), "Alex", data -> data.withAnimationSpeed(6));
        assertTrue(writeStarted.await(5, TimeUnit.SECONDS));

        PlayerDataService.ShutdownResult result = service.shutdown(Duration.ZERO);
        releaseWrite.countDown();

        assertFalse(result.completed());
        assertEquals(1, result.pendingTasks());
        assertFalse(result.failures().isEmpty());
        assertTrue(save.isCompletedExceptionally());
    }

    private void assertNewWorkRejected(PlayerDataService service) throws Exception {
        UUID probePlayerId = UUID.randomUUID();
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (System.nanoTime() < deadline) {
            CompletableFuture<PlayerData> probe = service.load(probePlayerId, "Probe");
            if (probe.isCompletedExceptionally()) {
                assertTrue(probe.handle((ignored, failure) ->
                    failure instanceof IllegalStateException).get(5, TimeUnit.SECONDS));
                return;
            }
            Thread.yield();
        }
        throw new AssertionError("Player data service did not stop accepting new work.");
    }

    private void await(CountDownLatch latch) throws java.io.IOException {
        try {
            if (!latch.await(5, TimeUnit.SECONDS)) {
                throw new java.io.IOException("Timed out waiting for test latch.");
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new java.io.IOException("Interrupted while waiting for test latch.", exception);
        }
    }
}
