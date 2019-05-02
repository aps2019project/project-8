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
        this.price = spellCard.price;
        this.collectionItemID = spellCard.collectionItemID;
        this.name = spellCard.name;
        this.manaCost = spellCard.manaCost;
        this.spell = spellCard.spell;
    }

    protected SpellCard() {}

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

    public Spell getSpell() {
        return spell;
    }

    @Override
    public String toString() {
        String ans = "";
        return ans;
    }
}
