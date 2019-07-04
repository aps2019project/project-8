package menus;

public class Battle extends Menu {
/*    public static Game makeSinglePlayerGame() {
        return null;
    }

    public static Game makeStoryModeGame() {
        return null;
    }

    public static Game makeCustomGame() {
        return null;
    }

    public static Game makeMultiplayerGame() {
        return null;
    }*/

    private static final String[] commands = {
            "-------Battle Menu-------",
            "Single player",
            "Multiplayer"
    };

    public static void help() {
        view.showHelp(commands);
    }

    public static String getString() {
        return "";
    }
}
