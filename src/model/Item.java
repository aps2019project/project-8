package model;

import java.util.ArrayList;

public class Item extends CollectionItem {
    protected Spell spell;
    protected int addMana;
    protected int addManaDuration;
    private ItemType itemType;
    private ArrayList<SpecialPowerType> specialPowerType;
    private ArrayList<Spell> specialPower;
    private ArrayList<Target> specialPowerTarget;

    public ArrayList<Spell> getSpecialPower() {
        return specialPower;
    }

    public ArrayList<Target> getSpecialPowerTarget() {
        return specialPowerTarget;
    }

    public ArrayList<SpecialPowerType> getSpecialPowerType() {
        return specialPowerType;
    }

    // Main constructor
    public Item(CollectionItem collectionItem, Spell spell, int addMana, int addManaDuration, ItemType itemType,
                ArrayList<SpecialPowerType> specialPowerType, ArrayList<Spell> specialPower, ArrayList<Target> specialPowerTarget) {
        super(collectionItem);
        this.spell = spell;
        this.addMana = addMana;
        this.addManaDuration = addManaDuration;
        this.itemType = itemType;
        this.specialPowerType = specialPowerType;
        this.specialPower = specialPower;
        this.specialPowerTarget = specialPowerTarget;
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
        this.itemType = item.itemType;
        this.specialPowerTarget = item.specialPowerTarget;
    }

    protected Item() {
    }


    public static class ItemBuilder {
        Spell spell;
        int addMana = 0;
        int addManaDuration = 0;
        CollectionItem collectionItem;
        ItemType itemType;
        ArrayList<SpecialPowerType> specialPowerType;
        ArrayList<Spell> specialPower;
        ArrayList<Target> specialPowerTarget;

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

        public ItemBuilder setItemType(ItemType itemType) {
            this.itemType = itemType;
            return this;
        }

        public ItemBuilder setSpecialPowerType(ArrayList<SpecialPowerType> specialPowerType) {
            this.specialPowerType = specialPowerType;
            return this;
        }

        public ItemBuilder setSpecialPower(ArrayList<Spell> specialPower) {
            this.specialPower = specialPower;
            return this;
        }

        public ItemBuilder setSpecialPowerTarget(ArrayList<Target> specialPowerTarget) {
            this.specialPowerTarget = specialPowerTarget;
            return this;
        }

        public Item build() {
            return new Item(collectionItem, spell, addMana, addManaDuration, itemType, specialPowerType, specialPower, specialPowerTarget);
        }
    }

    public ItemType getItemType() {
        return itemType;
    }

    public int getAddMana() {
        return addMana;
    }

    public int getAddManaDuration() {
        return addManaDuration;
    }

    @Override
    public String toString() {
        return "Name : " + getName() + DASH +
                "Desc : " + getDescription();
    }

    public static class Target {
        private TargetUnit targetUnit;
        private TargetUnitType targetUnitType;

        public Target(TargetUnit targetUnit, TargetUnitType targetUnitType) {
            this.targetUnit = targetUnit;
            this.targetUnitType = targetUnitType;
        }

        public TargetUnit getTargetUnit() {
            return targetUnit;
        }

        public TargetUnitType getTargetUnitType() {
            return targetUnitType;
        }

        public enum TargetUnit {
            FRIENDLY_UNIT,
            FRIENDLY_MINION,
            FRIENDLY_HERO,
        }

        public enum TargetUnitType {
            ALL,
            MELEE,
            RANGED,
            HYBRID,
            MELEE_RANGED,
            MELEE_HYBRID,
            RANGED_HYBRID
        }
    }
}
