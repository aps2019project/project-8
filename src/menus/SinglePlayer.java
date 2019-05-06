package menus;

public class SinglePlayer extends Menu {
    private static final String[] commands = {
            "-------Single Player Menu-------",
            "Story",
            "Custom game"
    };

    public static void help() {
        view.showHelp(commands);
    }
}
