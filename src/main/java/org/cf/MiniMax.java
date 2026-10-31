package org.cf;

public class MiniMax {
    private final Board board;
    private static final int DEPTH = 9;

    public MiniMax(Board board) {
        this.board = board;
    }

    public int findBestMove(BoardState state) {
        BoardState maximizer = state;
        BoardState minimizer = (state == BoardState.TEAM_A) ? BoardState.TEAM_B : BoardState.TEAM_A;

        int bestMoveCol = -1;
        int bestValue = Integer.MIN_VALUE;

        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        int[] colOrder = {3, 2, 4, 1, 5, 0, 6};

        for (int col : colOrder) {
            int row = board.getAvailableRow(col);
            if (row != -1) {
                Cell cell = new Cell(row, col, maximizer);
                this.board.makeMove(cell);

                int moveValue = minimax(DEPTH - 1, false, row, col, maximizer, minimizer, alpha, beta);

                this.board.undoMove(cell);

                if (moveValue > bestValue) {
                    bestValue = moveValue;
                    bestMoveCol = col;
                }

                alpha = Math.max(alpha, bestValue);
            }
        }

        if (bestMoveCol == -1) {
            for (int col : colOrder) {
                if (board.getAvailableRow(col) != -1) {
                    return col;
                }
            }
        }
        return bestMoveCol;
    }

    private int minimax(int depth, boolean isMaximizing, int lastRow, int lastCol, BoardState maximizer, BoardState minimizer, int alpha, int beta) {
        if (lastRow != -1 && board.checkWin(lastRow, lastCol)) {
            return isMaximizing ? (-1000000 - depth) : (1000000 + depth);
        }

        if (board.getMoves() == Board.ROWS * Board.COLS || depth == 0) {
            return evaluateBoard(maximizer, minimizer);
        }

        int[] colOrder = {3, 2, 4, 1, 5, 0, 6};

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col : colOrder) {
                int row = board.getAvailableRow(col);
                if (row != -1) {
                    Cell cell = new Cell(row, col, maximizer);
                    this.board.makeMove(cell);
                    int moveValue = minimax(depth - 1, false, row, col, maximizer, minimizer, alpha, beta);
                    this.board.undoMove(cell);

                    maxEval = Math.max(maxEval, moveValue);
                    alpha = Math.max(alpha, moveValue);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col : colOrder) {
                int row = board.getAvailableRow(col);
                if (row != -1) {
                    Cell cell = new Cell(row, col, minimizer);
                    this.board.makeMove(cell);
                    int moveValue = minimax(depth - 1, true, row, col, maximizer, minimizer, alpha, beta);
                    this.board.undoMove(cell);

                    minEval = Math.min(minEval, moveValue);
                    beta = Math.min(beta, moveValue);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return minEval;
        }
    }

    public int evaluateBoard(BoardState botState, BoardState playerState) {
        int score = 0;
        int centerCol = Board.COLS / 2;

        for (int row = 0; row < Board.ROWS; row++) {
            if (board.getCell(row, centerCol).getState() == botState) {
                score += 4;
            } else if (board.getCell(row, centerCol).getState() == playerState) {
                score -= 4;
            }
        }

        int botThrees  = board.countWindows(botState, 3);
        int botTwos    = board.countWindows(botState, 2);

        int playerThrees = board.countWindows(playerState, 3);
        int playerTwos   = board.countWindows(playerState, 2);

        score += botThrees * 100;
        score += botTwos   * 10;

        score -= playerThrees * 100;
        score -= playerTwos   * 10;

        return score;
    }
}