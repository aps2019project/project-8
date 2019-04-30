package model;

import java.util.ArrayList;

public class Buff {
    private int duration;
    private int holy;
    private int power;
    private int poison;
    private int weaknessAP;
    private int weaknessHP;
    private boolean stun;
    private boolean disarm;
    private int unholy;

    private int githubtest;

    // Constructor for BuffBuilder
    public Buff(int duration, int holy, int power, int poison, int weaknessAP, int weaknessHP, boolean stun, boolean disarm,int unholy) {
        this.duration = duration;
        this.holy = holy;
        this.power = power;
        this.poison = poison;
        this.weaknessAP = weaknessAP;
        this.weaknessHP = weaknessHP;
        this.stun = stun;
        this.disarm = disarm;
        this.unholy = unholy;
    }

    public static class BuffBuilder {
        private int duration = 0;
        private int holy = 0;
        private int power = 0;
        private int poison = 0;
        private int weaknessAP = 0;
        private int weaknessHP = 0;
        private boolean stun = false;
        private boolean disarm = false;
        private int unholy = 0;

        public BuffBuilder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public BuffBuilder setHoly(int holy) {
            this.holy = holy;
            return this;
        }
        public BuffBuilder setPower(int power) {
            this.power = power;
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
        public Buff build() {
            return new Buff(duration, holy, power, poison, weaknessAP, weaknessHP, stun, disarm, unholy);
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
