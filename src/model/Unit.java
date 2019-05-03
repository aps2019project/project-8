package model;

import java.util.ArrayList;

public class Unit extends Card{
    private int hitPoint;
    private int attackPoint;
    private ArrayList<Buff> buffs = new ArrayList<>();
    private int flags = 0;
    private Player player = null;
    private boolean canMove = false;
    private boolean canAttack = false;

    private int x = 0;
    private int y = 0;

    private UnitType unitType;
    private ArrayList<Spell> specialPower;
    private ArrayList<SpecialPowerType> specialPowerType;
    private String cardID = "";
    private boolean canFly;
    private int attackTimes = 0;
    private int defenceTimes = 0;
    private ArrayList<Card> hasAttacked = new ArrayList<>();
    private int attackRange;

    // Main constructor
    protected Unit(Card card,
                   int hitPoint, int attackPoint, UnitType unitType, ArrayList<SpecialPowerType> specialPowerType,
                   ArrayList<Spell> specialPower, boolean canFly, int attackRange) { // Main constructor
        super(card);
        this.hitPoint = hitPoint;
        this.attackPoint = attackPoint;
        this.unitType = unitType;
        this.specialPowerType = specialPowerType;
        this.specialPower = specialPower;
        this.canFly = canFly;
        this.attackRange = attackRange;
    }

    // Copy constructor
    public Unit(Unit unit) {
        this.price = unit.price;
        this.collectionItemID = unit.collectionItemID;
        this.name = unit.name;
        this.manaCost = unit.manaCost;
        this.hitPoint = unit.hitPoint;
        this.attackPoint = unit.attackPoint;
        this.unitType = unit.unitType;
        this.specialPowerType = unit.specialPowerType;
        this.specialPower = unit.specialPower;
        this.canFly = unit.canFly;
        this.attackRange = unit.attackRange;
    }

    protected Unit() {
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

        public UnitBuilder setSpell(ArrayList<Spell> specialPower) {
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

    public int getAttackRange() {
        return attackRange;
    }

    public boolean hasFlag() {
        return flags > 0;
    }

    public int getHitPoint() {
        return hitPoint;
    }

    public int getAttackPoint() {
        return attackPoint;
    }

    public boolean canFly() { return canFly; }

    public void receiveHit(int attackPoint) {
        hitPoint -= attackPoint;
        if (hitPoint <= 0) { // ?

        }
    }

    public void decreaseHealth(int amount) {}

    public boolean isAlive() {
        return hitPoint > 0;
    }

    public void counterAttack(Unit unit) {
        // ?
    }

    public ArrayList<SpecialPowerType> getSpecialPowerType() {
        return specialPowerType;
    }

    public void passTurn() {
        // ?
    }

    // boolean or void ? what is it supposed to do
    public boolean hasAttacked(Card card) {
        return false; // ?
    }

    public boolean equals() {
        return false;
        // ?
    }

    public void addBuff(Buff buff) {
        buffs.add(buff);
    }

    public void dispel() {
        // ?
    }

    public void addRange(int amount) {
        attackRange += amount; // ?
    }

    public UnitType getUnitType() {
        return unitType;
    }

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
        return "";
    }
}
