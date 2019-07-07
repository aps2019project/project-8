package menus;

public class ChatMenu extends Menu {
    private static final String[] commands = {
            "-------Chat Menu-------",
            "Send Message: [message]",
            "Get Messages",
            "Exit",
            "Help"
    };

    public static void help() {
        view.showHelp(commands);
    }

    public static void sendMessage(String message) {
        Menu.getConnection().sendChatMessage(getAccount().getName() + " : " + message);
    }

    public static void getMessages() {
        String[] messages = Menu.getConnection().getNewMessages();
        if (messages == null || messages.length == 0) {
            System.out.println("NO NEW MESSAGES WHERE FOUND!");
        } else {
            for (String s : messages) {
                System.out.println(s);
            }
        }
    }
}
