package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.utils.FileManager.FILE_MANAGER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.stealingdapenta.mc2048.MC2048;
import io.stealingdapenta.mc2048.config.PlayerConfigField;
import io.stealingdapenta.mc2048.utils.data.SavedGame;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

class FileManagerTest {

    @TempDir
    Path temporaryDirectory;

    private MockedStatic<MC2048> mc2048;
    private Player player;

    @BeforeEach
    void setUp() {
        MC2048 plugin = mock(MC2048.class);
        when(plugin.getDataFolder()).thenReturn(temporaryDirectory.toFile());
        MC2048.logger = Logger.getLogger(FileManagerTest.class.getName());
        mc2048 = mockStatic(MC2048.class);
        mc2048.when(MC2048::getInstance).thenReturn(plugin);

        player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.getName()).thenReturn("TestPlayer");
    }

    @AfterEach
    void tearDown() {
        mc2048.close();
    }

    @Test
    void savesLoadsAndClearsUnfinishedGame() {
        SavedGame savedGame = new SavedGame(64, 5_000, 1, 32, false, boardWith(2, 4), List.of());

        FILE_MANAGER.saveGame(player, savedGame);

        assertEquals(savedGame, FILE_MANAGER.getSavedGame(player).orElseThrow());
        FILE_MANAGER.clearSavedGame(player);
        assertTrue(FILE_MANAGER.getSavedGame(player).isEmpty());
    }

    @Test
    void migratesLegacyAnimationDelayToSpeedLevel() {
        FILE_MANAGER.setValueByKey(player, "speed", 0);

        assertEquals(6, FILE_MANAGER.getAnimationSpeed(player));

        YamlConfiguration configuration = FILE_MANAGER.getConfig(player);
        assertEquals(6, configuration.getInt(PlayerConfigField.ANIMATION_SPEED.getKey()));
        assertFalse(configuration.contains("speed"));
    }

    private static List<Integer> boardWith(int... values) {
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
