package model;

public class Spell {

    public static final int MAX_GOALS = 100;
    private TargetType targetType;
    private TargetArea targetArea;
    private TargetUnit targetUnit;
    private TargetUnitType targetUnitType;
    private int numberOfRandomTargets;
    private int gridX;
    private int gridY;
    private Buff[] buffs;
    private boolean canDispel;

    private boolean disarmable = true; // goraze vahshi // ok!
    private boolean poisonImmune = false; // piran // ok!
    private boolean spellImmune = false; // giv // ok!
    private boolean isBully = false; //  ashkboos // ok!
    private boolean multiplied = false; // pahlevane fars
    private boolean ignoreHoly = false; // shire darande // ok!

    public boolean isDisarmable() {
        return disarmable;
    }

    public boolean isPoisonImmune() {
        return poisonImmune;
    }

    public boolean isSpellImmune() {
        return spellImmune;
    }

    public boolean isBully() {
        return isBully;
    }

    public boolean isMultiplied() {
        return multiplied;
    }

    public boolean isIgnoreHoly() {
        return ignoreHoly;
    }

    // Main Constructor
    public Spell(TargetType targetType, TargetArea targetArea, TargetUnit targetUnit, TargetUnitType targetUnitType,
                 int numberOfRandomTargets, int gridX, int gridY, Buff[] buffs, boolean canDispel) {
        this.targetType = targetType;
        this.targetArea = targetArea;
        this.targetUnit = targetUnit;
        this.targetUnitType = targetUnitType;
        this.numberOfRandomTargets = numberOfRandomTargets;
        this.gridX = gridX;
        this.gridY = gridY;
        this.buffs = buffs;
        this.canDispel = canDispel;
    }
    // Copy constructor
    public Spell(Spell spell) {
        this.targetType = spell.targetType;
        this.targetArea = spell.targetArea;
        this.targetUnit = spell.targetUnit;
        this.targetUnitType = spell.targetUnitType;
        this.numberOfRandomTargets = spell.numberOfRandomTargets;
        this.gridX = spell.gridX;
        this.gridY = spell.gridY;
        this.buffs = spell.buffs;
        this.canDispel = spell.canDispel;
        this.disarmable = spell.disarmable;
        this.poisonImmune = spell.poisonImmune;
        this.spellImmune = spell.spellImmune;
        this.isBully = spell.isBully;
        this.multiplied = spell.multiplied;
        this.ignoreHoly = spell.ignoreHoly;
    }

    protected Spell() {
    }

    public void setMultiplied(boolean multiplied) {
        this.multiplied = multiplied;
    }

    public Spell setDisarmable(boolean disarmable) {
        this.disarmable = disarmable;
        return this;
    }

    public Spell setPoisonImmune(boolean poisonImmune) {
        this.poisonImmune = poisonImmune;
        return this;
    }

    public Spell setSpellImmune(boolean spellImmune) {
        this.spellImmune = spellImmune;
        return this;
    }

    public Spell setIsBully(boolean isBully) {
        this.isBully = isBully;
        return this;
    }

    public TargetUnitType getTargetUnitType() {
        return targetUnitType;
    }

    public void setIgnoreHoly(boolean ignoreHoly) {
        this.ignoreHoly = ignoreHoly;
    }

    TargetArea getTargetArea() {
        return targetArea;
    }

    int getGridX() {
        return gridX;
    }

    int getGridY() {
        return gridY;
    }

    TargetUnit getTargetUnit() {
        return targetUnit;
    }

    TargetType getTargetType() {
        return targetType;
    }

    public Buff[] getBuffs() {
        return buffs;
    }

    boolean canDispel() {
        return canDispel;
    }

    int getNumberOfRandomTargets() {
        return numberOfRandomTargets;
    }

    @Override
    public String toString() {
        if (buffs == null) {
            return "null buff";
        }
        String ans = "buffs : \n";
        for (Buff buff : buffs)
            ans = ans.concat(buff.toString());
        return ans;
    }

    public enum TargetType {
        CELL,
        UNIT;
    }

    public enum TargetArea {
        SELECTED_CELL,
        SELECTED_X_Y_GRID,
        ALL_OF_THE_MAP,
        ADJACENT_8,
        ADJACENT_4,
        SAME_ROW,
        ADJACENT_9,
        DISTANCE_2,
        SAME_COLUMN
    }

    public enum TargetUnit {
        UNIT,
        ENEMY_UNIT,
        FRIENDLY_UNIT,
        FRIENDLY_MINION,
        FRIENDLY_HERO,
        ENEMY_MINION,
        ENEMY_HERO,
        SELF
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

    public static class SpellBuilder {
        private TargetType targetType;
        private TargetArea targetArea;
        private TargetUnit targetUnit;
        private TargetUnitType targetUnitType;
        private int numberOfRandomTargets = MAX_GOALS;
        private int gridX = 1;
        private int gridY = 1;
        private Buff[] buffs;
        private boolean canDispel = false;

        public SpellBuilder setTargetType(TargetType targetType) {
            this.targetType = targetType;
            return this;
        }

        public SpellBuilder setTargetArea(TargetArea targetArea) {
            this.targetArea = targetArea;
            return this;
        }

        public SpellBuilder setTargetUnit(TargetUnit targetUnit) {
            this.targetUnit = targetUnit;
            return this;
        }

        public SpellBuilder setTargetUnitType(TargetUnitType targetUnitType) {
            this.targetUnitType = targetUnitType;
            return this;
        }

        public SpellBuilder setNumberOfRandomTargets(int numberOfRandomTargets) {
            this.numberOfRandomTargets = numberOfRandomTargets;
            return this;
        }

        public SpellBuilder setGridX(int gridX) {
            this.gridX = gridX;
            return this;
        }

        public SpellBuilder setGridY(int gridY) {
            this.gridY = gridY;
            return this;
        }

        public SpellBuilder setBuffs(Buff[] buffs) {
            this.buffs = buffs;
            return this;
        }

        public SpellBuilder setCanDispel(boolean canDispel) {
            this.canDispel = canDispel;
            return this;
        }

        public Spell build() {
            return new Spell(targetType, targetArea, targetUnit, targetUnitType, numberOfRandomTargets, gridX, gridY, buffs, canDispel);
        }
    }

}