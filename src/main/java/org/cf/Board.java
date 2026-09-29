package org.cf;

public class Board {
    public static final int ROWS = 6;
    public static final int COLS = 7;
    private final Cell[][] cells;
    private int moveCount = 0;

    public Board() {
        cells = new Cell[ROWS][COLS];

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                this.cells[row][col] = new Cell(row, col, State.NONE);
            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Cell getCell(int row, int col) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
            return cells[row][col];
        }
        return null;
    }

    public boolean canPlace(Cell cell) {
        if (cell == null) {
            return false;
        }

        int row = cell.getRow();
        int col = cell.getCol();

        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return false;
        }

        if (cells[row][col].getState() != State.NONE) {
            return false;
        }

        if (row == Board.ROWS - 1) {
            return true;
        }

        Cell cellBelow = cells[row + 1][col];

        if (cellBelow != null && cellBelow.getState() != State.NONE) {
            return true;
        }

        return false;
    }

    public boolean makeMove(Cell cell) {
        if (!canPlace(cell)) {
            return false;
        }
        int row = cell.getRow();
        int col = cell.getCol();

        this.cells[row][col] = new Cell(row, col, cell.getState());

        moveCount++;
        return true;
    }

    public int getMoves() {
        return this.moveCount;
    }

    public boolean undoMove(Cell cell) {
        if (cell == null) {
            return false;
        }

        int row = cell.getRow();
        int col = cell.getCol();

        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return false;
        }

        if (cells[row][col].getState() == State.NONE) {
            return false;
        }

        this.cells[row][col] = new Cell(row, col, State.NONE);
        moveCount--;
        return true;
    }
}
