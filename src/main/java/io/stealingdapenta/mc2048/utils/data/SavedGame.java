package io.stealingdapenta.mc2048.utils.data;

import java.util.List;
import org.bukkit.configuration.ConfigurationSection;

public record SavedGame(
    int score,
    long elapsedPlaytime,
    int undoRemaining,
    int scoreGainedAfterLastMove,
    boolean lastMoveUndo,
    List<Integer> board,
    List<Integer> previousBoard
) {

    private static final int BOARD_SIZE = 16;

    public SavedGame {
        if (score < 0 || elapsedPlaytime < 0 || scoreGainedAfterLastMove < 0) {
            throw new IllegalArgumentException("Saved game values cannot be negative.");
        }
        board = validateBoard(board, false);
        previousBoard = validateBoard(previousBoard, true);
    }

    public static SavedGame from(ConfigurationSection section) {
        return new SavedGame(
            section.getInt("score"),
            section.getLong("elapsed-playtime"),
            section.getInt("undo-remaining"),
            section.getInt("score-gained-after-last-move"),
            section.getBoolean("last-move-undo"),
            section.getIntegerList("board"),
            section.getIntegerList("previous-board")
        );
    }

    public void writeTo(ConfigurationSection section) {
        section.set("score", score);
        section.set("elapsed-playtime", elapsedPlaytime);
        section.set("undo-remaining", undoRemaining);
        section.set("score-gained-after-last-move", scoreGainedAfterLastMove);
        section.set("last-move-undo", lastMoveUndo);
        section.set("board", board);
        section.set("previous-board", previousBoard);
    }

    private static List<Integer> validateBoard(List<Integer> values, boolean optional) {
        List<Integer> copy = List.copyOf(values);
        if (optional && copy.isEmpty()) {
            return copy;
        }
        if (copy.size() != BOARD_SIZE) {
            throw new IllegalArgumentException("Saved game boards must contain exactly " + BOARD_SIZE + " cells.");
        }
        if (copy.stream().anyMatch(value -> value != 0 && NumberRepresentation.fromScore(value).isEmpty())) {
            throw new IllegalArgumentException("Saved game board contains an unknown tile value.");
        }
        return copy;
    }
}
