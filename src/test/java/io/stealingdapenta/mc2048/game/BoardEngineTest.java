package io.stealingdapenta.mc2048.game;

import static io.stealingdapenta.mc2048.game.BoardEngine.Direction.DOWN;
import static io.stealingdapenta.mc2048.game.BoardEngine.Direction.LEFT;
import static io.stealingdapenta.mc2048.game.BoardEngine.Direction.RIGHT;
import static io.stealingdapenta.mc2048.game.BoardEngine.Direction.UP;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;

class BoardEngineTest {

    private final BoardEngine engine = new BoardEngine();

    @Test
    void moveUpWithSingleTileMovesToTop() {
        assertBoard(
            board(row(2, 0, 0, 0), row(0, 0, 0, 0), row(0, 0, 0, 0), row(0, 0, 0, 0)),
            engine.move(board(row(0, 0, 0, 0), row(0, 0, 0, 0), row(0, 0, 0, 0), row(2, 0, 0, 0)), UP)
                  .board()
        );
    }

    @Test
    void moveDownWithSingleTileMovesToBottom() {
        int[][] result = engine.move(
            board(row(2, 0, 0, 0), row(0, 0, 0, 0), row(0, 0, 0, 0), row(0, 0, 0, 0)),
            DOWN
        ).board();

        assertBoard(
            board(row(0, 0, 0, 0), row(0, 0, 0, 0), row(0, 0, 0, 0), row(2, 0, 0, 0)),
            result
        );
    }

    @Test
    void moveLeftWithSingleTileMovesToLeft() {
        int[][] result = engine.move(emptyExcept(0, 2, 2), LEFT).board();

        assertBoard(emptyExcept(0, 0, 2), result);
    }

    @Test
    void moveRightWithSingleTileMovesToRight() {
        int[][] result = engine.move(emptyExcept(0, 1, 2), RIGHT).board();

        assertBoard(emptyExcept(0, 3, 2), result);
    }

    @Test
    void moveLeftWithFourEqualTilesMergesEachTileOnce() {
        BoardEngine.MoveResult result = engine.move(
            board(row(2, 2, 2, 2), row(0, 0, 0, 0), row(0, 0, 0, 0), row(0, 0, 0, 0)),
            LEFT
        );

        assertBoard(
            board(row(4, 4, 0, 0), row(0, 0, 0, 0), row(0, 0, 0, 0), row(0, 0, 0, 0)),
            result.board()
        );
        assertEquals(8, result.scoreGain());
    }

    @Test
    void moveLeftWithChainedPatternDoesNotMergeCreatedTileAgain() {
        BoardEngine.MoveResult result = engine.move(
            board(row(2, 2, 4, 0), row(0, 0, 0, 0), row(0, 0, 0, 0), row(0, 0, 0, 0)),
            LEFT
        );

        assertBoard(
            board(row(4, 4, 0, 0), row(0, 0, 0, 0), row(0, 0, 0, 0), row(0, 0, 0, 0)),
            result.board()
        );
        assertEquals(4, result.scoreGain());
    }

    @Test
    void moveWithPackedDistinctTilesIsNoOp() {
        int[][] board = board(
            row(2, 4, 8, 16),
            row(32, 64, 128, 256),
            row(512, 1024, 2048, 4096),
            row(8192, 16384, 32768, 65536)
        );

        BoardEngine.MoveResult result = engine.move(board, LEFT);

        assertFalse(result.moved());
        assertEquals(0, result.scoreGain());
        assertBoard(board, result.board());
    }

    @Test
    void undoRestoresBoardAndScore() {
        int[][] previous = emptyExcept(0, 0, 2);

        BoardEngine.UndoResult result = engine.undo(previous, 12, 4);

        assertBoard(previous, result.board());
        assertEquals(8, result.score());
    }

    @Test
    void hasLegalMoveWithFullBoardAndNoMergeReturnsFalse() {
        int[][] board = board(
            row(2, 4, 2, 4),
            row(4, 2, 4, 2),
            row(2, 4, 2, 4),
            row(4, 2, 4, 2)
        );

        assertFalse(engine.hasLegalMove(board));
    }

    @Test
    void hasLegalMoveWithFullBoardAndAdjacentPairReturnsTrue() {
        int[][] board = board(
            row(2, 2, 4, 8),
            row(16, 32, 64, 128),
            row(256, 512, 1024, 2048),
            row(4096, 8192, 16384, 32768)
        );

        assertTrue(engine.hasLegalMove(board));
    }

    @Test
    void moveWithMaximumPairDoesNotOverflowOrMerge() {
        int[][] board = board(
            row(BoardEngine.MAXIMUM_TILE, BoardEngine.MAXIMUM_TILE, 0, 0),
            row(0, 0, 0, 0),
            row(0, 0, 0, 0),
            row(0, 0, 0, 0)
        );

        BoardEngine.MoveResult result = engine.move(board, LEFT);

        assertFalse(result.moved());
        assertEquals(0, result.scoreGain());
        assertBoard(board, result.board());
    }

    @Test
    void hasLegalMoveWithOnlyMaximumPairReturnsFalse() {
        int[][] board = board(
            row(BoardEngine.MAXIMUM_TILE, BoardEngine.MAXIMUM_TILE, 2, 4),
            row(8, 16, 32, 64),
            row(128, 256, 512, 1024),
            row(2048, 4096, 8192, 16384)
        );

        assertFalse(engine.hasLegalMove(board));
    }

    @Test
    void spawnWithHundredPercentFourPlacesFourInOnlyEmptyCell() {
        int[][] board = board(
            row(0, 2, 4, 8),
            row(16, 32, 64, 128),
            row(256, 512, 1024, 2048),
            row(4096, 8192, 16384, 32768)
        );

        BoardEngine.SpawnResult result = engine.spawn(board, 100, new Random(1));

        assertTrue(result.spawned());
        assertEquals(new BoardEngine.Cell(0, 0), result.cell());
        assertEquals(4, result.value());
        assertEquals(4, result.board()[0][0]);
    }

    @Test
    void spawnWithZeroPercentFourPlacesTwo() {
        int[][] board = board(
            row(0, 2, 4, 8),
            row(16, 32, 64, 128),
            row(256, 512, 1024, 2048),
            row(4096, 8192, 16384, 32768)
        );

        BoardEngine.SpawnResult result = engine.spawn(board, 0, new Random(1));

        assertEquals(2, result.value());
        assertEquals(2, result.board()[0][0]);
    }

    @Test
    void spawnWithFullBoardDoesNothing() {
        int[][] board = board(
            row(2, 4, 2, 4),
            row(4, 2, 4, 2),
            row(2, 4, 2, 4),
            row(4, 2, 4, 2)
        );

        BoardEngine.SpawnResult result = engine.spawn(board, 40, new Random(1));

        assertFalse(result.spawned());
        assertBoard(board, result.board());
    }

    private int[][] emptyExcept(int row, int column, int value) {
        int[][] board = new int[BoardEngine.SIZE][BoardEngine.SIZE];
        board[row][column] = value;
        return board;
    }

    private int[] row(int... values) {
        return values;
    }

    private int[][] board(int[]... rows) {
        return rows;
    }

    private void assertBoard(int[][] expected, int[][] actual) {
        assertEquals(expected.length, actual.length);
        for (int row = 0; row < expected.length; row++) {
            assertArrayEquals(expected[row], actual[row], "row " + row);
        }
    }
}
