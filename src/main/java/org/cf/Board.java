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
                this.cells[row][col] = new Cell(row, col, BoardState.NONE);
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

        if (cells[row][col].getState() != BoardState.NONE) {
            return false;
        }

        if (row == Board.ROWS - 1) {
            return true;
        }

        Cell cellBelow = cells[row + 1][col];

        if (cellBelow != null && cellBelow.getState() != BoardState.NONE) {
            return true;
        }

        return false;
    }

    public int getAvailableRow(int col) {
        for (int row = Board.ROWS - 1; row >= 0; row--)
            if (this.cells[row][col].getState() == BoardState.NONE) {
                return row;
            }
        return -1;
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

        if (cells[row][col].getState() == BoardState.NONE) {
            return false;
        }

        this.cells[row][col] = new Cell(row, col, BoardState.NONE);
        moveCount--;
        return true;
    }

    public boolean checkWin(int startRow, int startCol) {
        Cell startCell = getCell(startRow, startCol);
        if (startCell == null || startCell.getState() == BoardState.NONE) {
            return false;
        }
        BoardState targetState = startCell.getState();

        int[][] dirs = {
                {0, 1},
                {1, 0},
                {1, 1},
                {1, -1}
        };

        for (int[] dir : dirs) {
            int dRow = dir[0];
            int dCol = dir[1];

            int count = 1;

            int r = startRow + dRow;
            int c = startCol + dCol;
            while (r >= 0 && r < ROWS && c >= 0 && c < COLS && cells[r][c].getState() == targetState) {
                count++;
                r += dRow;
                c += dCol;
            }

            r = startRow - dRow;
            c = startCol - dCol;
            while (r >= 0 && r < ROWS && c >= 0 && c < COLS && cells[r][c].getState() == targetState) {
                count++;
                r -= dRow;
                c -= dCol;
            }

            if (count >= 4) {
                return true;
            }
        }

        return false;
    }
}