package model;

import java.util.ArrayList;

public class Spell {
    protected Buff[] buffs;
    private boolean canDispel;

    // Main Constructor
    public Spell(Buff[] buffs,boolean canDispel) {
        this.buffs = buffs;
        this.canDispel = canDispel;
    }

    // Copy constructor
    public Spell(Spell spell) {
        this.buffs = spell.buffs;
        this.canDispel = spell.canDispel;
    }

    protected Spell() {
    }


    public static class SpellBuilder {
        Buff[] buffs;
        boolean canDispel = false;

        public SpellBuilder setBuffs(Buff[] buffs) {
            this.buffs = buffs;
            return this;
        }


        public SpellBuilder setCanDispel(boolean canDispel) {
            this.canDispel = canDispel;
            return this;
        }

        public Spell build() {
            return new Spell(buffs, canDispel);
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

