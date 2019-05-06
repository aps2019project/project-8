package menus;

public class GraveyardMenu extends InGameMenu {
    private static String[] commands = {
            "-------Graveyard-------",
            "Exit",
            "Help"
    };

    public static void help() {
        view.showHelp(commands);
    }

    public static void showInfo(String cardID) {
    }

    public static void showCards() {
    }

    public static String getString() {
        return "";
    }

    public static void showMenu() {
    }
}
