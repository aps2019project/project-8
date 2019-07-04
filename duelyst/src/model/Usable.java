package model;

public class Usable extends Item {
    // Main constructor
    public Usable(Item item) {
        super(item);
    }

    // Copy constructor
    public Usable(Usable usable) {
        super(usable);
    }

    // Builder
    public static class UsableBuilder{
        private Item item = null;
        public UsableBuilder setItem(Item item) {
            this.item = item;
            return this;
        }

        public Usable build() {
            return new Usable(item);
        }
    }

    public Spell getSpell() {
        return spell;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
