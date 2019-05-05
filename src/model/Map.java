package model;

public class Map {

    private static final int MAX_DISPLACEMENT = 2;
    private static final int NUMBER_OF_ROWS = 5;
    private static final int NUMBER_OF_COLUMNS = 9;
    private Cell[][] grid;

    void passTurn() {

    }

    public Map() {
        grid = new Cell[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
    }

    Cell[][] getGrid() {
        return this.grid;
    }

    Cell getCell(int r,int c) {return grid[r][c];}

    boolean isPathEmpty(int sourceR, int sourceC, int destinationR, int destinationC) {
        return true;
    }

    public int getMaxDisplacement() {
        return MAX_DISPLACEMENT;
    }

    public int getNumberOfRows() {
        return NUMBER_OF_ROWS;
    }

    public int getNumberOfColumns() {
        return NUMBER_OF_COLUMNS;
    }
}
