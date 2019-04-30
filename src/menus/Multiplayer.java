package menus;

public class Multiplayer extends Menu {
    private static final String[] commands = {
            "Select user [user name]",
            "Start multiplayer game [mode] [number of flags]"
    };

    public static void help() {
        view.showHelp(commands);
    }
}
