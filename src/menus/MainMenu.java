package menus;

public class MainMenu extends Menu {
    private static final String[] commands = {
            "Collection",
            "Shop",
            "Battle",
            "Exit",
            "Help",
            "Logout",
            "Save"
    };

    public static String[] help() {
        return commands;
    }
}
