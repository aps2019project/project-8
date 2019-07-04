package menus;

import model.AI;

public class Story extends Menu {
    public static void help() {
        view.showStoryModes(AI.getDecks(), AI.getGameMode());
    }
}
