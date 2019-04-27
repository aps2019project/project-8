package model;

public class Game {
    private static enum GameState{WIN_FIRST_PLAYER, DRAW, WIN_SECOND_PLAYER}
    private static enum GameType{HOLD_THE_FLAG, COLLECT_THE_FLAGS, KILL_OPPONENT_HERO}

    private static final int NUMBER_OF_PLAYERS = 2;
    private static final int[] HERO_INITIAL_ROW = {2, 2};
    private static final int[] HERO_INITIAL_COLUMN = {0, 8};

    private int turn;
    private Map map;
    private Player[] players;
    private boolean[] hasAI;
    private Account[] accounts;

    private Unit selectedUnit;
    private Card selectedCard;
    private Collectible selectedCollectible;
    private GameType gameType;

    Player getCurrentPlayer() {
        if (turn % 2 == 0) {
            return players[0];
        } else {
            return players[1];
        }
    }

    Map getMap() {
        return this.map;
    }

    void moveSelectedUnit(int destinationRow, int destinationColumn) {

    }

    void attackTargetCardWithSelectedUnit(Card targetCard) {

    }

    void attackCombo(Card targetCard, Card[] friendlyCards) {

    }

    void castSelectedSpellCard(int destinationRow, int destinationColumn) {

    }

    void castSpellOnMap(Spell spell, int destinationRow, int destinationColumn) {

    }

    void useUnitSpecialPower(int destinationRow, int destinationColumn) {

    }

    private void endGame() {

    }

    private GameState getGameState() {
        return GameState.DRAW;
    }

    private void rewardPlayer(Player player) {

    }

    void moveCardToGraveYard(Card card) { // How to access the card

    }

    void selectCard(String cardID) {

    }

    void selectUnit(int row, int column) {

    }

    void initiateGame() {

    }

    void showHand() {

    }

    void putCardOnMap(Card card, int row, int column) {

    }

    void endTurn() {

    }

    void selectCollectibleItem(String collectibleName) {

    }

    void applyCollectible(int row, int column) {

    }

    void showNextCardInDeck() {

    }

    void showGraveYardInfo() {

    }

    void showGraveYardCards() {

    }

}
