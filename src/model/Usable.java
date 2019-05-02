package model;

public class Usable extends Item {
    private Spell spell = null;

    // Main constructor
    public Usable(Item item, Spell spell) {
        super(item);
        this.spell = spell;
    }

    // Copy constructor
    public Usable(Usable usable) {
        this.price = usable.price;
        this.collectionItemID = usable.collectionItemID;
        this.name = usable.name;
        this.description = usable.description;
        this.spell = usable.spell;
    }

    // Builder
    public static class UsableBuilder{
        private Item item = null;
        private Spell spell = null;

        public UsableBuilder setItem(Item item) {
            this.item = item;
            return this;
        }

        public UsableBuilder setSpell(Spell spell) {
            this.spell = spell;
            return this;
        }

        public Usable build() {
            return new Usable(item, spell);
        }
    }

    public Spell getSpell() {
        return spell;
    }
}
