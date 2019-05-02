package menus;

import model.AI;

public class Story extends Menu {
    public static void help() {
        AI.getAIs().forEach(view::showAIDeckInformation);
    }
}
