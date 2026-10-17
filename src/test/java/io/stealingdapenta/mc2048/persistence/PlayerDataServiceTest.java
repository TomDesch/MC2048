package io.stealingdapenta.mc2048.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.Duration;
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
