package model;

public class SpellCard extends Card {
    protected Spell spell;

    // Main constructor
    protected SpellCard(Card card, Spell spell) {
        super(card);
        this.spell = spell;
    }

    // Copy constructor
    public SpellCard(SpellCard spellCard) {
        super(spellCard);
        this.spell = spellCard.spell;
    }

    protected SpellCard() {
    }

    public Spell getSpell() {
        return spell;
    }

    @Override
    public String toString() {
        return "Type : Spell" + DASH +
                "Name : " + getName() + DASH +
                "MP : " + manaCost + DASH +
                "Desc : " + getDescription();
    }

    // Builder
    public static class SpellCardBuilder {
        private Spell spell = null;
        private Card card = null;

        public SpellCardBuilder setSpell(Spell spell) {
            this.spell = spell;
            return this;
        }

        public SpellCardBuilder setCard(Card card) {
            this.card = card;
            return this;
        }

        public SpellCard build() {
            return new SpellCard(card, spell);
        }
    }
}

