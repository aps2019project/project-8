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

    
}
