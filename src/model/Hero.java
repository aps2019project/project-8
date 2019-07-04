package model;

public class Hero extends Unit {
    private int cooldown;
    private int remainingCooldown;

    // Main constructor
    public Hero(Unit unit, int cooldown) {
        super(unit);
        this.cooldown = cooldown;
        remainingCooldown = 0;
    }

    public Hero(Hero hero) {
        super(hero);
        this.cooldown = hero.cooldown;
    }

    // Copy constructor
//    public Hero(Unit unit) {
//        super(unit);
//    }

    public Hero() {}

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void resetRemainingCooldown() {
        remainingCooldown = cooldown;
    }

    public void decreaseRemainingCooldown() {
        remainingCooldown--;
        remainingCooldown = Math.max(0, remainingCooldown);
    }

    public int getRemainingCooldown() {
        return remainingCooldown;
    }

    @Override
    public String toString() {
        return "Name : " + name + DASH +
                "AP : " + getAttackPoint() + DASH +
                "HP : " + getHitPoint() + DASH +
                "Class : " + getUnitType() + DASH +
                "Special power : " + getSpecialPowerDescription() + DASH +
                "Cooldown : " + getOriginalCooldown();
    }

    public int getOriginalCooldown() {
        return cooldown;
    }
}

