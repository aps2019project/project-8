package model;

import menus.InGameMenu;

public class Game extends InGameMenu {
    private static final int NUMBER_OF_PLAYERS = 2;
    private static final int[] HERO_INITIAL_ROW = {2, 2};
    private static final int[] HERO_INITIAL_COLUMN = {0, 8};

    private int numberOfFlags;
    private int turn;
    private Map map;
    private Player[] players;
    private boolean[] hasAI;
    private Account[] accounts;

    private Unit selectedUnit;

    private Card selectedCard; // probably has no use


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

    private int getDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    void moveSelectedUnit(int destinationRow, int destinationColumn) {
        if (getDistance(selectedUnit.getX(), selectedUnit.getY(), destinationRow, destinationColumn) <= 2 || selectedUnit.canFly()) { // possibly we could add some moveRange to Unit class variables
            if (map.isPathEmpty(selectedUnit.getX(), selectedUnit.getY(), destinationRow, destinationColumn)) {
                map.getGrid()[selectedUnit.getX()][selectedUnit.getY()].setContent(null);
                map.getGrid()[destinationRow][destinationColumn].setContent(selectedUnit);
                selectedUnit.setX(destinationRow);
                selectedUnit.setY(destinationColumn);
                view.logMessage(selectedUnit.getID() + " moved to " + destinationRow + " " + destinationColumn);
                return;
            }
        }
        view.showInvalidTargetError();
    }

    private boolean isAdjacent(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) <= 1 && Math.abs(y1 - y2) <= 1 && (x1 != x2 || y1 != y2);
    }

    // returns true if attack happens
    private boolean attackUnitByUnit(Unit attacker, Unit defender) {
        int distance = getDistance(attacker.getX(), attacker.getY(), defender.getX(), defender.getY());
        boolean isAdjacent = isAdjacent(attacker.getX(), attacker.getY(), defender.getX(), defender.getY());
        if ((attacker.getUnitType() == UnitType.MELEE || attacker.getUnitType() == UnitType.HYBRID) && isAdjacent) {
            defender.receiveHit(attacker.getAttackPoint());
            // attack point of attacker should be calculated considering buffs and items attacker has
            return true;
        }
        if ((attacker.getUnitType() == UnitType.RANGED || attacker.getUnitType() == UnitType.HYBRID) && !isAdjacent && attacker.getAttackRange() >= distance) {
            defender.receiveHit(attacker.getAttackPoint());
            // attack point of attacker should be calculated considering buffs and items attacker has
            return true;
        }
        return false;
    }

    void attackTargetCardWithSelectedUnit(String targetCardID) {
        Unit targetUnit = findUnitInGridByID(targetCardID);
        if (targetUnit == null) { // invalid card id
            view.showInvalidCardIDError();
            return;
        }
        if (!selectedUnit.getCanAttack()) { // has already attacked before
            view.logMessage("Card with " + selectedUnit.getID() + " can't attack");
            return;
        }

        boolean attacked = attackUnitByUnit(selectedUnit, targetUnit);
        if (!attacked) { // no attack happens so ...
            view.logMessage("opponent minion is unavailable for attack");
            return;
        }
        selectedUnit.setCanAttack(false);
        attackUnitByUnit(targetUnit, selectedUnit);
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

    private Unit findUnitInGridByID(String cardID) {
        for (Cell[] rowCells : map.getGrid()) {
            for (Cell cell : rowCells) {
                if (cell.getContent() instanceof Unit) {
                    Unit unit = (Unit) cell.getContent();
                    if (unit.getID().contentEquals(cardID)) {
                        return unit;
                    }
                }
            }
        }
        return null;
    }

    void selectCard(String cardID) {
        Unit unit = findUnitInGridByID(cardID);
        // actually only minions and heroes can be found on grid so we just have units on grid
        if (unit == null) { // cardID is invalid and not found in the grid
            view.showInvalidCardIDError();
            return;
        }
        selectedUnit = unit;
    }

    // inserts card with name [cardName] from player's hand and puts it in cell ([x], [y])
    void insertCard(String cardName, int x, int y) {
        Player player = getCurrentPlayer();
        Card card = player.findCard(cardName);
        if (card == null) { // no such card is found in player's hand
            view.showInvalidCardError();
            return;
        }
        if (x < 0 || x >= Map.NUMBER_OF_COLUMNS || y < 0 || y >= Map.NUMBER_OF_ROWS) {
            view.showInvalidCoordinatesError();
            return;
        }
        Cell[][] grid = map.getGrid();
        if (grid[x][y].getContent() != null) { // the cell is already not empty
            view.showInvalidTargetError();
            return;
        }
        if (player.getMana() < card.getManaCost()) { // player doesn't have enough mana
            view.showNotEnoughManaError();
            return;
        }
        grid[x][y].setContent(card); // finally put the card on cell ([x], [y])
        view.logMessage(cardName + " with " + card.getID() + " inserted to " + "(" + x + "," + y + ")"); // log success message
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

    private enum GameState {WIN_FIRST_PLAYER, DRAW, WIN_SECOND_PLAYER}
}
