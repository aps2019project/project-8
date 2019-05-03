package model;

import java.util.ArrayList;

public class Spell {
    SpellTarget spellTarget;
    protected Buff[] buffs;
    private boolean canDispel;

    // Main Constructor
    public Spell(SpellTarget spellTarget,Buff[] buffs,boolean canDispel) {
        this.spellTarget = spellTarget;
        this.buffs = buffs;
        this.canDispel = canDispel;
    }

    // Copy constructor
    public Spell(Spell spell) {
        this.spellTarget = spell.spellTarget;
        this.buffs = spell.buffs;
        this.canDispel = spell.canDispel;
    }

    protected Spell() {
    }


    public static class SpellBuilder {
        SpellTarget spellTarget = null;
        Buff[] buffs;
        boolean canDispel = false;

        public SpellBuilder setBuffs(Buff[] buffs) {
            this.buffs = buffs;
            return this;
        }

        public SpellBuilder setSpellTarget(SpellTarget spellTarget) {
            this.spellTarget = spellTarget;
            return this;
        }

        public SpellBuilder setCanDispel(boolean canDispel) {
            this.canDispel = canDispel;
            return this;
        }

        public Spell build() {
            return new Spell(spellTarget, buffs, canDispel);
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

