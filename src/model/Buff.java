package model;

import java.util.ArrayList;

public class Buff {
    private int duration;
    private int holy;
    private int effectHp;
    private int effectAp;
    private boolean stun;
    private boolean disarm;
    private boolean dispellable;

    // Constructor for BuffBuilder
    public Buff(int duration, int holy, int effectHp, int effectAp,
                boolean stun, boolean disarm, boolean dispellable) {
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
        this.duration = buff.duration;
        this.holy = buff.holy;
        this.effectHp = buff.effectHp;
        this.effectAp = buff.effectAp;
        this.stun = buff.stun;
        this.disarm = buff.disarm;
        this.dispellable = buff.dispellable;
    }

    public static class BuffBuilder {
        private int duration = 0;
        private int holy = 0;
        private int effectHp = 0;
        private int effectAp = 0;
        private boolean stun = false;
        private boolean disarm = false;
        private boolean dispellable = false;

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
            return new Buff(duration, holy, effectHp, effectAp, stun, disarm, dispellable);
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
