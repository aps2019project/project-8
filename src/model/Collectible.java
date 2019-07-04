package model;

import java.util.Iterator;

public class Collectible extends Item {

    // Main constructor
    public Collectible(Item item) {
        super(item);
    }

    // Copy constructor
    public Collectible(Collectible collectible) {
        super(collectible);
    }

    protected Collectible() {}

    // Builder
    public static class CollectibleBuilder{
        private Item item;

        public CollectibleBuilder setItem(Item item) {
            this.item = item;
            return this;
        }

        public Collectible build() {
            return new Collectible(item);
        }
    }

    public Spell getSpell() {
        return spell;
    }
}

