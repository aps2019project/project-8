package model;

import java.util.ArrayList;

public class Spell {

    public static final int MAX_GOALS = 100;

    public static enum TargetType {
        CELL,
        UNIT
    }
    public static enum TargetArea {
        SELECTED_1_1_GRID,
        SELECTED_2_2_GRID,
        SELECTED_3_3_GRID,
        ALL_OF_THE_MAP,
        ADJACENT_8,
        ADJACENT_4
    }
    public static enum TargetUnit {
        UNIT,
        ENEMY_UNIT,
        FRIENDLY_UNIT,
        FRIENDLY_MINION,
        FRIENDLY_HERO,
        ENEMY_MINION,
        ENEMY_HERO,
        SELF
    }

    public static enum TargetUnitType {
        ALL,
        MELEE,
        RANGED,
        HYBRID,
        MELLE_RANGED,
        MELEE_HYBRID,
        RANGED_HYBRID
    }

    private TargetType targetType;
    private TargetArea targetArea;
    private TargetUnit targetUnit;
    private TargetUnitType targetUnitType;
    private int numberOfRandomTargets;

    private Buff[] buffs;
    private boolean canDispel;

    // Main Constructor
    public Spell(TargetType targetType, TargetArea targetArea, TargetUnit targetUnit, TargetUnitType targetUnitType,
                 int numberOfRandomTargets, Buff[] buffs,boolean canDispel) {
        this.targetType = targetType;
        this.targetArea = targetArea;
        this.targetUnit = targetUnit;
        this.targetUnitType = targetUnitType;
        this.numberOfRandomTargets = numberOfRandomTargets;
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
        this.buffs = spell.buffs;
        this.canDispel = spell.canDispel;
    }

    protected Spell() {
    }


    public static class SpellBuilder {
        private TargetType targetType;
        private TargetArea targetArea;
        private TargetUnit targetUnit;
        private TargetUnitType targetUnitType;
        private int numberOfRandomTargets = MAX_GOALS;
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

        public SpellBuilder setBuffs(Buff[] buffs) {
            this.buffs = buffs;
            return this;
        }


        public SpellBuilder setCanDispel(boolean canDispel) {
            this.canDispel = canDispel;
            return this;
        }

        public Spell build() {
            return new Spell(targetType, targetArea, targetUnit, targetUnitType, numberOfRandomTargets, buffs, canDispel);
        }
    }


    public void cast(int x, int y, Map map, Player player) {

    }

    @Override
    public String toString() {
        String ans = new String("");
        return ans;
    }
}

