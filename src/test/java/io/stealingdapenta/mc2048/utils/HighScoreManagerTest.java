package io.stealingdapenta.mc2048.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.bukkit.configuration.file.YamlConfiguration;

class HighScoreManagerTest {

    private static final Logger LOGGER = Logger.getLogger(HighScoreManagerTest.class.getName());

    @Test
    void recordScoreWithMultiplePlayersSortsScoresInMemory() {
        HighScoreManager manager = new HighScoreManager(LOGGER);

        UUID alex = UUID.randomUUID();
        UUID steve = UUID.randomUUID();
        manager.recordScore(alex, "Alex", 128);
        manager.recordScore(steve, "Steve", 512);
        manager.recordScore(alex, "Alex", 64);

        assertEquals(List.of("Steve", "Alex"), manager.getHighScores().keySet().stream().toList());
        assertEquals(128, manager.getHighScores().get("Alex"));
    }

    @Test
    void recordScoreWithChangedPlayerNameReplacesStaleName() {
        HighScoreManager manager = new HighScoreManager(LOGGER);
        UUID playerId = UUID.randomUUID();

        manager.recordScore(playerId, "OldName", 128);
        manager.recordScore(playerId, "NewName", 128);

        assertEquals(List.of("NewName"), manager.getHighScores().keySet().stream().toList());
    }

    @Test
    void getPlayerPositionWithEmptyCacheReturnsUnranked() {
        HighScoreManager manager = new HighScoreManager(LOGGER);

        OptionalInt position = manager.getPlayerPosition(UUID.randomUUID(), "Alex");

        assertTrue(position.isEmpty());
    }

    @Test
    void refreshWithMalformedFilesRetainsValidEntries(@TempDir Path directory) throws IOException {
        UUID validId = UUID.randomUUID();
        writePlayerFile(directory.resolve(validId + ".yml"), "Alex", 512);
        writePlayerFile(directory.resolve("not-a-uuid.yml"), "Invalid", 1024);
        Files.writeString(directory.resolve(UUID.randomUUID() + ".yml"), "high-score: [unclosed");
        HighScoreManager manager = new HighScoreManager(LOGGER);

        manager.refresh(directory.toFile());

        assertEquals(Map.of("Alex", 512), manager.getHighScores());
    }

    private void writePlayerFile(Path file, String playerName, int highScore) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("Player Name", playerName);
        configuration.set("high-score", highScore);
        configuration.save(file.toFile());
    }
}
