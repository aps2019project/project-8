package model;

public class Hero extends Unit {
    private int cooldown;
    private int remainingCooldown;

    // Main constructor
    public Hero(Unit unit, int cooldown) {
        super(unit);
        this.cooldown = cooldown;
    }
    
    // Copy constructor
    public Hero(Unit unit) {
        super(unit);
    }

    public Hero() {}

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}
