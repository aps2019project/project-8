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
        System.err.println("-- send message invoked! --");
        Menu.getConnection().sendChatMessage(message);
    }

    public static void getMessages() {
        System.err.println("-- get messages invoked! --");
        String[] messages = Menu.getConnection().getNewMessages();
        for (String s: messages)
            System.out.println(s);
    }
}
