package menus;

import model.CollectionItem;

import java.util.ArrayList;

public class Shop extends Menu {
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
    private static ArrayList<CollectionItem> collectionItems;

    public static CollectionItem getCollectionItemByName(String collectionItemName) {
        for (CollectionItem collectionItem : collectionItems) {
            if (collectionItem.equalsName(collectionItemName))
                return collectionItem;
        }
        return null;
    }

    public static String[] help() {
        return commands;
    }

    public static ArrayList<String> search(String collectionItemName) {
        return new ArrayList<CollectionItem>(getCollectionItemByName(collectionItemName));
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
