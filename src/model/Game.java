package model;

public class Game {
    private static final int NUMBER_OF_PLAYERS = 2;
    private static final int[] HERO_INITIAL_ROW = {2, 2};
    private static final int[] HERO_INITIAL_COLUMN = {0, 8};

    private int turn;
    private Map map;
    private Player[] players;
    private Account[] accounts;

    private Unit selectedUnit;
    private Card selectedCard;

    Map getMap() {
        return this.map;
    }

    void moveSelectedUnit(int destinationRow, int destinationColumn) {

    }

    void attackTargetCardWithSelectedUnit(Card targetCard) {

    }

    void attackCombo(Card targetCard, Card[] friendlyCards) {

    }

    void castSpellCard(int destinationRow, int destinationColumn) {

    }

    void
}
