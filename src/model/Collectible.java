package model;

import java.util.Iterator;

public class Collectible extends Item {
    Spell spell;

    // Main constructor
    protected Collectible(Item item, Spell spell) {
        super(item);
        this.spell = spell;
    }

    // Copy constructor
    public Collectible(Collectible collectible) {
        this.price = collectible.price;
        this.collectionItemID = collectible.collectionItemID;
        this.name = collectible.name;
        this.description = collectible.description;
        this.spell = collectible.spell;
    }

    protected Collectible() {}

    // Builder
    public static class CollectibleBuilder{
        private Item item;
        private Spell spell;

        public CollectibleBuilder setItem(Item item) {
            this.item = item;
            return this;
        }

        public CollectibleBuilder setSpell(Spell spell) {
            this.spell = spell;
            return this;
        }

        public Collectible build() {
            return new Collectible(item, spell);
        }
    }

    public Spell getSpell() {
        return spell;
    }
}
