package model;

import java.util.ArrayList;

public class Unit extends Card {


    // CollectionItem:
    private UnitType unitType;
    private int hitPoint;
    private int attackPoint;
    private ArrayList<SpecialPowerType> specialPowerTypes = null;
    private ArrayList<Spell> specialPowers = null;
    private boolean canFly;
    private int attackRange;
    // In Game:
    private ArrayList<Buff> buffs = new ArrayList<>();
    private int flags = 0;
    private boolean canMove = false;
    private boolean canAttack = false;
    private boolean isDead = false;
    private int x = 0;
    private int y = 0;
    private int attackTimes = 0;
    private int defenceTimes = 0;
    private ArrayList<Card> hasAttacked = new ArrayList<>();

    // Main constructor
    protected Unit(Card card,
                   int hitPoint, int attackPoint, UnitType unitType, ArrayList<SpecialPowerType> specialPowerType,
                   ArrayList<Spell> specialPower, boolean canFly, int attackRange) { // Main constructor
        super(card);
        this.hitPoint = hitPoint;
        this.attackPoint = attackPoint;
        this.unitType = unitType;
        this.specialPowerTypes = specialPowerType;
        this.specialPowers = specialPower;
        this.canFly = canFly;
        this.attackRange = attackRange;
    }

    // Copy constructor
    public Unit(Unit unit) {
        super(unit);
        this.hitPoint = unit.hitPoint;
        this.attackPoint = unit.attackPoint;
        this.unitType = unit.unitType;
        this.specialPowerTypes = unit.specialPowerTypes;
        this.specialPowers = unit.specialPowers;
        this.canFly = unit.canFly;
        this.attackRange = unit.attackRange;
        this.description = unit.description;
    }

    protected Unit() {
    }

    public int getHitPoint() {
        return hitPoint;
    }

    public ArrayList<SpecialPowerType> getSpecialPowerTypes() {
        return specialPowerTypes;
    }

    public ArrayList<Spell> getSpecialPowers() {
        return specialPowers;
    }

    public ArrayList<Buff> getBuffs() {
        return buffs;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public boolean hasFlag() {
        return flags > 0;
    }

    public boolean canFly() {
        return canFly;
    }

    public void receiveDamage(int damage) {
        hitPoint -= damage;
    }

    public int getAttackPoint() {
        return attackPoint;
    }

    public void addBuff(Buff buff) {
        buffs.add(buff);
    }

    // true for positive and false for negative
    public void removeBuffs(boolean type) {
        for (int i = 0; i < buffs.size(); i++) {
            if (buffs.get(i).isPositiveBuff() == type) {
                buffs.remove(i);
                i--;
            }
        }
    }

    public UnitType getUnitType() {
        return unitType;
    }

//    public void addRange(int amount) {
//        attackRange += amount; // ?
//    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean getCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public boolean getCanAttack() {
        return canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    protected String getSpecialPowerDescription() {
        return getDescription();
    }

    public int calculateAP() {
        int ans = attackPoint;
        for (Buff buff : buffs)
            ans += buff.getEffectAp();
        ans = Math.max(0, ans);
        return ans;
    }

    public int calculateHoly() {
        int ans = 0;
        for (Buff buff : buffs)
            ans += buff.getHoly();
        return ans;
    }

    public int calculateHP() {
        int ans = hitPoint;
        for (Buff buff : buffs)
            ans += buff.getEffectHp();
        return ans;
    }

    public boolean isDisarmed() {
        if (!isDisarmable())
            return false;
        for (Buff buff : buffs) {
            if (buff.canDisarm()) {
                return true;
            }
        }
        return false;
    }

    public boolean isStunned() {
        for (Buff buff : buffs) {
            if (buff.canStun()) {
                return true;
            }
        }
        return false;
    }

    // fields
    // disarmable poisonImmune and ... must be in Unit not spell

    public boolean isDisarmable() {
        boolean disarmable = true;
        for (Spell spell : specialPowers)
            disarmable &= spell.isDisarmable();
        return disarmable;
    }

    public boolean isPoisonImmune() {
        boolean poisonImmune = false;
        for (Spell spell : specialPowers)
            poisonImmune |= spell.isPoisonImmune();
        return poisonImmune;
    }

    public boolean isSpellImmune() {
        boolean isSpellImmune = false;
        for (Spell spell : specialPowers)
            isSpellImmune |= spell.isSpellImmune();
        return isSpellImmune;
    }

    public boolean isBully() {
        boolean isBully = false;
        for (Spell spell : specialPowers)
            isBully |= spell.isBully();
        return isBully;
    }

    public boolean isMultiplied() {
        boolean isMultiplied = false;
        for (Spell spell : specialPowers)
            isMultiplied |= spell.isMultiplied();
        return isMultiplied;
    }

    public boolean isHolyIgnoring() {
        boolean isHolyIgnoring = false;
        for (Spell spell : specialPowers)
            isHolyIgnoring |= spell.isIgnoreHoly();
        return isHolyIgnoring;
    }

    public int getNumberOfFlags() {
        return flags;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public void addFlags(int numberOfFlags) {
        flags += numberOfFlags;
    }

    public void showBuffs() {
        int i = 1;
        for (Buff buff : buffs) {
            System.err.println("Buff number." + i + ":\n");
            System.err.print(buff);
        }
    }

    // Builder
    public static class UnitBuilder {
        private int hitPoint = 0;
        private int attackPoint = 0;

        private UnitType unitType;
        private ArrayList<Spell> specialPower;
        private ArrayList<SpecialPowerType> specialPowerType;
        private boolean canFly = false;
        private int attackRange = 0;
        private Card card;

        public UnitBuilder setHitPoint(int hitPoint) {
            this.hitPoint = hitPoint;
            return this;
        }

        public UnitBuilder setAttackPoint(int attackPoint) {
            this.attackPoint = attackPoint;
            return this;
        }

        public UnitBuilder setUnitType(UnitType unitType) {
            this.unitType = unitType;
            return this;
        }

        public UnitBuilder setSpecialPowerType(ArrayList<SpecialPowerType> specialPowerType) {
            this.specialPowerType = specialPowerType;
            return this;
        }

        public UnitBuilder setSpecialPower(ArrayList<Spell> specialPower) {
            this.specialPower = specialPower;
            return this;
        }

        public UnitBuilder setCanFly(boolean canFly) {
            this.canFly = canFly;
            return this;
        }

        public UnitBuilder setAttackRange(int attackRange) {
            this.attackRange = attackRange;
            return this;
        }

        public UnitBuilder setCard(Card card) {
            this.card = card;
            return this;
        }

        public Unit build() {
            return new Unit(card, hitPoint, attackPoint, unitType, specialPowerType, specialPower, canFly, attackRange);
        }
    }
}
