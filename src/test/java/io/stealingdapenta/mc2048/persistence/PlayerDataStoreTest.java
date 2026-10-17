package io.stealingdapenta.mc2048.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.stealingdapenta.mc2048.utils.data.SavedGame;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PlayerDataStoreTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void updateWithCompletedGameWritesAllStatisticsAndClearsSavedGameAtomically() throws IOException {
        UUID playerId = UUID.randomUUID();
        PlayerDataStore store = new PlayerDataStore(temporaryDirectory, () -> 3);
        SavedGame savedGame = savedGame();
        store.update(playerId, "Alex", data -> new PlayerData(
            playerId, "Alex", 128, 2, 10_000, 64, 4, java.util.Optional.of(savedGame)));

        store.update(playerId, "Alex", data -> data.completed(512, 5_000, 213.33));

        PlayerData updated = store.read(playerId, "Alex");
        assertEquals(512, updated.highScore());
        assertEquals(3, updated.attempts());
        assertEquals(15_000, updated.playtime());
        assertEquals(213.33, updated.averageScore(), 0.001);
        assertTrue(updated.savedGame().isEmpty());
        try (java.util.stream.Stream<Path> files = Files.list(temporaryDirectory)) {
            assertFalse(files.anyMatch(path -> path.getFileName().toString().endsWith(".tmp")));
        }
    }

    @Test
    void updateWhenAtomicWriterFailsPropagatesFailureWithoutPublishingFile() {
        UUID playerId = UUID.randomUUID();
        PlayerDataStore store = new PlayerDataStore(
            temporaryDirectory,
            () -> 3,
            (target, content) -> {
                throw new IOException("disk full");
            }
        );

        IOException exception = assertThrows(
            IOException.class,
            () -> store.update(playerId, "Alex", data -> data.withAnimationSpeed(5))
        );

        assertEquals("disk full", exception.getMessage());
        assertFalse(Files.exists(temporaryDirectory.resolve(playerId + ".yml")));
    }

    @Test
    void readWithMissingSpeedUsesCurrentConfiguredDefault() throws IOException {
        AtomicInteger configuredDefault = new AtomicInteger(3);
        PlayerDataStore store = new PlayerDataStore(temporaryDirectory, configuredDefault::get);

        assertEquals(3, store.read(UUID.randomUUID(), "Alex").animationSpeed());
        configuredDefault.set(5);
        assertEquals(5, store.read(UUID.randomUUID(), "Steve").animationSpeed());
    }

    private SavedGame savedGame() {
        return new SavedGame(64, 5_000, 1, 32, false, boardWith(2, 4), List.of());
    }

    private List<Integer> boardWith(int... values) {
        List<Integer> board = new ArrayList<>(List.of(
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0
        ));
        for (int index = 0; index < values.length; index++) {
            board.set(index, values[index]);
        }
        return board;
    }
}
