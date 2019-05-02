package menus;

import com.gilecode.yagson.YaGson;
import model.*;

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

    private static CollectionItem getCollectionItemByName(String collectionItemName) {
        for (CollectionItem collectionItem : collectionItems) {
            if (collectionItem.equalsName(collectionItemName))
                return collectionItem;
        }
        return null;
    }

    public static void help() {
        view.showHelp(commands);
    }

    public static void search(String collectionItemName) {
        CollectionItem collectionItem = getCollectionItemByName(collectionItemName);
        if (collectionItem == null) {
            view.showNoSuchCollectionItemError();
            return;
        }
        view.showName(collectionItem.getName());
    }

    public static void sellCollectionItem(String collectionItemID) {
        if (CollectionMenu.hasCollectionItem(collectionItemID)) {
            CollectionItem collectionItem = account.getCollection().getCollectionItemByID(collectionItemID);
            account.receiveMoney(collectionItem.getPrice());
            account.getCollection().removeCollectionItem(collectionItemID);
            view.alertSell();
            return;
        }
        view.showNoSuchCollectionItemError();
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
        if (!hasCollectionItem(collectionItemName)) {
            view.showNoSuchCollectionItemError();
            return;
        }
        if (getAccount().getMoney() < getPrice(collectionItemName)) {
            view.showNotEnoughMoneyError();
            return;
        }
        if (getAccount().hasThreeItems() && isItem(collectionItemName)) {
            view.showFourthItemError();
            return;
        }
        CollectionItem collectionItem = getCollectionItemByName(collectionItemName);
        account.payMoney(collectionItem.getPrice());
        account.getCollection().addCollectionItem(collectionItem);
        view.alertBuy();
    }

    public static void show() {
        view.showShop(collectionItems);
    }

    public static void load() {
        try {
            for (File file : new File("./gameData/usables").listFiles()) {
                YaGson yaGson = new YaGson();
                collectionItems.add(yaGson.fromJson(new BufferedReader(new FileReader(file)).readLine(), Usable.class));
            }
            for (File file : new File("./gameData/heroes").listFiles()) {
                YaGson yaGson = new YaGson();
                collectionItems.add(yaGson.fromJson(new BufferedReader(new FileReader(file)).readLine(), Hero.class));
            }
        } catch (Exception ignored) {}
    }
}
