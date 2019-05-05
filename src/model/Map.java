package model;

import java.util.ArrayList;

public class Map {

    private static final int MAX_DISPLACEMENT = 2;
    private static final int NUMBER_OF_ROWS = 6;
    private static final int NUMBER_OF_COLUMNS = 10;
    private Cell[][] grid;

    void passTurn() {
    }

    public Map() {
        grid = new Cell[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public static int getDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public static boolean isAdjacent(int x1, int y1, int x2, int y2) {
        return getDistance(x1, y1, x2, y2) == 1;
    }

    Cell[][] getGrid() {
        return this.grid;
    }

    Cell getCell(int r,int c) {return grid[r][c];}

    public boolean isPathEmpty(int srcX, int srcY, int desX, int desY) {
        if (grid[desX][desY].getContent() != null)
            return false;
        int distance = getDistance(srcX, srcY, desX, desY);
        if (distance == 1) {
            return true;
        }
        int dx = desX - srcX;
        int dy = desY - srcY;
        if (dx * dy != 0) {
            if (grid[srcX + dx][srcY].getContent() != null || grid[srcX][srcY + dy].getContent() != null)
                return true;
        } else {
            dx /= 2;
            dy /= 2;
            if (grid[srcX + dx][srcY + dy].getContent() != null && grid[srcX + dx + dx][srcY + dy + dy].getContent() != null)
                return true;
        }
        return false;
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
