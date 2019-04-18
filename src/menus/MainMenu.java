package menus;

import java.io.FileWriter;
import java.io.IOException;

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

    public static void save() {
        try {
            FileWriter out = new FileWriter("./save/" + account.getName() + ".txt", false);
            out.write(account.getSaveData());
            out.flush();
        } catch (IOException ignored) {
            System.out.println("AAAA");
        }
    }
}
