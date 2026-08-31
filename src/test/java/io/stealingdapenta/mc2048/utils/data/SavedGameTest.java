package io.stealingdapenta.mc2048.utils.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

class SavedGameTest {

    @Test
    void roundTripsThroughYaml() {
        SavedGame savedGame = new SavedGame(
            128,
            12_345,
            1,
            64,
            false,
            boardWith(2, 4, 8),
            boardWith(2, 4)
        );
        YamlConfiguration configuration = new YamlConfiguration();
        ConfigurationSection section = configuration.createSection("active-game");

        savedGame.writeTo(section);

        assertEquals(savedGame, SavedGame.from(section));
    }

    @Test
    void rejectsUnknownTileValues() {
        List<Integer> board = boardWith(2, 4);
        board.set(5, 3);

        assertThrows(
            IllegalArgumentException.class,
            () -> new SavedGame(0, 0, 1, 0, false, board, List.of())
        );
    }

    @Test
    void rejectsIncompleteBoards() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new SavedGame(0, 0, 1, 0, false, List.of(2, 4), List.of())
        );
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
