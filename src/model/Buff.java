package model;

import java.util.ArrayList;

public class Buff {
    private int duration;
    private int holy;
    private int powerHp;
    private int powerAp;
    private int poison;
    private int weaknessAP;
    private int weaknessHP;
    private boolean stun;
    private boolean disarm;
    private int unholy;
    private boolean dispellable;

    private int githubtest;

    // Constructor for BuffBuilder
    public Buff(int duration, int holy, int powerHp, int powerAp, int poison, int weaknessAP, int weaknessHP,
                boolean stun, boolean disarm,int unholy, boolean dispellable) {
        this.duration = duration;
        this.holy = holy;
        this.powerHp = powerHp;
        this.powerAp = powerAp;
        this.poison = poison;
        this.weaknessAP = weaknessAP;
        this.weaknessHP = weaknessHP;
        this.stun = stun;
        this.disarm = disarm;
        this.unholy = unholy;
        this.dispellable = dispellable;
    }

    public static class BuffBuilder {
        private int duration = 0;
        private int holy = 0;
        private int powerHp = 0;
        private int powerAp = 0;
        private int poison = 0;
        private int weaknessAP = 0;
        private int weaknessHP = 0;
        private boolean stun = false;
        private boolean disarm = false;
        private int unholy = 0;
        private boolean dispellable = false;

        public BuffBuilder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public BuffBuilder setHoly(int holy) {
            this.holy = holy;
            return this;
        }
        public BuffBuilder setPowerHp(int powerHp) {
            this.powerHp = powerHp;
            return this;
        }
        public BuffBuilder setPowerAp(int powerAp) {
            this.powerAp = powerAp;
            return this;
        }
        public BuffBuilder setPoison(int poison) {
            this.poison = poison;
            return this;
        }
        public BuffBuilder setWeaknessAP(int weaknessAP) {
            this.weaknessAP = weaknessAP;
            return this;
        }
        public BuffBuilder setWeaknessHP(int weaknessHP) {
            this.weaknessHP = weaknessHP;
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
        public BuffBuilder setUnholy(int unholy) {
            this.unholy = unholy;
            return this;
        }
        public BuffBuilder setDispellable(boolean dispellable) {
            this.dispellable = dispellable;
            return this;
        }
        public Buff build() {
            return new Buff(duration, holy, powerHp, powerAp, poison, weaknessAP, weaknessHP, stun, disarm, unholy, dispellable);
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
