package model;

public class SpellCard extends Card {
    private Spell spell;

    public SpellCard(Spell spell) {
        this.spell = spell;
    }

    public Spell getSpell() {
        return spell;
    }

    @Override
    public String toString() {
        String ans = "";
        return ans;
    }
}
