package org.cf;

import java.util.Random;

public class MiniMax {
    private final Board board;
    private final Random random = new Random();

    public MiniMax(Board board) {
        this.board = board;
    }

    public int findBestMove(BoardState state) {
        for (int col = 0; col < Board.COLS; col++) {
            int row = board.getAvailableRow(col);
            if (row != -1) {
                Cell cell = new Cell(row, col, state);
                this.board.makeMove(cell);
                boolean isWin = board.checkWin(row, col);
                this.board.undoMove(cell);
                if (isWin) {
                    return col;
                }
            }
        }

        int randomCol;
        int row;
        do {
            randomCol = random.nextInt(Board.COLS);
            row = board.getAvailableRow(randomCol);
        } while (row == -1);
        return randomCol;
    }
}
