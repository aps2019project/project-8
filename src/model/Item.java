package model;

import java.util.Iterator;

public class Item extends CollectionItem {
    protected Spell spell;
    protected int addMana;
    protected int addManaDuration;

    // Main constructor
    public Item(CollectionItem collectionItem, Spell spell, int addMana, int addManaDuration) {
        super(collectionItem);
        this.spell = spell;
        this.addMana = addMana;
        this.addManaDuration = addManaDuration;
    }

    // Copy constructor
    public Item(Item item) {
        this.price = item.price;
        this.collectionItemID = item.collectionItemID;
        this.name = item.name;
        this.description = item.description;
        this.spell = item.spell;
        this.addMana = item.addMana;
        this.addManaDuration = item.addManaDuration;
    }

    protected Item() {}


    public static class ItemBuilder {
        Spell spell;
        int addMana = 0;
        int addManaDuration = 0;
        CollectionItem collectionItem;

        public ItemBuilder setSpell(Spell spell) {
            this.spell = spell;
            return this;
        }

        public ItemBuilder setAddMana(int addMana) {
            this.addMana = addMana;
            return this;
        }

        public ItemBuilder setAddManaDuration(int addManaDuration) {
            this.addManaDuration = addManaDuration;
            return this;
        }

        public ItemBuilder setCollectionItem(CollectionItem collectionItem) {
            this.collectionItem = collectionItem;
            return this;
        }

        public Item build() {
            return new Item(collectionItem, spell, addMana, addManaDuration);
        }
    }
    @Override
    public String toString() {
        return getName();
    }
}
