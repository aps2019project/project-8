package model;

public class Usable extends Item {
    private Spell spell = null;

    public Usable(int price, String collectionItemID, String name, Spell spell) {
        super(price, collectionItemID, name);
        this.spell = spell;
    }

    public Spell getSpell() {
        return spell;
    }
}
