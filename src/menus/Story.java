package menus;

import model.AI;

import java.util.ArrayList;

public class Story extends Menu {
    public static String[] help() {
        ArrayList<AI> ais = AI.getAIs();

        //
        if (ais == null) {
            return null;
        }
        //

        String[] levels = new String[ais.size()];
        for (int i = 0; i < ais.size(); i++) {
            AI ai = ais.get(i);
            levels[i] = ai.getDeck().getHero().getName() + " : " + ai.getMode();
        }
        return levels;
    }
}
