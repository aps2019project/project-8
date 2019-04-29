package menus;

import com.google.gson.Gson;
import model.Card;
import model.CollectionItem;
import model.Item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    private static ArrayList<CollectionItem> collectionItems = new ArrayList<>();

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

    public static String search(String collectionItemName) {
        return getCollectionItemByName(collectionItemName).getName();
    }

    public static void sellCollectionItem(String collectionItemID) {
        CollectionItem collectionItem = account.getCollection().getCollectionItemByID(collectionItemID);
        account.receiveMoney(collectionItem.getPrice());
        account.getCollection().removeCollectionItem(collectionItemID);
    }

    public static String getString() {
        return "";
    }

    public static boolean hasCollectionItem(String collectionItemName) {
        return getCollectionItemByName(collectionItemName) != null;
    }

    public static int getPrice(String collectionItemName) {
        return getCollectionItemByName(collectionItemName).getPrice();
    }

    public static boolean isItem(String collectionItemName) {
        return getCollectionItemByName(collectionItemName) instanceof Item;
    }

    public static void buy(String collectionItemName) {
        CollectionItem collectionItem = getCollectionItemByName(collectionItemName);
        account.payMoney(collectionItem.getPrice());
        account.getCollection().addCollectionItem(collectionItem);
    }

    public static ArrayList<CollectionItem> show() {
        return collectionItems;
    }

    public static void load() {
        try {
            for (File file : new File("./gameData/cards").listFiles()) {
                Gson gson = new Gson();
                collectionItems.add(new Card(gson.fromJson(new BufferedReader(new FileReader(file)).readLine(),
                        Card.class)));
            }
        } catch (IOException ignored) {}
    }
}
