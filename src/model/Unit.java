package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.jar.Attributes;

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
    private Spell specialPower;
    private SpecialPowerType specialPowerType;
    private String cardID = "";
    private boolean canFly;
    private int attackTimes;
    private int defenceTimes;
    private Faction faction;
    private ArrayList<Card> hasAttacked = new ArrayList<>();
    private int attackRange;

    public Unit(int hitPoint, int attackPoint, UnitType unitType, SpecialPowerType specialPowerType,
                Spell specialPower, boolean canFly, Faction faction, int attackRange) {
        this.hitPoint = hitPoint;
        this.attackPoint = attackPoint;
        this.unitType = unitType;
        this.specialPowerType = specialPowerType;
        this.specialPower = specialPower;
        this.canFly = canFly;
        this.faction = faction;
        this.attackRange = attackRange;
    }

    public static class UnitBuilder {
        private int hitPoint = 0;
        private int attackPoint = 0;

        private UnitType unitType;
        private Spell specialPower;
        private SpecialPowerType specialPowerType;
        private boolean canFly = false;
        private int attackTimes = 0;
        private int defenceTimes = 0;
        private Faction faction;
        private int attackRange = 0;

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

        public UnitBuilder setSpecialPowerType(SpecialPowerType specialPowerType) {
            this.specialPowerType = specialPowerType;
            return this;
        }

        public UnitBuilder setSpell(Spell specialPower) {
            this.specialPower = specialPower;
            return this;
        }

        public UnitBuilder setCanFly(boolean canFly) {
            this.canFly = canFly;
            return this;
        }

        public UnitBuilder setFaction(Faction faction) {
            this.faction = faction;
            return this;
        }

        public UnitBuilder setAttackRange(int attackRange) {
            this.attackRange = attackRange;
            return this;
        }

        public Unit build() {
            return new Unit(hitPoint, attackPoint, unitType, specialPowerType, specialPower, canFly, faction, attackRange);
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

    public void recieveAttack(int attackPoint) {
        hitPoint -= attackPoint;
        if (hitPoint <= 0) { // ?

        }
    }

    public boolean isAlive() {
        return hitPoint > 0;
    }

    public void counterAttack(Unit unit) {
        // ?
    }

    public Spell castSpecialPower() {
        // ?
        return new Spell();
    }

    // is this correct ?
    // ?
    public SpecialPowerType getSpecialPower() {
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
}
