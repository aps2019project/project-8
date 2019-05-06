// on defend moonnnnnndeeeeeeee

package model;

import menus.InGameMenu;

import java.util.ArrayList;
import java.util.Random;

public class Game extends InGameMenu {

    private static final int NUMBER_OF_PLAYERS = 2;
    private static final int[] HERO_INITIAL_ROW = {2, 2};
    private static final int[] HERO_INITIAL_COLUMN = {0, 8};

    private ArrayList<Item> currentItems = new ArrayList<>();
    private ArrayList<Integer> itemCastingTurns = new ArrayList<>();
    private int numberOfFlags;
    private int turn;
    private Map map = new Map();
    private Player[] players;
    private boolean hasAI;
    private Account[] accounts;
    private Unit selectedUnit;
    private Card selectedCard; // probably has no use
    private Collectible selectedCollectible;
    private GameType gameType;
    private int prize = 1000;

    public Game(Account firstPlayer, Account secondPlayer, GameType gameType, int numberOfFlags) {
        accounts = new Account[]{firstPlayer, secondPlayer};
        players = new Player[]{firstPlayer.getPlayer(), secondPlayer.getPlayer()};
        hasAI = false;
        this.gameType = gameType;
        this.numberOfFlags = numberOfFlags;
    }

    public Game(Account account, AI ai, GameType gameType, int numberOfFlags) {
        accounts = new Account[]{account, null};
        players = new Player[]{account.getPlayer(), ai.getPlayer()};
        hasAI = false;
        this.gameType = gameType;
        this.numberOfFlags = numberOfFlags;
    }

    private Player getCurrentPlayer() {
        if (turn % 2 == 0) {
            return players[0];
        } else {
            return players[1];
        }
    }

    Map getMap() {
        return this.map;
    }

    void moveSelectedUnit(int x, int y) {
        // can fly has got to do something in here
        if (Map.getDistance(selectedUnit.getX(), selectedUnit.getY(), x, y) <= 2) { // possibly we could add some moveRange to Unit class variables
            if (map.isPathEmpty(selectedUnit.getX(), selectedUnit.getY(), x, y)) {
                map.getGrid()[selectedUnit.getX()][selectedUnit.getY()].setContent(null);
                map.getGrid()[x][y].setContent(selectedUnit);
                selectedUnit.setX(x);
                selectedUnit.setY(y);
                view.logMessage(selectedUnit.getID() + " moved to " + x + " " + y);
                return;
            }
        }
        view.showInvalidTargetError();
    }

    private void rawAttack(Unit attacker, Unit defender) {
        int damage = Math.min(attacker.calculateAP() - defender.calculateHoly(), 0);
        defender.receiveDamage(damage);
    }

    private  boolean canAttack(Unit attacker, Unit defender) {
        int distance = Map.getDistance(attacker.getX(), attacker.getY(), defender.getX(), defender.getY());
        boolean isAdjacent = Map.isAdjacent(attacker.getX(), attacker.getY(), defender.getX(), defender.getY());
        boolean can = false;
        switch (attacker.getUnitType()) {
            case MELEE:
                can |= isAdjacent;
                break;
            case RANGED:
                can |= !isAdjacent && attacker.getAttackRange() >= distance;
                break;
            case HYBRID:
                can |= attacker.getAttackRange() >= distance;
                break;
        }
        return  can;
    }

    private void attackWithSpecialPowers(Unit attacker, Unit defender) {
        int i = 0;
        ArrayList<Spell> spells = attacker.getSpecialPowers();
        ArrayList<SpecialPowerType> types = attacker.getSpecialPowerTypes();
        for (Spell spell : spells) {
            if (types.get(i) == SpecialPowerType.ON_ATTACK) {
                castSpellOnCellUnit(spell, defender.getX(), defender.getY(), attacker.getPlayer());
            }
            i++;
        }
        rawAttack(attacker, defender);
    }

    private void attackUnitByUnit(Unit attacker, Unit defender) {
        attackWithSpecialPowers(attacker, defender);
        if (!defender.isDisarmed()) {
            rawAttack(defender, attacker);
        }
        attacker.setCanAttack(false);
    }

