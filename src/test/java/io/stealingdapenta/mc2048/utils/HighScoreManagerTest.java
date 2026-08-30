package io.stealingdapenta.mc2048.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class HighScoreManagerTest {

    @Test
    void recordsAndSortsScoresInMemory() {
        HighScoreManager manager = new HighScoreManager();

        manager.recordScore("Alex", 128);
        manager.recordScore("Steve", 512);
        manager.recordScore("Alex", 64);

        assertEquals(List.of("Steve", "Alex"), manager.getHighScores().keySet().stream().toList());
        assertEquals(128, manager.getHighScores().get("Alex"));
    }
}
