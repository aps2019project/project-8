package model;

public class Hero extends Unit {
    private int cooldown;
    private int remainingCooldown;

    public Hero(Unit unit) {
        super(unit);
    }

    public Hero() {}

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}
