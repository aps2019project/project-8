package model;

public class Buff {
    private int duration;
    private int holy;
    private int poison;
    private int effectHp;
    private int effectAp;
    private boolean stun;
    private boolean disarm;
    private boolean dispellable;
    private int delay = 0;
    private Allegiance allegiance = Allegiance.NONE;

    // Constructor for BuffBuilder
    public Buff(int duration, int holy, int poison, int effectHp, int effectAp,
                boolean stun, boolean disarm, boolean dispellable) {
        this.duration = duration;
        this.holy = holy;
        this.poison = poison;
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
        this.poison = buff.poison;
        this.effectHp = buff.effectHp;
        this.effectAp = buff.effectAp;
        this.stun = buff.stun;
        this.disarm = buff.disarm;
        this.dispellable = buff.dispellable;
        this.delay = buff.delay;
    }

    public static Buff newPoisonBuff(int poison) {
        return new Buff(3, 0, poison, 0, 0, false, false, true);
    }

    public static Buff newFireBuff(int effectHp) {
        return new Buff(1000000, 0, 0, effectHp, 0, false, false, false);
    }

    public void decrementDuration() {
        delay--;
        duration--;
    }

    public int getDuration() {
        return duration;
    }

    int getHoly() {
        if (delay > 0)
            return 0;
        return holy;
    }

    int getPoison() {
        if (delay > 0)
            return 0;
        return poison;
    }

    int getEffectHp() {
        if (delay > 0)
            return 0;
        return effectHp;
    }

    int getEffectAp() {
        if (delay > 0)
            return 0;
        return effectAp;
    }

    public boolean canStun() {
        if (delay > 0)
            return false;
        return stun;
    }

    public boolean canDisarm() {
        if (delay > 0)
            return false;
        return disarm;
    }

    public boolean dispellable() {
        return dispellable;
    }

    public boolean isPositiveBuff() {
        if (stun || disarm)
            return false;
        return holy + effectHp + effectAp - poison > 0;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\tbuff duration: ");
        stringBuilder.append(duration);
        stringBuilder.append("\n");
        stringBuilder.append("\tbuff holy: ");
        stringBuilder.append(holy);
        stringBuilder.append("\n");
        stringBuilder.append("\tbuff effect hp: ");
        stringBuilder.append(effectHp);
        stringBuilder.append("\n");
        stringBuilder.append("\tbuff effect ap: ");
        stringBuilder.append(effectAp);
        stringBuilder.append("\n");
        stringBuilder.append("\tbuff poison: ");
        stringBuilder.append(poison);
        stringBuilder.append("\n");
        stringBuilder.append("\tbuff disarm: ");
        stringBuilder.append(disarm);
        stringBuilder.append("\n");
        stringBuilder.append("\tbuff stun: ");
        stringBuilder.append(stun);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public void setFriendly() {
        allegiance = Allegiance.FRIENDLY;
    }

    public void setEnemy() {
        allegiance = Allegiance.ENEMY;
    }

    public Allegiance getAllegiance() {
        return allegiance;
    }

    enum Allegiance {
        NONE,
        FRIENDLY,
        ENEMY
    }

    public static class BuffBuilder {
        private int duration = 0;
        private int holy = 0;
        private int poison = 0;
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

        public BuffBuilder setPoison(int poison) {
            this.poison = poison;
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
            return new Buff(duration, holy, poison, effectHp, effectAp, stun, disarm, dispellable);
        }
    }
}
