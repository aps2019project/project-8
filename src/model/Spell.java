package model;

import java.util.ArrayList;

public class Spell {
    protected Buff buff;
    protected boolean enemyMinion;
    protected boolean enemyHero;
    protected boolean friendlyMinion;
    protected boolean friendlyHero;
    private boolean global;
    protected int attack;
    protected int heal;
    protected boolean dispel;
    private boolean adjacent;
    private Unit unitAdjacency;

    private Faction comboAplifier;
    private int addMana;
    private int addRange;

    public void cast(int x, int y, Map map, Player player) {

    }

    public void amplify(ArrayList<Card> friendlyCards) {

    }

    @Override
    public String toString() {
        String ans = new String("");
        return ans;
    }

=======
public class Spell {
>>>>>>> origin/master
}
