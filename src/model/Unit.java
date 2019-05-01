package model;

import java.awt.*;
import java.util.ArrayList;

public class Unit extends Card{
    private int hitPoint;
    private int attackPoint;
    private ArrayList<Buff> buffs;
    private int flags;
    private Player player;
    private boolean canMove;
    private boolean canAttack;

    private int x;
    private int y;

    private UnitType unitType;
    private Spell specialPower;
    private SpecialPowerType specialPowerType;
    private String cardID;
    private boolean isFlying;
    private int attackTimes;
    private int defenceTimes;
    private Faction faction;
    private ArrayList<Card> hasAttacked;
    private int attackRange;

    public Unit() {
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
