package menus;

public class CustomGame extends Menu {
    private static final String[] commands = {
            "Start game [deck name] [mode] [number of flags]"
    };

    public static String[] help() {
        return commands;
    }
}
