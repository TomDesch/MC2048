package io.stealingdapenta.mc2048.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class HighScoreManagerTest {

    @Test
    void recordsAndSortsScoresInMemory() {
        HighScoreManager manager = new HighScoreManager();

        UUID alex = UUID.randomUUID();
        UUID steve = UUID.randomUUID();
        manager.recordScore(alex, "Alex", 128);
        manager.recordScore(steve, "Steve", 512);
        manager.recordScore(alex, "Alex", 64);

        assertEquals(List.of("Steve", "Alex"), manager.getHighScores().keySet().stream().toList());
        assertEquals(128, manager.getHighScores().get("Alex"));
    }

    @Test
    void replacesAPlayersStaleName() {
        HighScoreManager manager = new HighScoreManager();
        UUID playerId = UUID.randomUUID();

        manager.recordScore(playerId, "OldName", 128);
        manager.recordScore(playerId, "NewName", 128);

        assertEquals(List.of("NewName"), manager.getHighScores().keySet().stream().toList());
    }
}
