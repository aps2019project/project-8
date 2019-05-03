package model;

import java.util.ArrayList;

public class Buff {
    public static final int MAX_GRID_SIZE = 100;

    public static enum BuffTargetType {
        CELL,
        UNIT
    }
    public static enum BuffTargetArea {
        SELECTED_1_1_GRID,
        SELECTED_2_2_GRID,
        SELECTED_3_3_GRID,
        ALL_OF_THE_MAP,
        ADJACENT_8,
        ADJACENT_4
    }
    public static enum BuffTargetUnit {
        ENEMY_UNIT,
        FRIENDLY_UNIT,
        FRIENDLY_MINION,
        FRIENDLY_HERO,
        ENEMY_MINION,
        ENEMY_HERO,
        SELF
    }

    private BuffTargetType buffTargetType;
    private BuffTargetArea buffTargetArea;
    private BuffTargetUnit buffTargetUnit;
    private int numberOfRandomTargets;
    private int duration;
    private int holy;
    private int effectHp;
    private int effectAp;
    private boolean stun;
    private boolean disarm;
    private boolean dispellable;

    // Constructor for BuffBuilder
    public Buff(BuffTargetType buffTargetType, BuffTargetArea buffTargetArea, BuffTargetUnit buffTargetUnit,
                int numberOfRandomTargets, int duration, int holy, int effectHp, int effectAp,
                boolean stun, boolean disarm, boolean dispellable) {
        this.buffTargetType = buffTargetType;
        this.buffTargetArea = buffTargetArea;
        this.buffTargetUnit = buffTargetUnit;
        this.numberOfRandomTargets = numberOfRandomTargets;
        this.duration = duration;
        this.holy = holy;
        this.effectHp = effectHp;
        this.effectAp = effectAp;
        this.stun = stun;
        this.disarm = disarm;
        this.dispellable = dispellable;
    }

    // Copy constructor
    public Buff(Buff buff) {
        this.buffTargetType = buff.buffTargetType;
        this.buffTargetArea = buff.buffTargetArea;
        this.buffTargetUnit = buff.buffTargetUnit;
        this.numberOfRandomTargets = buff.numberOfRandomTargets;
        this.duration = buff.duration;
        this.holy = buff.holy;
        this.effectHp = buff.effectHp;
        this.effectAp = buff.effectAp;
        this.stun = buff.stun;
        this.disarm = buff.disarm;
        this.dispellable = buff.dispellable;
    }

    public static class BuffBuilder {
        private BuffTargetType buffTargetType;
        private BuffTargetArea buffTargetArea;
        private BuffTargetUnit buffTargetUnit;
        private int numberOfRandomTargets = MAX_GRID_SIZE;
        private int duration = 0;
        private int holy = 0;
        private int effectHp = 0;
        private int effectAp = 0;
        private boolean stun = false;
        private boolean disarm = false;
        private boolean dispellable = false;

        public BuffBuilder setBuffTargetType(BuffTargetType buffTargetType) {
            this.buffTargetType = buffTargetType;
            return this;
        }

        public BuffBuilder setBuffTargetArea(BuffTargetArea buffTargetArea) {
            this.buffTargetArea = buffTargetArea;
            return this;
        }

        public BuffBuilder setBuffTargetUnit(BuffTargetUnit buffTargetUnit) {
            this.buffTargetUnit = buffTargetUnit;
            return this;
        }

        public BuffBuilder setNumberOfRandomTargets(int numberOfRandomTargets) {
            this.numberOfRandomTargets = numberOfRandomTargets;
            return this;
        }

        public BuffBuilder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public BuffBuilder setHoly(int holy) {
            this.holy = holy;
            return this;
        }
        public BuffBuilder setEffectHp(int effectHp) {
            this.effectHp = effectHp;
            return this;
        }
        public BuffBuilder setEffectAp(int effectAp) {
            this.effectAp = effectAp;
            return this;
        }
        public BuffBuilder setStun(boolean stun) {
            this.stun = stun;
            return this;
        }
        public BuffBuilder setDisarm(boolean disarm) {
            this.disarm = disarm;
            return this;
        }
        public BuffBuilder setDispellable(boolean dispellable) {
            this.dispellable = dispellable;
            return this;
        }
        public Buff build() {
            return new Buff(buffTargetType, buffTargetArea, buffTargetUnit, numberOfRandomTargets,
                    duration, holy, effectHp, effectAp, stun, disarm, dispellable);
        }
    }

    public void decrementDuration() {
        duration--;
        if (duration < 0) { // ?

        }
    }

    ArrayList<Integer> getBuff() {
        ArrayList<Integer> characteristics;
        characteristics = new ArrayList<Integer>(0);
//        characteristics.add(duration);
//        characteristics.add()
        return characteristics;
    }

}
