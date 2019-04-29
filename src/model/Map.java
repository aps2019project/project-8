package model;

public class Map {
    private static final int MAX_DISPLACEMENT = 2;
    private static final int NUMBER_OF_ROWS = 5;
    private static final int NUMBER_OF_COLUMNS = 9;
    private Cell[][] grids;

    void passTurn() {

    }

    Cell[][] getCells() {
        return this.grids;
    }

    boolean isPathEmpty(int sourceR, int sourceC, int destinationR, int destinationC) {
        return true;
    }
}
