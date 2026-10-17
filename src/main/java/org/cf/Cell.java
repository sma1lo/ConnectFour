package org.cf;

public class Cell {
    private int row;
    private int col;
    private BoardState state;

    public Cell(int row, int col, BoardState state) {
        this.row = row;
        this.col = col;
        this.state = state;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public BoardState getState() {
        return state;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setState(BoardState state) {
        this.state = state;
    }
}
