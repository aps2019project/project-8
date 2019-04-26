package menus;

import model.CollectionItem;

import java.util.ArrayList;

public class Shop extends Menu {
/*    private ArrayList<CollectionItem> collectionItems;

    public CollectionItem getCollectionItem(String name) {
        return null;
    }*/

    private static final String[] commands = {
            "exit",
            "show collection",
            "search [item name | card name]",
            "search collection [item name | card name]",
            "buy [card name | item name]",
            "sell [card id]",
            "show",
            "help"
    };

    public static String[] help() {
        return commands;
    }

    public static ArrayList<String> search(String collectionItemName) {
        return null;
    }

    public static void buyCollectionItem(String name) {
    }

    public static void sellCollectionItem(String collectionItemID) {
    }

    public static String getString() {
        return "";
    }

    public static boolean hasCollectionItem(String collectionItemName) {
        return false;
    }

    public static int getPrice(String collectionItemName) {
        return 0;
    }

    public static boolean isItem(String collectionItemName) {
        return false;
    }

    public static void buy(String collectionItemName) {
    }

    public static ArrayList<CollectionItem> show() {
        return null;
    }
}
