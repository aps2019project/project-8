package model;

import menus.InGameMenu;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;

public class Game extends InGameMenu {

    class Pair {
        int x;
        int y;
        Pair (int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

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

    private boolean inMap(int x, int y) {
        return x >= 0 && x < map.getNumberOfRows() && y >= 0 && y < map.getNumberOfColumns();
    }

    //returns true if target matches spell TargetType and spell TargetUnit (in case TargetType is Unit)

    private boolean isValidTarget(Spell spell, int x, int y) {
        if (spell.getTargetType() == Spell.TargetType.CELL) {
            return true;
        }
        Cell cell = map.getGrid()[x][y];
        Unit unit = (Unit) cell.getContent();
        if (unit == null) {
            return false;
        }
        boolean isForCurrentPlayer = cell.getObjectOwner() == getCurrentPlayer();
        boolean isMinion = cell.getContent() instanceof Minion;
        boolean isHero = cell.getContent() instanceof Hero;
        boolean valid = false;
        switch (spell.getTargetUnit()) {
            case FRIENDLY_UNIT:
                valid = isForCurrentPlayer;
                break;
            case FRIENDLY_HERO:
                valid = isForCurrentPlayer && isHero;
                break;
            case FRIENDLY_MINION:
                valid = isForCurrentPlayer && isMinion;
                break;
            case ENEMY_UNIT:
                valid = !isForCurrentPlayer;
                break;
            case ENEMY_HERO:
                valid = !isForCurrentPlayer && isHero;
                break;
            case ENEMY_MINION:
                valid = !isForCurrentPlayer && isMinion;
                break;
            case SELF:
                valid = true;
                break;
            case UNIT:
                valid = true;
                break;
        }
        return valid;
    }

    private void castSpellOnCellUnit(Spell spell, int x, int y) {
        boolean valid = isValidTarget(spell, x, y);
        Unit unit = (Unit) map.getGrid()[x][y].getContent();
        if (valid) {
            for (Buff buff : spell.getBuffs()) {
                unit.addBuff(buff);
            }
        }
    }

    private void castSpellOnCell(Spell spell, int x, int y) {
        Cell cell = map.getGrid()[x][y];
        for (Buff buff : spell.getBuffs()) {
            cell.addEffect(buff);
        }
    }

    private void castSpellOnCoordinate(Spell spell, int x, int y) {
        switch (spell.getTargetType()) {
            case UNIT:
                castSpellOnCellUnit(spell, x, y);
                break;
            case CELL:
                castSpellOnCell(spell, x, y);
                break;
        }
    }

    // if spell is from a spell card (x, y) should be target point of spell
    // in this case for adjacent 8 and adjacent 9 the center cell must be given not the left most and upper most
    // for selected X_Y grid the upper most left most must be given
    // if spell is from a Unit special power (x, y) should be coordination of the unit itself

    // returns true if spell had

    private boolean castSpell(Spell spell, int x, int y) {
        Spell.TargetArea area = spell.getTargetArea();
        boolean ret = false;

        ArrayList<Pair> targets = new ArrayList<>(0);

        switch (spell.getTargetArea()) {
            case ALL_OF_THE_MAP:
                for (int i = 0; i < map.getNumberOfRows(); i++) {
                    for (int j = 0; j < map.getNumberOfColumns(); j++) {
                        if (isValidTarget(spell, i, j)) {
                            targets.add(new Pair(i, j));
                        }
                    }
                }
                break;

            case ADJACENT_9:
                if (isValidTarget(spell, x, y)) {
                    targets.add(new Pair(x, y));
                }

            case ADJACENT_8:
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if (inMap(i, j) && isValidTarget(spell, i, j)) {
                            targets.add(new Pair(i, j));
                        }
                    }
                }
                break;

            case ADJACENT_4:
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if (inMap(i, j) && getDistance(i, j, x, y) == 1 && isValidTarget(spell, i, j)) {
                            targets.add(new Pair(i, j));
                        }
                    }
                }

            case SELECTED_X_Y_GRID:
                for (int i = x; i < x + spell.getGridX(); i++) {
                    for (int j = y; i < y + spell.getGridY(); j++) {
                        if (inMap(i, j) && isValidTarget(spell, i, j)) {
                            targets.add(new Pair(i, j));
                        }
                    }
                }
                break;

            case SAME_ROW:
                for (int i = 0; i < map.getNumberOfColumns(); i++) {
                    if (isValidTarget(spell, x, i)) {
                        targets.add(new Pair(x, i));
                    }
                }
                break;

            case SAME_COLUMN:
                for (int i = 0; i < map.getNumberOfRows(); i++)
                    if (isValidTarget(spell, i, y)) {
                        targets.add(new Pair(x, i));
                    }
                break;

            case DISTANCE_2:
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if (inMap(i, j) && getDistance(i, j, x, y) == 2 && isValidTarget(spell, i, j)) {
                            targets.add(new Pair(i, j));
                        }
                    }
                }
                break;

            case SELECTED_CELL:
                if (isValidTarget(spell, x, y)) {
                    targets.add(new Pair(x, y));
                }
                break;
        }
        return ret;
    }

    private void checkOnSpawn(Unit unit, int x, int y) { // when inserting a unit card

        ArrayList<SpecialPowerType> types = unit.getSpecialPowerTypes();
        ArrayList<Spell> spells = unit.getSpecialPowers();

        int i = 0;
        for (Spell spell : spells) {
            if (types.get(i) == SpecialPowerType.ON_SPAWN) {
                castSpell(spell, x, y);
            }
        }
    }

    private void castSpellCard(SpellCard spellCard, int x, int y) {

    }

    // inserts card with name [cardName] from player's hand and puts it in cell ([x], [y])
    // if card is a spell card (x, y) is the target of the spell
    void insertCard(String cardName, int x, int y) {
        Player player = getCurrentPlayer();
        Card card = player.findCard(cardName);
        if (card == null) { // no such card is found in player's hand
            view.showInvalidCardError();
            return;
        }
        if (inMap(x, y)) {
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
        player.decreaseMana(card.getManaCost());
        grid[x][y].setContent(card, player); // finally put the card on cell ([x], [y])

        if (card instanceof Unit)
            checkOnSpawn((Unit) card, x, y);
        if (card instanceof SpellCard)
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