    public void attackTargetCardWithSelectedUnit(String targetCardID) {
        Unit targetUnit = findUnitInGridByID(targetCardID);
        if (targetUnit == null) { // invalid card id
            view.showInvalidCardIDError();
            return;
        }
        if (!selectedUnit.getCanAttack() || selectedUnit.isStunned()) { // has already attacked before or is stunned
            view.logMessage("Card with " + selectedUnit.getID() + " can't attack");
            return;
        }
        if (!canAttack(selectedUnit, targetUnit)) { // can't attack because
            view.logMessage("opponent minion is unavailable for attack");
            return;
        }
        attackUnitByUnit(targetUnit, selectedUnit);
    }

    public void attackCombo(String targetCardID, String[] friendlyCardsIDs) {

    }

    void useHeroSpecialPower(int destinationRow, int destinationColumn) {

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

    public void selectCard(String cardID) {
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

    public boolean checkUnitTypeInItemTarget(Unit unit, Item.Target.TargetUnitType targetUnitType) {
        switch (targetUnitType) {
            case ALL:
                return true;
            case MELEE:
                return unit.getUnitType() == UnitType.MELEE;
            case RANGED:
                return unit.getUnitType() == UnitType.RANGED;
            case HYBRID:
                return unit.getUnitType() == UnitType.HYBRID;
            case MELEE_HYBRID:
                return unit.getUnitType() == UnitType.MELEE || unit.getUnitType() == UnitType.HYBRID;
            case MELEE_RANGED:
                return unit.getUnitType() == UnitType.MELEE || unit.getUnitType() == UnitType.RANGED;
            case RANGED_HYBRID:
                return unit.getUnitType() == UnitType.RANGED || unit.getUnitType() == UnitType.HYBRID;
            default:
                return false;
        }
    }

    public boolean checkUnitTypeInSpellTarget(Unit unit, Spell.TargetUnitType targetUnitType) {
        switch (targetUnitType) {
            case ALL:
                return true;
            case MELEE:
                return unit.getUnitType() == UnitType.MELEE;
            case RANGED:
                return unit.getUnitType() == UnitType.RANGED;
            case HYBRID:
                return unit.getUnitType() == UnitType.HYBRID;
            case MELEE_HYBRID:
                return unit.getUnitType() == UnitType.MELEE || unit.getUnitType() == UnitType.HYBRID;
            case MELEE_RANGED:
                return unit.getUnitType() == UnitType.MELEE || unit.getUnitType() == UnitType.RANGED;
            case RANGED_HYBRID:
                return unit.getUnitType() == UnitType.RANGED || unit.getUnitType() == UnitType.HYBRID;
            default:
                return false;
        }
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

    private void castSpellOnCellUnit(Spell spell, int x, int y, Player player) {
        boolean valid = isValidTarget(spell, x, y);
        Unit unit = (Unit) map.getGrid()[x][y].getContent();
        if (valid) {

            if (spell.canDispel()) {
                unit.removeBuffs(player != unit.getPlayer());
                // if the the player casting the spell is the same one owning this unit
            }

            for (Buff buff : spell.getBuffs()) {
                unit.addBuff(new Buff(buff));
            }
        }
    }

    private void castSpellOnCell(Spell spell, int x, int y) {
        Cell cell = map.getGrid()[x][y];
        for (Buff buff : spell.getBuffs()) {
            cell.addEffect(new Buff(buff));
        }
    }

    /*
    private boolean isPositiveBuff(Buff buff) {
        if (buff.canDisarm() || buff.canStun()) {
            return false;
        }
        int sum = buff.getEffectAp() + buff.getEffectAp() + buff.getHoly() - buff.getPoison();
        return sum > 0;
    }*/

    private void castSpellOnCoordinate(Spell spell, int x, int y, Player player) {
        switch (spell.getTargetType()) {
            case UNIT:
                castSpellOnCellUnit(spell, x, y, player);
                break;
            case CELL:
                castSpellOnCell(spell, x, y);
                break;
        }
    }

    private void shuffle(ArrayList<Pair> targets) {
        final int swapCount = targets.size();
        Random rand = new Random();
        for (int count = 0; count < swapCount; count++) {
            int i = rand.nextInt(targets.size());
            int j = rand.nextInt(targets.size());
            Pair temp = targets.get(i);
            targets.set(i, targets.get(j));
            targets.set(j, temp);
        }
    }

    private ArrayList<Pair> getTargets(Spell spell, int x, int y) {
        Spell.TargetArea area = spell.getTargetArea();
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
                        if (inMap(i, j) && Map.getDistance(i, j, x, y) == 1 && isValidTarget(spell, i, j)) {
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
                        if (inMap(i, j) && Map.getDistance(i, j, x, y) == 2 && isValidTarget(spell, i, j)) {
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
        return targets;
    }

    // if spell is from a spell card (x, y) should be target point of spell
    // in this case for adjacent 8 and adjacent 9 the center cell must be given not the left most and upper most
    // for selected X_Y grid the upper most left most must be given
    // if spell is from a Unit special power (x, y) should be coordination of the unit itself

    // returns true if spell had

    private void castSpell(Spell spell, int x, int y, Player player) {
        ArrayList<Pair> targets = getTargets(spell, x, y);
        shuffle(targets); // here we handle random targets!
        for (int i = 0; i < Math.min(targets.size(), spell.getNumberOfRandomTargets()); i++) {
            Pair p = targets.get(i);
            castSpellOnCoordinate(spell, p.getX(), p.getY(), player);
        }
    }

    private void checkOnSpawn(Unit unit, int x, int y) { // when inserting a unit card

        ArrayList<SpecialPowerType> types = unit.getSpecialPowerTypes();
        ArrayList<Spell> spells = unit.getSpecialPowers();

        int i = 0;
        if (spells == null) {
            return;
        }
        for (Spell spell : spells) {
            if (types.get(i) == SpecialPowerType.ON_SPAWN) {
                castSpell(spell, x, y, unit.getPlayer());
            }
            i++;
        }
    }

    // returns true in case of success returns false otherwise
    private boolean castSpellCard(SpellCard spellCard, int x, int y) {
        if (spellCard.getName().equals("kingsGuard")) {
            for (int i = 0; i < map.getNumberOfRows(); i++)
                for (int j = 0; j < map.getNumberOfColumns(); j++) {
                    if (map.getGrid()[i][j].getContent() instanceof Hero) {
                        Hero hero = (Hero) map.getGrid()[i][j].getContent();
                        if (hero.getPlayer() != getCurrentPlayer()) {
                            if (x != i || y != j) {
                                view.showInvalidTargetError();
                                return false;
                            }
                        }
                    }
                }
        }
        Spell.TargetArea targetArea = spellCard.getSpell().getTargetArea();
        if (targetArea != Spell.TargetArea.SELECTED_CELL || isValidTarget(spellCard.getSpell(), x, y)) {
            castSpell(spellCard.getSpell(), x, y, getCurrentPlayer());
        } else {
            view.showInvalidTargetError();
            return false;
        }
        return true;
    }

    private boolean putUnitCard(Unit unit, int x, int y) {
        Cell[][] grid = map.getGrid();
        if (grid[x][y].getContent() != null) { // the cell is already not empty
            view.showInvalidTargetError();
            return false;
        }
        Player player = getCurrentPlayer();
        unit.setPlayer(player);
        grid[x][y].setContent(unit, player); // finally put the card on cell ([x], [y])
        checkOnSpawn(unit, x, y);
        unit.setX(x);
        unit.setY(y);
        return true;
    }

    // inserts card with name [cardName] from player's hand and puts it in cell ([x], [y])
    // if card is a spell card (x, y) is the target of the spell
    public void insertCard(String cardName, int x, int y) {
        Player player = getCurrentPlayer();
        Card card = player.findCardInHand(cardName);
        if (card == null) { // no such card is found in player's hand
            view.showInvalidCardError();
            return;
        }
        if (!inMap(x, y)) {
            view.showInvalidCoordinatesError();
            return;
        }
        if (player.getMana() < card.getManaCost()) { // player doesn't have enough mana
            view.showNotEnoughManaError();
            return;
        }
        boolean inserted = false;
        if (card instanceof Unit) {
            inserted = putUnitCard((Unit) card, x, y);
        }
        if (card instanceof SpellCard) {
            inserted = castSpellCard((SpellCard) card, x, y);
        }
        if (inserted) {
            view.logMessage(cardName + " with " + card.getID() + " inserted to " + "(" + x + "," + y + ")"); // log success message
            player.decreaseMana(card.getManaCost());
        }
    }

    void selectUnit(int row, int column) {

    }

    public void addSpecialPowerToUnit(Unit unit, Spell specialPower, SpecialPowerType specialPowerType, Item.Target.TargetUnitType targetUnitType) {
        if (checkUnitTypeInItemTarget(unit, targetUnitType)) {
            unit.getSpecialPowers().add(specialPower);
            unit.getSpecialPowerTypes().add(specialPowerType);
        }
    }

    public void castItem(Item item, Player player, int r, int c, int startTime) {
        switch (item.getItemType()) {
            case ADD_MANA:
                if (item.getAddManaDuration() > turn - startTime) {
                    player.addMana(item.getAddMana());
                }
                currentItems.add(item);
                itemCastingTurns.add(turn);
                break;
            case ADD_A_SPECIAL_POWER:
                for (int i = 0; i < item.getSpecialPower().size(); i++) {
                    Spell specialPower = item.getSpecialPower().get(i);
                    SpecialPowerType specialPowerType = item.getSpecialPowerType().get(i);
                    Item.Target target = item.getSpecialPowerTarget().get(i);
                    // Here I add special power to units in map:
                    {
                        for (int row = 0; row < getMap().getNumberOfRows(); row++)
                            for (int column = 0; column < getMap().getNumberOfColumns(); column++) {
                                Cell cell = getMap().getCell(row, column);
                                // check has content and is friendly
                                if (!cell.hasContent() || !(cell.getContent() instanceof Unit) || ((Unit) cell.getContent()).getPlayer() != player) {
                                    continue;
                                }
                                Unit unit = (Unit) cell.getContent();
                                addSpecialPowerToUnit(unit, specialPower, specialPowerType, target.getTargetUnitType());
                            }
                    }
                    // Here I add special power to units in Hand:
                    {
                        for (Card card : player.getHand().getCards()) {
                            if (card instanceof Unit) {
                                addSpecialPowerToUnit((Unit) card, specialPower, specialPowerType, target.getTargetUnitType());
                            }
                        }
                    }
                    // Here I add special power to units in deck
                    {
                        for (Card card : player.getDeck().getCards()) {
                            if (card instanceof Unit) {
                                addSpecialPowerToUnit((Unit) card, specialPower, specialPowerType, target.getTargetUnitType());
                            }
                        }
                    }
                }
                break;
            case CAST_A_SPELL:
                castSpell(item.getSpell(), r, c, player);
                break;
        }
    }

    public void initiateGame() {
        putUnitCard(players[0].getHero(), 2, 0);
        putUnitCard(players[1].getHero(), 2, 8);
        for (int i = 0; i < 2; i++) {
            if (players[i].getUsable() != null) {
                players[i].initiateHand();
                Usable usable = new Usable(players[i].getUsable());
                castItem(usable, players[i], 0, 0, 0);
            }
        }
    }

    public void initiateTurn() {
        // mana processes
        getCurrentPlayer().setMana((turn + 1) / 2 + 2);
        // item processes
        for (int i = 0; i < currentItems.size(); i++) {
            Item item = currentItems.get(i);
            int startTime = itemCastingTurns.get(i);
            castItem(item, getCurrentPlayer(), 0, 0, startTime);
        }
    }

    public void endTurn() {
        // lower the duration of buffs
        for (int i = 0; i < getMap().getNumberOfRows(); i++)
            for (int j = 0; j < getMap().getNumberOfColumns(); j++) {
                Cell cell = getMap().getCell(i, j);
                if (!cell.hasContent())
                    continue;
                Card card = (Card) cell.getContent();
                if (card instanceof Unit) {
                    Unit unit = (Unit) card;
                    for (Buff buff : unit.getBuffs()) {
                        buff.decrementDuration();
                    }
                }
            }
    }

    public void selectCollectibleItem(String collectibleName) {

    }

    public void applyCollectible(int row, int column) {
        castItem(selectedCollectible, getCurrentPlayer(), row, column, turn);
    }

    public void showNextCardInDeck() {
        view.showCardInfo(getCurrentPlayer().getDeck().getNextCard());
    }

    public void showHand() {
        view.showHand(getCurrentPlayer().getHand());
    }

    void showGraveYardInfoOfACard(String cardName) {
        view.showCardInfo(getCurrentPlayer().findCardInGraveyard(cardName));
    }

    void showGraveYardCards() {
        view.showGraveyard(getCurrentPlayer().getGraveYard());
    }

    public void showGameInfo() {

    }

    public void showOpponentMinions() {
    }

    public void showMyMinions() {
    }

    public void showCardInfo(String cardID) {
    }

    public void showAllCollectibles() {
    }

    public void showCollectibleInfo() {
    }

    public void showAvailableOptions() {
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public enum GameState {
        WIN_FIRST_PLAYER,
        DRAW,
        WIN_SECOND_PLAYER
    }

    class Pair {
        int x;
        int y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int getX() {
            return x;
        }

        int getY() {
            return y;
        }
    }
}
