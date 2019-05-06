// on defend moonnnnnndeeeeeeee

package model;

import menus.InGameMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game extends InGameMenu {

    private static final int NUMBER_OF_PLAYERS = 2;
    private static final int[] HERO_INITIAL_ROW = {2, 2};
    private static final int[] HERO_INITIAL_COLUMN = {0, 8};
    private static final int NUMBER_OF_FLAG_TURNS = 6;

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
    private ArrayList<HashMap<String, Integer>> numberOfPlayedCollectionItems = new ArrayList<>(2);

    public Game(Account firstPlayer, Account secondPlayer, GameType gameType, int numberOfFlags) {
        accounts = new Account[]{firstPlayer, secondPlayer};
        players = new Player[]{firstPlayer.getPlayer().setName(firstPlayer.getName()), secondPlayer.getPlayer().
                setName(secondPlayer.getName())};
        hasAI = false;
        this.gameType = gameType;
        this.numberOfFlags = numberOfFlags;
    }

    public Game(Account account, AI ai, GameType gameType, int numberOfFlags) {
        accounts = new Account[]{account, null};
        players = new Player[]{account.getPlayer().setName(account.getName()), ai.getPlayer().setName("COM")};
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

    private Map getMap() {
        return this.map;
    }

    private int getDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private boolean isAdjacent(int x1, int y1, int x2, int y2) {
        return getDistance(x1, y1, x2, y2) == 1;
    }

    private boolean isPathEmpty(int srcX, int srcY, int desX, int desY, Player player) {
        Cell[][] grid = map.getGrid();
        if (grid[desX][desY].getContent() != null && grid[desX][desY].getContent() instanceof Unit)
            return false;
        int distance = getDistance(srcX, srcY, desX, desY);
        if (distance == 1) {
            return true;
        }
        int dx = desX - srcX;
        int dy = desY - srcY;
        if (dx * dy != 0) {
            if (grid[srcX + dx][srcY].getContent() == null || grid[srcX + dx][srcY].getContent() instanceof Collectible
                    || grid[srcX][srcY].getObjectOwner() == player)
                return true;
            return grid[srcX][srcY + dy].getContent() == null || grid[srcX][srcY + dy].getObjectOwner() == player ||
                    grid[srcX + dx][srcY].getContent() instanceof Collectible;
        } else {
            dx /= 2;
            dy /= 2;
            return grid[srcX + dx][srcY + dy].getContent() == null || grid[srcX][srcY].getObjectOwner() == player ||
                    grid[srcX + dx][srcY].getContent() instanceof Collectible;
        }
    }

    public void moveSelectedUnit(int x, int y) {
        // can fly has got to do something in here
        if (getDistance(selectedUnit.getX(), selectedUnit.getY(), x, y) <= 2 && selectedUnit.getCanMove()) { // possibly we could add some moveRange to Unit class variables
            if (isPathEmpty(selectedUnit.getX(), selectedUnit.getY(), x, y, getCurrentPlayer())) {
                Cell currentCell = map.getGrid()[selectedUnit.getX()][selectedUnit.getY()];
                if (currentCell.getContent() != null && currentCell.getContent() instanceof Collectible) {
                    Collectible collectible = (Collectible) currentCell.getContent();
                    collectible.setCollectionItemID(getNewID(collectible));
                    getCurrentPlayer().addCollectible(collectible);
                }
                currentCell.setContent(null);
                map.getGrid()[x][y].setContent(selectedUnit);
                selectedUnit.setX(x);
                selectedUnit.setY(y);
                view.logMessage(selectedUnit.getID() + " moved to " + x + " " + y);
                selectedUnit.setCanMove(false);
                return;
            }
        }
        view.showInvalidTargetError();
    }



    private void checkOnDeath(Unit defender) {

    }




    // no special powers included
    private void rawAttack(Unit attacker, Unit defender) {
        int damage = Math.min(attacker.calculateAP() - defender.calculateHoly(), 0);
        defender.receiveDamage(damage);
    }

    // returns -1 if attacker couldn't attack and -2 if defender wasn't in range and 0 otherwise
    private int canAttack(Unit attacker, Unit defender) {
        if (!attacker.getCanAttack() || attacker.isStunned())
            return -1;
        int distance = getDistance(attacker.getX(), attacker.getY(), defender.getX(), defender.getY());
        boolean isAdjacent = isAdjacent(attacker.getX(), attacker.getY(), defender.getX(), defender.getY());
        boolean can = false;
        switch (attacker.getUnitType()) {
            case MELEE:
                can = isAdjacent;
                break;
            case RANGED:
                can = !isAdjacent && attacker.getAttackRange() >= distance;
                break;
            case HYBRID:
                can = attacker.getAttackRange() >= distance;
                break;
        }
        return can ? 0 : -2;
    }


    // special powers are included but defender doesn't counter attack
    private void oneSidedAttack(Unit attacker, Unit defender) {
        int i = 0;
        ArrayList<Spell> spells = attacker.getSpecialPowers();
        ArrayList<SpecialPowerType> types = attacker.getSpecialPowerTypes();
        for (Spell spell : spells) {
            if (types.get(i) == SpecialPowerType.ON_ATTACK || types.get(i) == SpecialPowerType.ON_USE) {
                castSpellOnCellUnit(spell, map.getGrid()[defender.getX()][defender.getY()], attacker.getPlayer());
            }
            i++;
        }
        rawAttack(attacker, defender);
    }

    // special powers are included
    // it's assumed that attacker can attack defender (hasn't attacked before and defender is attack range)
    private void twoSidedAttack(Unit attacker, Unit defender) {
        oneSidedAttack(attacker, defender);
        // no ON_DEFEND for now!
        if (defender.isDisarmed())
            rawAttack(defender, attacker);
    }


    // returns -1 in case when attacker can't attack
    // returns -2 if defender is not in attack range of attacker
    // returns 0 for success
    // if oneSided is true defender doesn't counter attack (for combo attacks)

    private int attackUnitByUnit(Unit attacker, Unit defender, boolean oneSided) {
        int state = canAttack(attacker, defender);
        if (state != 0) {
            return state;
        }
        if (!oneSided) {
            twoSidedAttack(attacker, defender);
        } else {
            oneSidedAttack(attacker, defender);
        }
        attacker.setCanAttack(false);
        attacker.setCanMove(false);

        if (!defender.isDead() && defender.calculateHP() < 0 && defender.getX() != -1) {
            Cell cell = map.getGrid()[defender.getX()][defender.getY()];
            cell.setObjectOwner(null);
            cell.setContent(null);
            defender.setDead(true);
            checkOnDeath(defender);
        }

        return 0;
    }

    public void attackTargetCardWithSelectedUnit(String targetCardID) {
        Unit targetUnit = findUnitInGridByID(targetCardID);
        if (targetUnit == null) { // invalid card id
            view.showInvalidCardIDError();
            return;
        }
        int state = attackUnitByUnit(selectedUnit, targetUnit, false);
        if (state == -1) { // has already attacked before or is stunned
            view.logMessage("Card with " + selectedUnit.getID() + " can't attack");
            return;
        }
        if (state == -2) { // can't attack because
            view.logMessage("opponent minion is unavailable for attack");
        }
    }

    public void attackCombo(String targetCardID, String[] friendlyCardsIDs) {
        Unit defender = findUnitInGridByID(targetCardID);
        if (defender == null) {
            view.showInvalidCardIDError();
            return;
        }
        for (String friendlyCardsID : friendlyCardsIDs) {
            Unit attacker = findUnitInGridByID(friendlyCardsID);
            if (attacker == null) {
                view.showInvalidCardIDError();
                return;
            }
            int state = canAttack(attacker, defender);
            if (state == -1) {
                view.logMessage("Card with " + selectedUnit.getID() + " can't attack");
                return;
            } else if (state == -2) {
                view.logMessage("opponent minion is unavailable for attack");
                return;
            }
        }

        for (int i = 0; i < friendlyCardsIDs.length; i++) {
            Unit attacker = findUnitInGridByID(friendlyCardsIDs[i]);
            attackUnitByUnit(attacker, defender, i != 0);
        }
    }

    // x, y must be valid
    public void useHeroSpecialPower(int x, int y) {
        Hero hero = getCurrentPlayer().getHero();
        Player player = getCurrentPlayer();
        if (hero.getSpecialPowers() == null) {
            return;
        }
        if (player.getMana() < hero.getManaCost()) {
            view.showNotEnoughManaError();
            return;
        }
        if (hero.getRemainingCooldown() != 0) {
            view.showCooldownError();
            return;
        }
        hero.resetRemainingCooldown();
        for (Spell spell : hero.getSpecialPowers()) {
            castSpell(spell, x, y, getCurrentPlayer());
        }
    }

    private void endGame() {

    }

    public GameState getGameState() {
        switch (gameType) {
            case COLLECT_THE_FLAGS:
                if (players[0].getNumberOfFlags() > numberOfFlags / 2)
                    return GameState.WIN_FIRST_PLAYER;
                if (players[1].getNumberOfFlags() > numberOfFlags / 2)
                    return GameState.WIN_SECOND_PLAYER;
            case HOLD_THE_FLAG:
                if (players[0].getNumberOfFlagTurns() >= NUMBER_OF_FLAG_TURNS)
                    return GameState.WIN_FIRST_PLAYER;
                if (players[1].getNumberOfFlagTurns() >= NUMBER_OF_FLAG_TURNS)
                    return GameState.WIN_SECOND_PLAYER;
            default:
                if (players[0].getHero().calculateHP() <= 0)
                    return GameState.WIN_SECOND_PLAYER;
                if (players[1].getHero().calculateHP() <= 0)
                    return GameState.WIN_FIRST_PLAYER;
        }
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
        if (unit == null || unit.getPlayer() != getCurrentPlayer()) {
            view.showInvalidCardIDError();
            return;
        }
        view.alertUnitSelection(cardID);
        selectedUnit = unit;
    }

    private boolean inMap(int x, int y) {
        return x >= 0 && x < map.getNumberOfRows() && y >= 0 && y < map.getNumberOfColumns();
    }

    private boolean checkUnitTypeInItemTarget(Unit unit, Item.Target.TargetUnitType targetUnitType) {
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

    private boolean isValidTarget(Spell spell, Cell cell, Player player) {
        if (spell.getTargetType() == Spell.TargetType.CELL) {
            return true;
        }
        if (!(cell.getContent() instanceof Unit)) { // hamid
            return false;
        }

        Unit unit = (Unit) cell.getContent();
        if (unit == null) {
            return false;
        }
        if (!checkUnitTypeInSpellTarget(unit, spell.getTargetUnitType())) {
            return false;
        }
        boolean isForCastingPlayer = cell.getObjectOwner() == player;
        boolean isMinion = cell.getContent() instanceof Minion;
        boolean isHero = cell.getContent() instanceof Hero;
        boolean valid = false;
        switch (spell.getTargetUnit()) {
            case FRIENDLY_UNIT:
                valid = isForCastingPlayer;
                break;
            case FRIENDLY_HERO:
                valid = isForCastingPlayer && isHero;
                break;
            case FRIENDLY_MINION:
                valid = isForCastingPlayer && isMinion;
                break;
            case ENEMY_UNIT:
                valid = !isForCastingPlayer;
                break;
            case ENEMY_HERO:
                valid = !isForCastingPlayer && isHero;
                break;
            case ENEMY_MINION:
                valid = !isForCastingPlayer && isMinion;
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

    private void castSpellOnCellUnit(Spell spell, Cell cell, Player player) {
        boolean valid = isValidTarget(spell, cell, player);
        Unit unit = (Unit) cell.getContent();
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

    private void castSpellOnCell(Spell spell, Cell cell) {
        for (Buff buff : spell.getBuffs()) {
            cell.addEffect(new Buff(buff));
        }
    }

    private void castSpellOnCoordinate(Spell spell, Cell cell, Player player) {
        switch (spell.getTargetType()) {
            case UNIT:
                castSpellOnCellUnit(spell, cell, player);
                break;
            case CELL:
                castSpellOnCell(spell, cell);
                break;
        }
    }

    private void shuffle(ArrayList<Cell> targets) {
        final int swapCount = targets.size();
        Random rand = new Random();
        for (int count = 0; count < swapCount; count++) {
            int i = rand.nextInt(targets.size());
            int j = rand.nextInt(targets.size());
            Cell temp = targets.get(i);
            targets.set(i, targets.get(j));
            targets.set(j, temp);
        }
    }

    private ArrayList<Cell> getTargets(Spell spell, int x, int y, Player player) {
        ArrayList<Cell> targets = new ArrayList<>(0);
        switch (spell.getTargetArea()) {
            case ALL_OF_THE_MAP:
                for (int i = 0; i < map.getNumberOfRows(); i++) {
                    for (int j = 0; j < map.getNumberOfColumns(); j++) {
                        Cell cell = map.getGrid()[i][j];
                        if (isValidTarget(spell, cell, player)) {
                            targets.add(cell);
                        }
                    }
                }
                break;

            case ADJACENT_9:
                Cell cell = map.getGrid()[x][y];
                if (isValidTarget(spell, cell, player)) {
                    targets.add(cell);
                }

            case ADJACENT_8:
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        cell = map.getGrid()[i][j];
                        if (inMap(i, j) && isValidTarget(spell, cell, player)) {
                            targets.add(cell);
                        }
                    }
                }
                break;

            case ADJACENT_4:
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        cell = map.getGrid()[i][j];
                        if (inMap(i, j) && getDistance(i, j, x, y) == 1 && isValidTarget(spell, cell, player)) {
                            targets.add(cell);
                        }
                    }
                }
                break;

            case SELECTED_X_Y_GRID:
                for (int i = x; i < x + spell.getGridX(); i++) {
                    for (int j = y; i < y + spell.getGridY(); j++) {
                        cell = map.getGrid()[i][j];
                        if (inMap(i, j) && isValidTarget(spell, cell, player)) {
                            targets.add(cell);
                        }
                    }
                }
                break;

            case SAME_ROW:
                for (int i = 0; i < map.getNumberOfColumns(); i++) {
                    cell = map.getGrid()[x][i];
                    if (isValidTarget(spell, cell, player)) {
                        targets.add(cell);
                    }
                }
                break;

            case SAME_COLUMN:
                for (int i = 0; i < map.getNumberOfRows(); i++) {
                    cell = map.getGrid()[i][y];
                    if (isValidTarget(spell, cell, player)) {
                        targets.add(cell);
                    }
                }
                break;

            case DISTANCE_2:
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        cell = map.getGrid()[i][j];
                        if (inMap(i, j) && getDistance(i, j, x, y) <= 2 && isValidTarget(spell, cell, player)) {
                            targets.add(cell);
                        }
                    }
                }
                break;

            case SELECTED_CELL:
                cell = map.getGrid()[x][y];
                if (isValidTarget(spell, cell, player)) {
                    targets.add(cell);
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
        ArrayList<Cell> targets = getTargets(spell, x, y, player);
        shuffle(targets); // here we handle random targets!
        for (int i = 0; i < Math.min(targets.size(), spell.getNumberOfRandomTargets()); i++) {
            castSpellOnCoordinate(spell, targets.get(i), player);
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

    // to be added :)
//    private boolean checkInvalidTarget(Spell spell, int x, int y) {
//        if (spell.getTargetType() == Spell.TargetType.CELL) {
//            return true;
//        }
//        return true;
//    }

    // returns true in case of success returns false otherwise
    private boolean castSpellCard(SpellCard spellCard, int x, int y, Player player) {
        if (spellCard.getName().equals("kingsGuard")) {
            Hero hero = getCurrentPlayer().getHero();


            if (hero == null) { // redundant
                // must throw an exception
                return false;
            }

            if (x != hero.getX() || y != hero.getY()) {
                view.showInvalidCardIDError();
                return false;
            }
        }
        Spell.TargetArea targetArea = spellCard.getSpell().getTargetArea();
        if (targetArea != Spell.TargetArea.SELECTED_CELL || isValidTarget(spellCard.getSpell(), map.getGrid()[x][y], getCurrentPlayer())) {
            castSpell(spellCard.getSpell(), x, y, player);
        } else {
            view.showInvalidTargetError();
            return false;
        }
        spellCard.setCollectionItemID(getNewID(spellCard));
        getCurrentPlayer().addToGraveyard(spellCard);
        return true;
    }

    private boolean putUnitCard(Unit unit, int x, int y) {
        Cell[][] grid = map.getGrid();
        if (grid[x][y].getContent() != null && grid[x][y].getContent() instanceof Unit) {
            // the cell is already not empty
            view.showInvalidTargetError();
            return false;
        }
        Player player = getCurrentPlayer();
        unit.setPlayer(player);
        grid[x][y].setContent(unit, player); // finally put the card on cell ([x], [y])
        checkOnSpawn(unit, x, y);
        unit.setX(x);
        unit.setY(y);
        unit.setCollectionItemID(getNewID(unit));
        player.addUnit(unit);
        return true;
    }

    private String getNewID(CollectionItem collectionItem) {
        int currentTurn = turn % 2;
        if (numberOfPlayedCollectionItems.get(currentTurn).containsKey(collectionItem.getName())) {
            int currentNumber = numberOfPlayedCollectionItems.get(currentTurn).get(collectionItem.getName());
            numberOfPlayedCollectionItems.get(currentTurn).replace(collectionItem.getName(), currentNumber + 1);
            return getCurrentPlayer().getName() + "_" + collectionItem.getName() + "_" + (currentNumber + 1);
        }
        numberOfPlayedCollectionItems.get(currentTurn).put(collectionItem.getName(), 1);
        return getCurrentPlayer().getName() + "_" + collectionItem.getName() + "_" + 1;
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
            inserted = castSpellCard((SpellCard) card, x, y, getCurrentPlayer());
        }
        if (inserted) {
            view.logMessage(cardName + " with " + card.getID() + " inserted to " + "(" + (x + 1) + "," + (y + 1) + ")");
            // log success message
            player.decreaseMana(card.getManaCost());
            player.getHand().getCards().remove(card);
        }
    }

    public boolean hasUnit(String unitID) {
        return getCurrentPlayer().getUnit(unitID) != null;
    }

    public boolean hasCollectible(String unitID) {
        return getCurrentPlayer().getCollectible(unitID) != null;
    }

    private void addSpecialPowerToUnit(Unit unit, Spell specialPower, SpecialPowerType specialPowerType, Item.Target.
            TargetUnitType targetUnitType) {
        if (checkUnitTypeInItemTarget(unit, targetUnitType)) {
            unit.getSpecialPowers().add(specialPower);
            unit.getSpecialPowerTypes().add(specialPowerType);
        }
    }

    private void castItem(Item item, Player player, int r, int c, int startTime) {
        switch (item.getItemType()) {
            case ADD_MANA:
                if (item.getAddManaDuration() > turn - startTime) {
                    player.addMana(item.getAddMana());
                }
//                currentItems.add(item);
                itemCastingTurns.add(turn);
                break;
            case ADD_A_SPECIAL_POWER:
                for (int i = 0; i < item.getSpecialPowers().size(); i++) {
                    Spell specialPower = item.getSpecialPowers().get(i);
                    SpecialPowerType specialPowerType = item.getSpecialPowerTypes().get(i);

//                    System.err.println(item.getSpecialPowerTargets().size());

                    Item.Target target = item.getSpecialPowerTargets().get(i);
                    // Here I add special power to units in map:
                    {
                        for (int row = 0; row < getMap().getNumberOfRows(); row++)
                            for (int column = 0; column < getMap().getNumberOfColumns(); column++) {
                                Cell cell = getMap().getCell(row, column);
                                // check has content and is friendly
                                if (!cell.hasContent() || !(cell.getContent() instanceof Unit) || ((Unit)
                                        cell.getContent()).getPlayer() != player) {
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
                                addSpecialPowerToUnit((Unit) card, specialPower, specialPowerType, target.
                                        getTargetUnitType());
                            }
                        }
                    }
                    // Here      I add special power to units in deck
                    {
                        for (Card card : player.getDeck().getCards()) {
                            if (card instanceof Unit) {
                                addSpecialPowerToUnit((Unit) card, specialPower, specialPowerType, target.
                                        getTargetUnitType());
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
        numberOfPlayedCollectionItems.add(new HashMap<>());
        numberOfPlayedCollectionItems.add(new HashMap<>());

        turn = 0;
        putUnitCard(players[0].getHero(), 2, 0);
        turn = 1;
        putUnitCard(players[1].getHero(), 2, 8);
        turn = 0;
        for (int i = 0; i < 2; i++) {
            players[i].initiateHand();
            if (players[i].getUsable() != null) {
                Usable usable = new Usable(players[i].getUsable());
                castItem(usable, players[i], 0, 0, 0);
            }
        }

        // initiate next turn
        initiateTurn();
    }

    public void initiateTurn() {
        // mana processes
        getCurrentPlayer().setMana((turn + 1) / 2 + 2);

        // item processes
        for (int i = 0; i < currentItems.size(); i++) {

            System.err.println(i + 1);
            System.err.println(currentItems.get(i));

            Item item = currentItems.get(i);
            int startTime = itemCastingTurns.get(i);
            castItem(item, getCurrentPlayer(), 0, 0, startTime);
        }

        // passives
        ArrayList<Unit> allUnits = new ArrayList<>(players[0].getUnits());
        allUnits.addAll(players[1].getUnits());
        for (Unit unit: getCurrentPlayer().getUnits()) {
            for (int i = 0; i < unit.getSpecialPowers().size(); i++) {
                Spell spell = unit.getSpecialPowers().get(i);
                SpecialPowerType specialPowerType = unit.getSpecialPowerTypes().get(i);
                if (specialPowerType == SpecialPowerType.PASSIVE) {
                    castSpell(spell, unit.getX(), unit.getY(), unit.getPlayer());
                }
            }
        }
    }

    public void endTurn() {
        // lower the duration of buffs and remove buff
        for (int i = 0; i < getMap().getNumberOfRows(); i++)
            for (int j = 0; j < getMap().getNumberOfColumns(); j++) {
                Cell cell = getMap().getCell(i, j);
                if (!cell.hasContent())
                    continue;
                Card card = (Card) cell.getContent();
                if (card instanceof Unit) {
                    Unit unit = (Unit) card;
                    for (int t = unit.getBuffs().size() - 1; t >= 0; t--) {
                        Buff buff = unit.getBuffs().get(t);
                        buff.decrementDuration();

                        unit.receiveDamage(buff.getPoison());

                        if (buff.getDuration() <= 0) {
                            unit.getBuffs().remove(buff);
                        }
                    }
                }
            }
        // can move and can attack for all units
        for (int row = 0; row < getMap().getNumberOfRows(); row++)
            for (int column = 0; column < getMap().getNumberOfColumns(); column++) {
                Cell cell = getMap().getCell(row, column);
                if (cell.getContent() != null && cell.getContent() instanceof  Unit) {
                    Unit unit = (Unit) cell.getContent();
                    unit.setCanAttack(true);
                    unit.setCanMove(true);
                }
            }

        // cool down here
        getCurrentPlayer().getHero().decreaseRemainingCooldown();

        // add turn
        turn++;

        // initiate next turn
        initiateTurn();
    }

    public Collectible selectCollectible(String collectibleID) {
        view.alertCollectibleSelection(collectibleID);
        return getCurrentPlayer().getCollectible(collectibleID);
    }

    public void applyCollectible(int row, int column) {
        castItem(selectedCollectible, getCurrentPlayer(), row, column, turn);
    }

    public void showNextCardInDeck() {
        view.showCardInfo(getCurrentPlayer().getDeck().getNextCard());
    }

    public void showHand() {

        //
        System.err.println(getCurrentPlayer().getMana());
        //

        view.showHand(getCurrentPlayer().getHand());
    }

    public void showGraveYardInfoOfACard(String cardID) {
        view.showCardInfo(getCurrentPlayer().findCardInGraveyard(cardID));
    }

    public void showGraveYardCards() {
        view.showGraveyard(getCurrentPlayer().getGraveYard());
    }

    public void showGameInfo() {
        switch (gameType) {
            case KILL_OPPONENT_HERO:
                view.showGameInfoKillOponentHero(players[0].getHero(), players[1].getHero());
                break;
            case HOLD_THE_FLAG:
                boolean successfull = false;
                for (int row = 0; row < getMap().getNumberOfRows(); row++)
                    for (int column = 0; column < getMap().getNumberOfColumns(); column++) {
                        Cell cell = getMap().getCell(row, column);
                        if (cell.hasContent() && cell.getContent() instanceof Unit && ((Unit) cell.getContent()).
                                getNumberOfFlags() > 0) {
                            view.showGameInfoHoldTheFlag(row, column, (Unit) cell.getContent());
                            successfull = true;
                            break;
                        }
                    }
                if (!successfull) {
                    view.showGameInfoHoldTheFlag(-1, -1, null);
                }
                break;
            case COLLECT_THE_FLAGS:
                ArrayList<Unit> units = new ArrayList<>();
                for (int row = 0; row < getMap().getNumberOfRows(); row++) {
                    for (int column = 0; column < getMap().getNumberOfColumns(); column++) {
                        Cell cell = getMap().getCell(row, column);
                        if (cell.hasContent() && cell.getContent() instanceof Unit) {
                            Unit unit = (Unit) cell.getContent();
                            if (unit.getNumberOfFlags() > 0) {
                                units.add(new Unit(unit));
                            }
                        }
                    }
                }
                view.infoShowNumberOfFlags(units);
                break;
            default:
                break;
        }
    }

    public void showOpponentMinions() {
        for (Player player : players)
            if (player != getCurrentPlayer()) {
                player.getUnits().forEach(view::showUnit);
            }
    }

    public void showMyMinions() {
        getCurrentPlayer().getUnits().forEach(view::showUnit);
    }

    public void showCardInfo(String cardID) {
        Unit unit = findUnitInGridByID(cardID);
        if (unit == null) {
            view.showNoSuchUnitFoundError();
            return;
        }
        view.showUnitInfo(unit);
    }

    public void showAllCollectibles() {
        view.showCollectibles(getCurrentPlayer().getCollectibles());
    }

    public void showCollectibleInfo() {
        view.showCollectible(selectedCollectible);
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