package org.cf;

public class MiniMax {
    Board board = new Board();

    public boolean canPlace(Cell cell) {
        int row = cell.getRow();
        int col = cell.getCol();

        if (cell.getState() != State.NONE) {
            return false;
        }

        if (row == Board.ROWS - 1) {
            return true;
        }

        Cell cellBellow = board.getCell(row + 1, col);

        if (cellBellow != null && cellBellow.getState() != State.NONE) {
            return true;
        }

        return false;
    }
}
