package menus;

public class CustomGame extends Menu {
    private static final String[] commands = {
            "Start game [deck name] [mode] [number of flags]"
    };

    public static void help() {
        view.showHelp(commands);
        getAccount().getData().getDecks().forEach(o -> {
            if (o.isValid())
                view.showDeckInformation(o);
        });
    }
}
