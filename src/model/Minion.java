package model;

public class Minion extends Unit{
    public Minion(Unit unit) {
        super(unit);
    }

    @Override
    public String toString() {
        return "Type : Minion" + DASH +
                "Name : " + getName() + DASH +
                "Class : " + getUnitType() + DASH +
                "AP : " + getAttackPoint() + DASH +
                "HP : " + getHitPoint() + DASH +
                "MP : " + getManaCost() + DASH +
                "Special power : " + getSpecialPowerDescription();
    }
}
