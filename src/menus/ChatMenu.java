package menus;

public class ChatMenu extends Menu {
    private static final String[] commands = {
            "-------Chat Menu-------",
            "Send Message [message]",
            "Exit",
            "Help"
    };

    public static void help() {
        view.showHelp(commands);
    }

    public static void sendMessage(String message) {
        Menu.getConnection().sendChatMessage(message);
        System.err.println(getAccount() + " : " + message);
    }
}
