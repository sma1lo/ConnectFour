package org.cf;

import java.util.Random;

public class MiniMax {
    private final Board board;
    private final Random random = new Random();
    private static final int DEPTH = 5;

    public MiniMax(Board board) {
        this.board = board;
    }

    public int findBestMove(BoardState state) {
        int bestMoveCol = -1;
        int bestValue = Integer.MIN_VALUE;

        for (int col = 0; col < Board.COLS; col++) {
            int row = board.getAvailableRow(col);
            if (row != -1) {
                Cell cell = new Cell(row, col, state);
                this.board.makeMove(cell);

                int moveValue = minimax(DEPTH - 1, false, row, col);

                this.board.undoMove(cell);

                if (moveValue > bestValue) {
                    bestValue = moveValue;
                    bestMoveCol = col;
                }
            }
        }

        if (bestMoveCol == -1) {
            int fallbackRow;
            do {
                bestMoveCol = random.nextInt(Board.COLS);
                fallbackRow = board.getAvailableRow(bestMoveCol);
            } while (fallbackRow == -1);
        }
        return bestMoveCol;
    }

    private int minimax(int depth, boolean isMaximizing, int lastRow, int lastCol) {
        if (lastRow != -1 && board.checkWin(lastRow, lastCol)) {
            return isMaximizing ? -1000 : 1000;
        }
        if (board.getMoves() == Board.ROWS * Board.COLS || depth == 0) {
            return 0;
        }
        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < Board.COLS; col++) {
                int row = board.getAvailableRow(col);
                if (row != -1) {
                    Cell cell = new Cell(row, col, BoardState.TEAM_A);
                    this.board.makeMove(cell);
                    int moveValue = minimax(depth - 1, false, row, col);
                    this.board.undoMove(cell);
                    if (moveValue > maxEval) {
                        maxEval = moveValue;
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < Board.COLS; col++) {
                int row = board.getAvailableRow(col);
                if (row != -1) {
                    Cell cell = new Cell(row, col, BoardState.TEAM_B);
                    this.board.makeMove(cell);
                    int moveValue = minimax(depth - 1, true, row, col);
                    this.board.undoMove(cell);
                    if (moveValue < minEval) {
                        minEval = moveValue;
                    }
                }
            }
            return minEval;
        }
    }
}
