package org.cf;

public class Board {
    public static final int ROWS = 6;
    public static final int COLS = 7;
    private final Cell[][] cells;

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
}
