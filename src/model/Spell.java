package model;

import java.util.ArrayList;

public class Spell {
    SpellTarget spellTarget;
    protected Buff buff;
    private boolean global;
    protected int attack;
    protected int heal;
    protected boolean dispel;
    private boolean adjacent;

    private int addMana;
    private int addRange;

    protected Spell() {

    }
    public Spell(SpellTarget spellTarget,Buff buff,boolean global,int attack,int heal,boolean dispel,
                 boolean adjacent,int addMana,int addRange) {
        this.spellTarget = spellTarget;
        this.buff = buff;
        this.global = global;
        this.attack = attack;
        this.heal = heal;
        this.dispel = dispel;
        this.adjacent = adjacent;
        this.addMana = addMana;
        this.addRange = addRange;
    }

    public static class SpellBuilder {
        SpellTarget spellTarget;
        private Buff buff;
        private boolean global = false;
        private int attack = 0;
        private int heal = 0;
        private boolean dispel = false;
        private boolean adjacent = false;

        private int addMana = 0;
        private int addRange = 0;

        public SpellBuilder setBuff(Buff buff) {
            this.buff = buff;
            return this;
        }

        public SpellBuilder setSpellTarget(SpellTarget spellTarget) {
            this.spellTarget = spellTarget;
            return this;
        }

        public SpellBuilder setGlobal(boolean global) {
            this.global = global;
            return this;
        }

        public SpellBuilder setAttack(int attack) {
            this.attack = attack;
            return this;
        }

        public SpellBuilder setHeal(int heal) {
            this.heal = heal;
            return this;
        }

        public SpellBuilder setDispel(boolean dispel) {
            this.dispel = dispel;
            return this;
        }

        public SpellBuilder setAdjacent(boolean adjacent) {
            this.adjacent = adjacent;
            return this;
        }
        public SpellBuilder setAddMana(int addMana) {
            this.addMana = addMana;
            return this;
        }

        public SpellBuilder setAddRange(int addRange) {
            this.addRange = addRange;
            return this;
        }

        public Spell build() {
            return new Spell(spellTarget, buff, global, attack, heal, dispel, adjacent, addMana, addRange);
        }
    }


    public void cast(int x, int y, Map map, Player player) {

    }

    public void amplify(ArrayList<Card> friendlyCards) {

    }

    @Override
    public String toString() {
        String ans = new String("");
        return ans;
    }
}

