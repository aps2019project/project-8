package model;

public class AreaOfEffect extends Spell {
    private static int MAP_ROWS;
    private static int MAP_COLUMNS;
    private int rowsEffected;
    private int columnsEffected;
    private boolean affectsUnits;
    private boolean affectsCells;

    @Override
    public void cast(int x, int y, Map map, Player player) {
    }
}
