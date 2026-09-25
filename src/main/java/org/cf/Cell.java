package org.cf;

public class Cell {
    private int row;
    private int col;
    private State state;

    public Cell(int row, int col, State state) {
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

    public State getState() {
        return state;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setState(State state) {
        this.state = state;
    }
}
