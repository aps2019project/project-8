package menus;

import model.Game;

public class GraveyardMenu extends InGameMenu {
    private static String[] commands = {
            "-------Graveyard-------",
            "Show info [card id]",
            "Show cards",
            "Exit",
            "Help"
    };
    private static Game game;

    public static void setGame(Game game) {
        GraveyardMenu.game = game;
    }

    public static void help() {
        view.showHelp(commands);
    }

    public static void showInfo(String cardID) {
        game.showGraveYardInfoOfACard(cardID);
    }

    public static void showCards() {
        game.showGraveYardCards();
    }

    public static String getString() {
        return "";
    }
}
