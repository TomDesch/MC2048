package io.stealingdapenta.mc2048.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.random.RandomGenerator;

public final class BoardEngine {

    public static final int SIZE = 4;
    public static final int MAXIMUM_TILE = 262_144;

    public MoveResult move(int[][] source, Direction direction) {
        int[][] board = copyAndValidate(source);
        boolean[][] mergedCells = new boolean[SIZE][SIZE];
        List<Movement> movements = new ArrayList<>();
        int[] scoreGain = {0};

        switch (direction) {
            case UP -> traverse(board, mergedCells, movements, scoreGain, 1, SIZE, 1, 0, SIZE, 1, -1, 0);
            case DOWN -> traverse(board, mergedCells, movements, scoreGain, SIZE - 2, -1, -1, 0, SIZE, 1, 1, 0);
            case LEFT -> traverse(board, mergedCells, movements, scoreGain, 0, SIZE, 1, 1, SIZE, 1, 0, -1);
            case RIGHT -> traverse(board, mergedCells, movements, scoreGain, 0, SIZE, 1, SIZE - 2, -1, -1, 0, 1);
        }
        return new MoveResult(board, scoreGain[0], List.copyOf(movements));
    }

    public boolean hasLegalMove(int[][] source) {
        int[][] board = copyAndValidate(source);
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (board[row][column] == 0) {
                    return true;
                }
                if (row + 1 < SIZE && canMerge(board[row][column], board[row + 1][column])) {
                    return true;
                }
                if (column + 1 < SIZE && canMerge(board[row][column], board[row][column + 1])) {
                    return true;
                }
            }
        }
        return false;
    }

    public SpawnResult spawn(int[][] source, int fourPercentage, RandomGenerator random) {
        if (fourPercentage < 0 || fourPercentage > 100) {
            throw new IllegalArgumentException("Four percentage must be between 0 and 100.");
        }
        int[][] board = copyAndValidate(source);
        List<Cell> emptyCells = new ArrayList<>();
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (board[row][column] == 0) {
                    emptyCells.add(new Cell(row, column));
                }
            }
        }
        if (emptyCells.isEmpty()) {
            return new SpawnResult(board, null, 0);
        }
        Cell selected = emptyCells.get(random.nextInt(emptyCells.size()));
        int value = random.nextInt(100) < fourPercentage ? 4 : 2;
        board[selected.row()][selected.column()] = value;
        return new SpawnResult(board, selected, value);
    }

    public UndoResult undo(int[][] previousBoard, int currentScore, int scoreGainedAfterLastMove) {
        if (scoreGainedAfterLastMove < 0 || scoreGainedAfterLastMove > currentScore) {
            throw new IllegalArgumentException("Invalid score restoration amount.");
        }
        return new UndoResult(copyAndValidate(previousBoard), currentScore - scoreGainedAfterLastMove);
    }

    private void traverse(
        int[][] board,
        boolean[][] mergedCells,
        List<Movement> movements,
        int[] scoreGain,
        int rowStart,
        int rowEnd,
        int rowStep,
        int columnStart,
        int columnEnd,
        int columnStep,
        int rowDelta,
        int columnDelta
    ) {
        for (int row = rowStart; row != rowEnd; row += rowStep) {
            for (int column = columnStart; column != columnEnd; column += columnStep) {
                if (board[row][column] != 0) {
                    processCell(
                        row, column, rowDelta, columnDelta, board, mergedCells, movements, scoreGain);
                }
            }
        }
    }

    private void processCell(
        int row,
        int column,
        int rowDelta,
        int columnDelta,
        int[][] board,
        boolean[][] mergedCells,
        List<Movement> movements,
        int[] scoreGain
    ) {
        int currentRow = row;
        int currentColumn = column;
        int stepCount = 0;
        List<Movement> tileMovements = new ArrayList<>();

        while (isInBounds(currentRow + rowDelta, currentColumn + columnDelta)
            && board[currentRow + rowDelta][currentColumn + columnDelta] == 0) {
            int fromRow = currentRow;
            int fromColumn = currentColumn;
            currentRow += rowDelta;
            currentColumn += columnDelta;
            stepCount++;
            int value = board[fromRow][fromColumn];
            tileMovements.add(new Movement(
                fromRow, fromColumn, currentRow, currentColumn, value, false, 0, stepCount, 0));
            board[currentRow][currentColumn] = value;
            board[fromRow][fromColumn] = 0;
        }

        int destinationRow = currentRow + rowDelta;
        int destinationColumn = currentColumn + columnDelta;
        if (isInBounds(destinationRow, destinationColumn)
            && !mergedCells[destinationRow][destinationColumn]
            && canMerge(board[currentRow][currentColumn], board[destinationRow][destinationColumn])) {
            stepCount++;
            int value = board[currentRow][currentColumn];
            int mergedValue = value * 2;
            tileMovements.add(new Movement(
                currentRow,
                currentColumn,
                destinationRow,
                destinationColumn,
                value,
                true,
                mergedValue,
                stepCount,
                0
            ));
            board[destinationRow][destinationColumn] = mergedValue;
            board[currentRow][currentColumn] = 0;
            mergedCells[destinationRow][destinationColumn] = true;
            scoreGain[0] += mergedValue;
        }

        int totalSteps = stepCount;
        tileMovements.forEach(movement -> movements.add(movement.withTotalSteps(totalSteps)));
    }

    private boolean canMerge(int first, int second) {
        return first != 0 && first == second && first < MAXIMUM_TILE;
    }

    private boolean isInBounds(int row, int column) {
        return row >= 0 && row < SIZE && column >= 0 && column < SIZE;
    }

    private int[][] copyAndValidate(int[][] source) {
        if (source.length != SIZE) {
            throw new IllegalArgumentException("Board must have four rows.");
        }
        int[][] copy = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            if (source[row].length != SIZE) {
                throw new IllegalArgumentException("Board rows must have four columns.");
            }
            copy[row] = Arrays.copyOf(source[row], SIZE);
            for (int value : copy[row]) {
                if (value != 0 && (value < 2 || Integer.bitCount(value) != 1 || value > MAXIMUM_TILE)) {
                    throw new IllegalArgumentException("Invalid board tile: " + value);
                }
            }
        }
        return copy;
    }

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public record Cell(int row, int column) {
    }

    public record Movement(
        int fromRow,
        int fromColumn,
        int toRow,
        int toColumn,
        int value,
        boolean merge,
        int mergedValue,
        int step,
        int totalSteps
    ) {
        private Movement withTotalSteps(int totalSteps) {
            return new Movement(
                fromRow, fromColumn, toRow, toColumn, value, merge, mergedValue, step, totalSteps);
        }
    }

    public record MoveResult(int[][] board, int scoreGain, List<Movement> movements) {
        public MoveResult {
            board = deepCopy(board);
        }

        @Override
        public int[][] board() {
            return deepCopy(board);
        }

        public boolean moved() {
            return !movements.isEmpty();
        }
    }

    public record SpawnResult(int[][] board, Cell cell, int value) {
        public SpawnResult {
            board = deepCopy(board);
        }

        @Override
        public int[][] board() {
            return deepCopy(board);
        }

        public boolean spawned() {
            return cell != null;
        }
    }

    public record UndoResult(int[][] board, int score) {
        public UndoResult {
            board = deepCopy(board);
        }

        @Override
        public int[][] board() {
            return deepCopy(board);
        }
    }

    private static int[][] deepCopy(int[][] board) {
        int[][] copy = new int[board.length][];
        for (int row = 0; row < board.length; row++) {
            copy[row] = Arrays.copyOf(board[row], board[row].length);
        }
        return copy;
    }
}
