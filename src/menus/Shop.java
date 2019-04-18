package menus;

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

    public static void search(String name) {
    }

    public static void buyCollectionItem(String name) {
    }

    public static void sellCollectionItem(int collectionItemID) {
    }

    public static String getString() {
        return "";
    }
}
