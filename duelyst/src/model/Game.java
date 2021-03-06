package model;

import com.gilecode.yagson.YaGson;
import menus.InGameMenu;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Game extends InGameMenu {

    private static final int[] HERO_INITIAL_ROW = {2, 2};
    private static final int[] HERO_INITIAL_COLUMN = {0, 8};
    private static final int NUMBER_OF_FLAG_TURNS = 6;
    private static final int NUMBER_OF_COLLECTIBLES = 10;
    private static final String FRIENDLY = "friendly";
    private static final String ENEMY = "enemy";
    private static final String NEITHER = "neither";

    private HashMap<Player, ArrayList<Item>> currentItems = new HashMap<>();
    private HashMap<Player, HashMap<Item, Integer>> itemCastingTurns = new HashMap<>();
    private int numberOfFlags;
    private int turn;
    private Map map = new Map();
    private Player[] players;
    private boolean[] hasAI = new boolean[2];
    private AccountUser[] accounts;
    private Unit selectedUnit;
    private Collectible selectedCollectible;
    private GameType gameType;
    private int prize = 1000;
    private ArrayList<HashMap<String, Integer>> numberOfPlayedCollectionItems = new ArrayList<>(2);
    private AI ai;
    private ArrayList<Collectible> collectibles = new ArrayList<>();
    private ArrayList<Unit> dead = new ArrayList<>();

    public Game(AccountUser firstPlayer, AccountUser secondPlayer, GameType gameType, int numberOfFlags) {
        accounts = new AccountUser[]{firstPlayer, secondPlayer};
        players = new Player[]{firstPlayer.getData().getPlayer().setName(firstPlayer.getName()), secondPlayer.getData().getPlayer().
                setName(secondPlayer.getName())};
        hasAI[0] = hasAI[1] = false;
        this.gameType = gameType;
        this.numberOfFlags = numberOfFlags;
    }

    public Game(AccountUser account, AI ai, GameType gameType, int numberOfFlags) {
        this.ai = ai;
        accounts = new AccountUser[]{account, null};
        players = new Player[]{account.getData().getPlayer().setName(account.getName()), ai.getPlayer().setName("COM")};
        hasAI[0] = false;
        hasAI[1] = true;
        this.gameType = gameType;
        this.numberOfFlags = numberOfFlags;
    }

    Player getFirstPlayer() {
        return players[0];
    }

    Player getSecondPlayer() {
        return players[1];
    }

    public Player getCurrentPlayer() {
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

    private boolean isAdjacent(int x1, int y1, int x2, int y2) {
        return getDistance(x1, y1, x2, y2) == 1;
    }

    private boolean isPathEmpty(int srcX, int srcY, int desX, int desY, Player player) {
        Cell[][] grid = map.getGrid();
        if (grid[desX][desY].getContent() instanceof Unit)
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

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public boolean moveSelectedUnit(int x, int y) { // returns true if successful
        // can fly has got to do something in here
        if (selectedUnit == null) {
            if (!hasAI[turn % 2])
                view.showNoUnitSelectedError();
            return false;
        }
        if (!selectedUnit.getCanMove()) {
            if (!hasAI[turn % 2])
                view.showUnableToMoveError();
            return false;
        }
        if (getDistance(selectedUnit.getX(), selectedUnit.getY(), x, y) <= 2) { // possibly we could add some moveRange to Unit class variables
            if (isPathEmpty(selectedUnit.getX(), selectedUnit.getY(), x, y, getCurrentPlayer())) {
                Cell currentCell = map.getGrid()[selectedUnit.getX()][selectedUnit.getY()];
                Cell destinationCell = map.getGrid()[x][y];
                if (destinationCell.getContent() instanceof Collectible) {
                    Collectible collectible = (Collectible) destinationCell.getContent();
                    collectible.setCollectionItemID(getNewID(collectible));
                    getCurrentPlayer().addCollectible(collectible);
                    if (collectible.getItemType() == ItemType.ADD_MANA) {
                        currentItems.get(getCurrentPlayer()).add(collectible);
                        itemCastingTurns.get(getCurrentPlayer()).put(collectible, turn);
                    }
                }
                selectedUnit.addFlags(destinationCell.getNumberOfFlags());
                map.getCell(x, y).decreaseNumberOfFlags();
                currentCell.setContent(null);
                map.getGrid()[x][y].setContent(selectedUnit);

                if (!hasAI[turn % 2])
                    view.logMessage(selectedUnit.getID() + " moved to " + (x + 1) + " " + (y + 1));
                else view.logMessage(selectedUnit.getID() + " moved from " +  (selectedUnit.getX() + 1) + " " + (selectedUnit.getY() + 1) + " to " + (x + 1) + " " + (y + 1));
                selectedUnit.setX(x);
                selectedUnit.setY(y);

                selectedUnit.setCanMove(false);
                return true;
            } else {
                if (!hasAI[turn % 2])
                    view.showPathBlockedError();
                return true;
            }
        }
        if (!hasAI[turn % 2])
            view.showTargetOutOfRangeError();
        return false;
    }


    private void checkOnDeath(Unit unit) {
        if (unit.isDead()) {
            return;
        }
        Cell cell = map.getGrid()[unit.getX()][unit.getY()];
        cell.setObjectOwner(null);
        cell.setContent(null);
        unit.setDead(true);
        int i = 0;
        for (Spell spell : unit.getSpecialPowers()) {
            if (unit.getSpecialPowerTypes().get(i) == SpecialPowerType.ON_DEATH) {
                castSpell(unit, spell, unit.getX(), unit.getY(), unit.getPlayer());
            }
            i++;
        }
        moveCardToGraveYard(unit);
        map.getCell(unit.getX(), unit.getY()).addFlag(unit.getNumberOfFlags());
        unit.getPlayer().removeFromUnits(unit);
        dead.add(unit);
    }

    private void handlePoison(Unit unit) {
        if (unit.isPoisonImmune()) {
            return;
        }
        for (Buff buff : unit.getBuffs()) {
            unit.receiveDamage(buff.getPoison());
        }
        if (unit.calculateHP() <= 0) {
            checkOnDeath(unit);
        }
    }

    // no special powers included
    private void rawAttack(Unit attacker, Unit defender) {
        int damage = attacker.calculateAP();
        if (!attacker.isHolyIgnoring())
            damage -= defender.calculateHoly();
        damage = Math.max(0, damage);
        if (defender.isBully() && defender.calculateAP() > attacker.calculateAP()) {
            damage = 0;
        }
        defender.receiveDamage(damage);
    }

    // returns -1 if attacker couldn't attack and -2 if defender wasn't in range and 0 otherwise
    // returns 1 if the attacker/defender don't exist
    private int attackState(Unit attacker, Unit defender) {
        if (attacker == null) {
            view.showNoUnitSelectedError();
            return 1;
        }
        if (defender == null) {
            view.showNoSuchUnitFoundError();
            return 1;
        }
        if (!attacker.getCanAttack() || attacker.isStunned())
            return -1;
        int distance = getDistance(attacker.getX(), attacker.getY(), defender.getX(), defender.getY());
        boolean can = canAttack(attacker, defender, distance);
        return can ? 0 : -2;
    }

    public boolean canAttack(Unit attacker, Unit defender, int distance) {
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
        return can;
    }


    // special powers are included but defender doesn't counter attack
    private void oneSidedAttack(Unit attacker, Unit defender) {
        int i = 0;
        ArrayList<Spell> spells = attacker.getSpecialPowers();
        ArrayList<SpecialPowerType> types = attacker.getSpecialPowerTypes();
        for (Spell spell : spells) {
            if (types.get(i) == SpecialPowerType.ON_ATTACK || types.get(i) == SpecialPowerType.ON_USE) {
                castSpellOnCellUnit(attacker, spell, map.getGrid()[defender.getX()][defender.getY()], attacker.getPlayer());
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
//        System.err.println("shit pel " + attacker.getName() + defender.getName() + defender.isDisarmed());
        if (!defender.isDisarmed())
            rawAttack(defender, attacker);
    }


    // returns -1 in case when attacker can't attack
    // returns -2 if defender is not in attack range of attacker
    // returns 0 for success
    // if oneSided is true defender doesn't counter attack (for combo attacks)

    public void printSpells(Unit unit) {
        //System.err.println(unit.getName());
//        System.err.println("SPECIAL POWERS: ");
//        for (Spell spell : unit.getSpecialPowers()) {
//            System.err.println(spell);
//        }
        //System.err.println("BUFFS: ");
        unit.getBuffs().forEach(System.out::println);
    }

    public void forTesting(Unit attacker, Unit defender) {
        printSpells(attacker);
        printSpells(defender);
    }


    public int attackUnitByUnit(Unit attacker, Unit defender, boolean oneSided) {
        int state = attackState(attacker, defender);

//        System.err.println(attacker.getName() + " is attacking " + defender.getName() + " on sided " + oneSided);
//        System.err.println("attack state : " + state);
//        forTesting(attacker, defender);

        if (state != 0) {
            return state;
        }
        if (defender.isDead()) {
            return 0;
        }
        if (!oneSided) {
            twoSidedAttack(attacker, defender);
        } else {
            oneSidedAttack(attacker, defender);
        }
        attacker.setCanAttack(false);
        attacker.setCanMove(false);

        if (defender.calculateHP() <= 0) {
            checkOnDeath(defender);
        }

        return 0;
    }

    public boolean attackTargetCardWithSelectedUnit(String targetCardID) {
        Unit targetUnit = findUnitInGridByID(targetCardID);
        if (targetUnit == null || targetUnit.getPlayer() == getCurrentPlayer()) { // invalid card id
            if (!hasAI[turn % 2])
                view.showInvalidCardIDError();
            return false;
        }

        int state = attackUnitByUnit(selectedUnit, targetUnit, false);
        if (state == -1) { // has already attacked before or is stunned
            if (!hasAI[turn % 2])
                view.logMessage("Card with " + selectedUnit.getID() + " can't attack");
            return false;
        }

        if (state == -2) { // can't attack because
            if (!hasAI[turn % 2])
                view.logMessage("opponent minion is unavailable for attack");
            return false;
        }
        if (state == 1) {
            return false;
        }
//        if (hasAI[turn % 2])
//            view.logMessage("attack from " + selectedUnit.getID() + " to " + targetCardID);
        checkForDeath();
        return true;
    }

    public void attackCombo(String targetCardID, String[] friendlyCardsIDs) {
        Unit defender = findUnitInGridByID(targetCardID);
        if (defender == null || defender.getPlayer() == getCurrentPlayer()) {
            view.showInvalidCardIDError();
            return;
        }
        for (String friendlyCardsID : friendlyCardsIDs) {
            Unit attacker = findUnitInGridByID(friendlyCardsID);
            if (attacker == null) {
                view.showInvalidCardIDError();
                return;
            }
            if (attacker.getPlayer() != getCurrentPlayer()) {
                continue;
            }
            int state = attackState(attacker, defender);
            if (state == -1) {
                if (!hasAI[turn % 2])
                    view.logMessage("Card with " + attacker.getID() + " can't attack");
                return;
            } else if (state == -2) {
                if (!hasAI[turn % 2])
                    view.logMessage("opponent minion is unavailable for attack");
                return;
            }
        }

        for (int i = 0; i < friendlyCardsIDs.length; i++) {
            Unit attacker = findUnitInGridByID(friendlyCardsIDs[i]);
            attackUnitByUnit(attacker, defender, i != 0);
        }
        checkForDeath();
    }

    // x, y must be valid
    public boolean useHeroSpecialPower(int x, int y) { // true if successful
        Hero hero = getCurrentPlayer().getHero();
        Player player = getCurrentPlayer();
        if (hero.getSpecialPowers() == null) {
            return false;
        }
        if (player.getMana() < hero.getManaCost()) {
            if (!hasAI[turn % 2])
                view.showNotEnoughManaError();
            return false;
        }
        if (hero.getRemainingCooldown() != 0) {
            if (!hasAI[turn % 2])
                view.showCooldownError();
            return false;
        }
        hero.resetRemainingCooldown();
        int i = 0;
        for (Spell spell : hero.getSpecialPowers()) {
            if (spell.getTargetUnit() == Spell.TargetUnit.SELF) {
                x = hero.getX();
                y = hero.getY();
            }
            castSpell(hero, spell, x, y, getCurrentPlayer());
            i++;
        }
        checkForDeath();
        if (!hasAI[turn % 2])
            view.logMessage("hero power successfully used");
        return true;
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
                return GameState.DRAW;
            case HOLD_THE_FLAG:
                if (players[0].getNumberOfFlagTurns() >= NUMBER_OF_FLAG_TURNS)
                    return GameState.WIN_FIRST_PLAYER;
                if (players[1].getNumberOfFlagTurns() >= NUMBER_OF_FLAG_TURNS)
                    return GameState.WIN_SECOND_PLAYER;
                return GameState.DRAW;
            default:
                if (players[0].getHero().calculateHP() <= 0)
                    return GameState.WIN_SECOND_PLAYER;
                if (players[1].getHero().calculateHP() <= 0)
                    return GameState.WIN_FIRST_PLAYER;
                return GameState.DRAW;
        }
    }

    private void rewardPlayer(Player player) {
    }

    void moveCardToGraveYard(Card card) { // How to access the card
        card.getPlayer().addToGraveyard(card);
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
            if (!hasAI[turn % 2])
                view.showInvalidCardIDError();
            return;
        }
        if (!hasAI[turn % 2])
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

    private boolean isValidTarget(Unit castingUnit, Spell spell, Cell cell, Player player) {
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
        boolean isForCastingPlayer = unit.getPlayer() == player;
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
                valid = unit == castingUnit;
                break;
            case UNIT:
                valid = true;
                break;
        }
        return valid;
    }

    private void castSpellOnCellUnit(Unit castingUnit, Spell spell, Cell cell, Player player) {
        boolean valid = isValidTarget(castingUnit, spell, cell, player);
        Unit unit = (Unit) cell.getContent();
        if (valid) {
            if (spell.canDispel()) {
                unit.removeBuffs(player != unit.getPlayer());
                // if the the player casting the spell is the same one owning this unit
            }

            boolean isSpellImmune = unit.isSpellImmune();
            if (spell.getBuffs() != null) {
                for (Buff buff : spell.getBuffs()) {
                    if (!buff.isPositiveBuff() && isSpellImmune)
                        continue;
//                    if (buff.getAllegiance().equals(FRIENDLY) && unit.getPlayer() == player)
//                        unit.addBuff(new Buff(buff));
//                    else if (buff.getAllegiance().equals(ENEMY) && unit.getPlayer() != player)
//                        unit.addBuff(new Buff(buff));
//                    else if (buff.getAllegiance().equals(NEITHER))
                        unit.addBuff(new Buff(buff));
                }
            }
        }
    }

    private void castSpellOnCell(Spell spell, Cell cell, Player player) {
        for (Buff buff : spell.getBuffs()) {
            cell.addEffect(new Buff(buff), player);
        }
    }

    private void castSpellOnCoordinate(Unit castingUnit, Spell spell, Cell cell, Player player) {
        switch (spell.getTargetType()) {
            case UNIT:
                castSpellOnCellUnit(castingUnit, spell, cell, player);
                break;
            case CELL:
                castSpellOnCell(spell, cell, player);
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

    private ArrayList<Cell> getTargets(Unit castingUnit, Spell spell, int x, int y, Player player) {
        ArrayList<Cell> targets = new ArrayList<>();
        switch (spell.getTargetArea()) {
            case ALL_OF_THE_MAP:
                for (int i = 0; i < map.getNumberOfRows(); i++) {
                    for (int j = 0; j < map.getNumberOfColumns(); j++) {
                        Cell cell = map.getGrid()[i][j];
                        if (isValidTarget(castingUnit, spell, cell, player)) {
                            targets.add(cell);
                        }
                    }
                }
                break;

            case ADJACENT_9:
                Cell cell = map.getGrid()[x][y];
                if (isValidTarget(castingUnit, spell, cell, player)) {
                    targets.add(cell);
                }

            case ADJACENT_8:
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if (inMap(i, j)) {
                            if (i == x && y == j)
                                continue;
                            cell = map.getGrid()[i][j];
                            if (isValidTarget(castingUnit, spell, cell, player)) {
                                targets.add(cell);
                            }
                        }
                    }
                }
                break;

            case ADJACENT_4:
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if (inMap(i, j)) {
                            cell = map.getGrid()[i][j];
                            if (getDistance(i, j, x, y) == 1 && isValidTarget(castingUnit, spell, cell, player)) {
                                targets.add(cell);
                            }
                        }
                    }
                }
                break;

            case SELECTED_X_Y_GRID:
                for (int i = x; i < x + spell.getGridX(); i++) {
                    for (int j = y; j < y + spell.getGridY(); j++) {
                        if (inMap(i, j)) {
                            cell = map.getGrid()[i][j];
                            if (isValidTarget(castingUnit, spell, cell, player)) {
                                targets.add(cell);
                            }
                        }
                    }
                }
                break;

            case SAME_ROW:
                for (int i = 0; i < map.getNumberOfColumns(); i++) {
                    cell = map.getGrid()[x][i];
                    if (isValidTarget(castingUnit, spell, cell, player)) {
                        targets.add(cell);
                    }
                }
                break;

            case SAME_COLUMN:
                for (int i = 0; i < map.getNumberOfRows(); i++) {
                    cell = map.getGrid()[i][y];
                    if (isValidTarget(castingUnit, spell, cell, player)) {
                        targets.add(cell);
                    }
                }
                break;

            case DISTANCE_2:
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if (inMap(i, j)) {
                            cell = map.getGrid()[i][j];
                            if (getDistance(i, j, x, y) <= 2 && isValidTarget(castingUnit, spell, cell, player)) {
                                targets.add(cell);
                            }
                        }
                    }
                }
                break;

            case SELECTED_CELL:
                cell = map.getGrid()[x][y];
                if (isValidTarget(castingUnit, spell, cell, player)) {
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

    private void castSpell(Unit castingUnit, Spell spell, int x, int y, Player player) {
        ArrayList<Cell> targets = getTargets(castingUnit, spell, x, y, player);
        shuffle(targets); // here we handle random targets!
        for (int i = 0; i < Math.min(targets.size(), spell.getNumberOfRandomTargets()); i++) {
            castSpellOnCoordinate(castingUnit, spell, targets.get(i), player);
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
                castSpell(unit, spell, x, y, unit.getPlayer());
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
        if (targetArea != Spell.TargetArea.SELECTED_CELL || isValidTarget(null, spellCard.getSpell(), map.getGrid()[x][y], getCurrentPlayer())) {
            castSpell(null, spellCard.getSpell(), x, y, player);
        } else {
            if (!hasAI[turn % 2])
                view.showInvalidTargetError();
            return false;
        }
        spellCard.setCollectionItemID(getNewID(spellCard));
        spellCard.setPlayer(player);
        moveCardToGraveYard(spellCard);
        return true;
    }

    private boolean putUnitCard(Unit unit, int x, int y) {
        Cell[][] grid = map.getGrid();
        if (grid[x][y].getContent() instanceof Unit) {
            // the cell is already not empty
            if (!hasAI[turn % 2])
                view.showInvalidTargetError();
            return false;
        }
        if (!(unit instanceof Hero)) {
            boolean hasAdjacentFriendly = false;
            for (int dx = -1; dx < 2; dx++)
                for (int dy = -1; dy < 2; dy++)
                    if (inMap(x + dx, y + dy))
                        if (grid[x + dx][y + dy].getContent() instanceof Unit)
                            if (((Unit) grid[x + dx][y + dy].getContent()).getPlayer() == getCurrentPlayer())
                                hasAdjacentFriendly = true;
            if (!hasAdjacentFriendly) {
                if (!hasAI[turn % 2])
                    view.showNoAdjacentFriendlyUnitError();
                return false;
            }
        }
        Player player = getCurrentPlayer();
        unit.setPlayer(player);
        grid[x][y].setContent(unit, player); // finally put the card on cell ([x], [y])
        checkOnSpawn(unit, x, y);
        unit.setX(x);
        unit.setY(y);
        unit.setCollectionItemID(getNewID(unit));
        player.addUnit(unit);
        for (int i = 0; i < unit.getSpecialPowers().size(); i++) {
            Spell spell = unit.getSpecialPowers().get(i);
            SpecialPowerType specialPowerType = unit.getSpecialPowerTypes().get(i);
            if (specialPowerType == SpecialPowerType.PASSIVE) {
                castSpell(unit, spell, unit.getX(), unit.getY(), unit.getPlayer());
            }
        }
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
    public boolean insertCard(String cardName, int x, int y) { // returns true if successful
        Player player = getCurrentPlayer();
        Card card = player.findCardInHand(cardName);
        if (card == null) { // no such card is found in player's hand
            if (!hasAI[turn % 2])
                view.showInvalidCardError();
            return false;
        }
        if (!inMap(x, y)) {
            if (!hasAI[turn % 2])
                view.showInvalidCoordinatesError();
            return false;
        }
        if (player.getMana() < card.getManaCost()) { // player doesn't have enough mana
            if (!hasAI[turn % 2])
                view.showNotEnoughManaError();
            return false;
        }
        boolean inserted = false;
        if (card instanceof Unit) {
            inserted = putUnitCard((Unit) card, x, y);
        }
        if (card instanceof SpellCard) {
            inserted = castSpellCard((SpellCard) card, x, y, getCurrentPlayer());
            checkForDeath();
        }
        if (inserted) {
            if (!hasAI[turn % 2])
                view.logMessage(cardName + " with " + card.getID() + " inserted to " + "(" + (x + 1) + "," + (y + 1) + ")");
//            else
//                view.logMessage("a new card inserted to " + (x + 1) + " " + (y + 1));
            // log success message
            player.decreaseMana(card.getManaCost());
            player.getHand().getCards().remove(card);
        }
        checkForDeath();
        return inserted;
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
        if (item == null)
            return;
        switch (item.getItemType()) {
            case ADD_MANA:
                if (item.getAddManaDuration() >= (turn - startTime) / 2) {
                    player.addMana(item.getAddMana());
                    if (item instanceof Collectible) {
                        getCurrentPlayer().removeCollectible(item);
                        currentItems.get(getCurrentPlayer()).remove(item);
                        itemCastingTurns.get(getCurrentPlayer()).remove(item);
                        return;
                    }
                }
                if (!(currentItems.get(player).contains(item))) {
                    currentItems.get(player).add(item);
                    itemCastingTurns.get(player).put(item, turn);
                }
                break;
            case ADD_A_SPECIAL_POWER:
                for (int i = 0; i < item.getSpecialPowers().size(); i++) {
                    Spell specialPower = item.getSpecialPowers().get(i);
                    SpecialPowerType specialPowerType = item.getSpecialPowerTypes().get(i);
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
                    // Here I add special power to units in deck
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
                castSpell(null, item.getSpell(), r, c, player);
                break;
        }
        if (item instanceof Collectible)
            getCurrentPlayer().removeCollectible(item);
    }

    public void initiateGame() {
        numberOfPlayedCollectionItems.add(new HashMap<>());
        numberOfPlayedCollectionItems.add(new HashMap<>());
        currentItems.put(players[0], new ArrayList<>());
        currentItems.put(players[1], new ArrayList<>());
        itemCastingTurns.put(players[0], new HashMap<>());
        itemCastingTurns.put(players[1], new HashMap<>());
        turn = 0;
        putUnitCard(players[0].getHero(), HERO_INITIAL_ROW[0], HERO_INITIAL_COLUMN[0]);
        turn = 1;
        putUnitCard(players[1].getHero(), HERO_INITIAL_ROW[1], HERO_INITIAL_COLUMN[1]);
        turn = 0;
        if (gameType == GameType.COLLECT_THE_FLAGS) {
            for (int i = 0; i < numberOfFlags; i++) {
                putARandomFlag();
            }
        } else if (gameType == GameType.HOLD_THE_FLAG) {
            putARandomFlag();
        }
        for (int i = 0; i < 2; i++) {
            players[i].initiateHand();
            if (players[i].getUsable() != null) {
                Usable usable = new Usable(players[i].getUsable());
                castItem(usable, players[i], 0, 0, 0);
            }
        }

        try {
            for (File file : new File("./gameData/Collectibles/").listFiles()) {
                YaGson yaGson = new YaGson();
                collectibles.add(yaGson.fromJson(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())),
                        StandardCharsets.UTF_8), Collectible.class));
            }
            Collections.shuffle(collectibles);
            for (int i = 0; i < NUMBER_OF_COLLECTIBLES; i++) {
                Random random = new Random();
                Cell cell = getRandomEmptyCell();
                cell.setContent(collectibles.get(Integer.min(i, collectibles.size() - 1)));
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        // initiate next turn
        initiateTurn();
    }

    private Cell getRandomEmptyCell() {
        Random random = new Random();
        Cell cell = map.getCell(random.nextInt(5), random.nextInt(9));
        while (cell.hasContent() && cell.getNumberOfFlags() == 0)
            cell = map.getCell(random.nextInt(5), random.nextInt(9));
        return cell;
    }

    private void putARandomFlag() {
        Random random = new Random();
        Cell cell = getRandomEmptyCell();
        cell.addFlag(1);
    }

    public void initiateTurn() {
        // mana processes
        getCurrentPlayer().setMana(Math.min(9, (turn + 1) / 2 + 2));
        getCurrentPlayer().refillHand();

        // item processes
        for (int i = 0; i < currentItems.get(getCurrentPlayer()).size(); i++) {
            Item item = currentItems.get(getCurrentPlayer()).get(i);
            int startTime = itemCastingTurns.get(getCurrentPlayer()).get(item);
            castItem(item, getCurrentPlayer(), 0, 0, startTime);
        }

        // passives
        ArrayList<Unit> allUnits = new ArrayList<>(players[0].getUnits());
        allUnits.addAll(players[1].getUnits());
        ArrayList<Unit> temp = new ArrayList<>();
        for (Unit unit : allUnits) {
            prepareUnit(unit);
            for (int i = 0; i < unit.getSpecialPowers().size(); i++) {
                Spell spell = unit.getSpecialPowers().get(i);
                SpecialPowerType specialPowerType = unit.getSpecialPowerTypes().get(i);
                if (specialPowerType == SpecialPowerType.PASSIVE) {
                    castSpell(unit, spell, unit.getX(), unit.getY(), unit.getPlayer());
                    if (unit.calculateHP() <= 0) {
                        temp.add(unit);
                    }
                }
            }
        }
        for (Unit unit : temp)
            checkOnDeath(unit);

        if (hasAI[turn % 2]) {
            ai.makeMove();
            endTurn();
        }
    }

    private void checkForDeath() {
        ArrayList<Unit> allUnits = new ArrayList<>(players[0].getUnits());
        allUnits.addAll(players[1].getUnits());
        ArrayList<Unit> temp = new ArrayList<>();
        for (Unit unit : allUnits)
            if (unit.calculateHP() <= 0) {
                temp.add(unit);
            }
        for (Unit unit : temp)
            checkOnDeath(unit);
    }

    private void prepareUnit(Unit unit) {
        unit.setCanMove(true);
        unit.setCanAttack(true);
    }

    public void endTurn() {
        // lower the duration of buffs and remove buff
        for (int i = 0; i < getMap().getNumberOfRows(); i++)
            for (int j = 0; j < getMap().getNumberOfColumns(); j++) {
                Cell cell = getMap().getCell(i, j);
                if (cell.getContent() instanceof Card) {
                    Card card = (Card) cell.getContent();
                    if (card instanceof Unit) {
                        Unit unit = (Unit) card;
                        addPoison(cell, unit);
                        addFire(cell, unit);
                        if (unit.getNumberOfFlags() > 0)
                            unit.getPlayer().addNumberOfFlagTurns();
                        for (int t = unit.getBuffs().size() - 1; t >= 0; t--) {
                            Buff buff = unit.getBuffs().get(t);
                            handlePoison(unit);
                            buff.decrementDuration();
                            if (buff.getDuration() <= 0) {
                                unit.getBuffs().remove(t);
                            }
                        }
                    }
                }
                cell.getEffects().forEach(Buff::decrementDuration);
                for (int k = cell.getEffects().size() - 1; k >= 0; k--)
                    if (cell.getEffects().get(k).getDuration() <= 0) {
                        cell.getEffects().remove(cell.getEffects().get(k));
                        cell.getCasters().remove(cell.getCasters().get(k));
                    }
            }

        // can move and can attack for all units
        for (int row = 0; row < getMap().getNumberOfRows(); row++)
            for (int column = 0; column < getMap().getNumberOfColumns(); column++) {
                Cell cell = getMap().getCell(row, column);
                if (cell.getContent() != null && cell.getContent() instanceof Unit) {
                    Unit unit = (Unit) cell.getContent();
                    unit.setCanAttack(true);
                    unit.setCanMove(true);
                }
            }

        // cool down here
        getCurrentPlayer().getHero().decreaseRemainingCooldown();

        // add turn
        turn++;

        selectedUnit = null;
        selectedCollectible = null;

        // initiate next turn
        initiateTurn();
    }

    private void addFire(Cell cell, Unit unit) {
        for (int i = 0; i < cell.getEffects().size(); i++) {
            Buff o = cell.getEffects().get(i);
            Player caster = cell.getCasters().get(i);
            if (o.getEffectHp() != 0) {
//                if (o.getAllegiance().equals(FRIENDLY) && unit.getPlayer() == caster)
//                    unit.addBuff(Buff.newFireBuff(o.getEffectHp()));
//                else if (o.getAllegiance().equals(ENEMY) && unit.getPlayer() != caster)
//                    unit.addBuff(Buff.newFireBuff(o.getEffectHp()));
//                else if (o.getAllegiance().equals(NEITHER))
                    unit.addBuff(Buff.newFireBuff(o.getEffectHp()));
            }
        }
    }

    private void addPoison(Cell cell, Unit unit) {
        for (int i = 0; i < cell.getEffects().size(); i++) {
            Buff o = cell.getEffects().get(i);
            Player caster = cell.getCasters().get(i);
            if (o.getPoison() != 0) {
//                if (o.getAllegiance().equals(FRIENDLY) && unit.getPlayer() == caster)
//                    unit.addBuff(Buff.newPoisonBuff(o.getPoison()));
//                else if (o.getAllegiance().equals(ENEMY) && unit.getPlayer() != caster)
//                    unit.addBuff(Buff.newPoisonBuff(o.getPoison()));
//                else if (o.getAllegiance().equals(NEITHER))
                    unit.addBuff(Buff.newPoisonBuff(o.getPoison()));
            }
        }
    }

    public boolean selectCollectible(String collectibleID) {
        if (!hasAI[turn % 2])
            view.alertCollectibleSelection(collectibleID);
        selectedCollectible = getCurrentPlayer().getCollectible(collectibleID);
        return selectedCollectible != null;
    }

    public void applyCollectible(int row, int column) {
        if (selectedCollectible.getItemType() == ItemType.ADD_MANA) {
            view.showUncastableItemError();
            return;
        }
        castItem(selectedCollectible, getCurrentPlayer(), row, column, turn);
        checkForDeath();
    }

    public void showNextCardInDeck() {
        view.showCardInfo(getCurrentPlayer().getDeck().getNextCard());
    }

    public void showHand() {
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

    public ArrayList<Card>[] showAvailableOptions() {
        ArrayList<Card>[] availableOptions = new ArrayList[3];
        for (int i = 0; i < availableOptions.length; i++)
            availableOptions[i] = new ArrayList<>();
        Player player = getCurrentPlayer();
        for (Card card : player.getHand().getCards())
            if (card.getManaCost() <= player.getMana())
                availableOptions[2].add(card);
        for (Unit attacker : player.getUnits()) {
            if (attacker.getCanMove())
                availableOptions[0].add(attacker);
            for (Unit defender : players[1 - turn % 2].getUnits())
                if (attackState(attacker, defender) == 0)
                    availableOptions[1].add(defender);
        }
        return availableOptions;
    }

    public void shengdeShow() {
        System.out.println("Turn number: " + turn);
        System.out.println(players[0].getName() + " Mana(" + players[0].getMana() + ") deck(" + players[0].getDeck().getCards().size() + ") hand:");
        for (Card card : players[0].getHand().getCards()) {
            System.out.format("%-35s", card.getName() + "(" + card.getManaCost() + ")");
        }
        System.out.println();
        System.out.print("Player 1 usable item is: ");
        if (players[0].getDeck().getDeckUsableItem() == null) {
            System.out.println("No Items selected");
        } else {
            System.out.println(players[0].getDeck().getDeckUsableItem().getName());
        }

        for (int row = 0; row < getMap().getNumberOfRows(); row++) {
            for (int column = 0; column < getMap().getNumberOfColumns(); column++) {
                Cell cell = getMap().getCell(row, column);
                String output;
                if (!cell.hasContent() && !(cell.getContent() instanceof CollectionItem)) {
                    output = ".";
                } else {
                    CollectionItem collectionItem = (CollectionItem) cell.getContent();
                    output = collectionItem.getName();
                    if (collectionItem instanceof Unit) {
                        if (((Unit) collectionItem).getPlayer() == players[0]) {
                            output = "+" + output + "+";
                            if ((collectionItem) == selectedUnit) {
                                output = "[" + output + "]";
                            }
                        } else {
                            output = "-" + output + "-";
                        }
                    } else {
                        output = "(" + output + ")";
                    }
                }
                int numberOfFlags = cell.getNumberOfFlags();
                if (cell.getContent() instanceof Unit)
                    numberOfFlags += ((Unit) cell.getContent()).getNumberOfFlags();
                output += ":" + numberOfFlags;
                output += "!" + (cell.getEffects().stream().map(Buff::getPoison).reduce(Integer::sum).orElse(0));
                output += "?" + (cell.getEffects().stream().map(Buff::getEffectHp).reduce(Integer::sum).orElse(0));
                System.out.format("%-40s", output);
            }
            System.out.print("\n");
        }
        System.out.println(players[1].getName() + " Mana(" + players[1].getMana() + ") deck(" + players[1].getDeck().getCards().size() + ") hand:");
        for (Card card : players[1].getHand().getCards()) {
            System.out.format("%-35s", card.getName() + "(" + card.getManaCost() + ")");
        }
        System.out.println();
        System.out.print("Player 2 usable item is: ");
        if (players[1].getDeck().getDeckUsableItem() == null) {
            System.out.println("No Items selected");
        } else {
            System.out.println(players[1].getDeck().getDeckUsableItem().getName());
        }
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public AccountUser getOtherAccount() {
        return accounts[turn % 2];
    }

    public void killInstantly(String unitID) {
        Unit unit = findUnitInGridByID(unitID);
        if (unit == null) {
            return;
        }
        unit.receiveDamage(1000000);
        checkOnDeath(unit);
    }

    public void getDead() {
        dead.forEach(unit -> {
            System.out.print("death " + unit.getName() + " " + unit.getX() + " " + unit.getY());
            if (unit.getSpecialPowerTypes().stream().anyMatch(e -> e == SpecialPowerType.ON_DEATH)) {
                System.out.println(" onDeath");
            }
            else
                System.out.println();
        });
        dead.clear();
    }

    public Player getOtherPlayer() {
        if (turn % 2 == 0) {
            return players[1];
        } else {
            return players[0];
        }
    }

    public enum GameState {
        WIN_FIRST_PLAYER,
        DRAW,
        WIN_SECOND_PLAYER
    }
}