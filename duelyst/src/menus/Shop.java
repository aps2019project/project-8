package menus;

import com.gilecode.yagson.YaGson;
import com.sun.org.apache.xml.internal.security.exceptions.AlgorithmAlreadyRegisteredException;
import model.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

public class Shop extends Menu {
    private static final String[] commands = {
            "-------Shop Menu-------",
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
    private static ArrayList<Collectible> collectibles = new ArrayList<>();

    public static ArrayList<CollectionItem> getCollectionItems() {
        return collectionItems;
    }

    public static CollectionItem getCollectionItemByName(String collectionItemName) {
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
        view.showInfoOfCollectionItem(collectionItem);
    }

    public static String sellCollectionItem(String collectionItemID) {
        String log = connection.tradeCollectionItem(collectionItemID, true);
        account = connection.getAccount();
        return log;
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

    public static String buy(String collectionItemName) {
        if (!hasCollectionItem(collectionItemName)) {
            view.showNoSuchCollectionItemError();
            return "no such collection item";
        }
        String log = connection.tradeCollectionItem(collectionItemName, false);
        if (log.contains("successfully")) {
//           getAccount().getData().getCollection().addCollectionItem(getCopy(getCollectionItemByName(collectionItemName)));
            Menu.setAccount(connection.getAccount());
        }
        return log;
//
//        if (getAccount().getMoney() < getPrice(collectionItemName)) {
//            view.showNotEnoughMoneyError();
//            return "not enough money";
//        }
//        if (getAccount().getData().hasThreeItems() && isItem(collectionItemName)) {
//            view.showFourthItemError();
//            return "you already have three items! you can't have more!";
//        }
//        CollectionItem collectionItem = getCollectionItemByName(collectionItemName);
//        account.payMoney(collectionItem.getPrice());
//        getAccount().getData().getCollection().addCollectionItem(getCopy(collectionItem));
//        view.alertBuy();
    }

    public static CollectionItem getCopy(CollectionItem collectionItem) {
        if (collectionItem instanceof Hero)
            return new Hero((Hero)collectionItem);
        if (collectionItem instanceof Minion)
            return new Minion((Minion) collectionItem);
        if (collectionItem instanceof SpellCard)
            return new SpellCard((SpellCard) collectionItem);
        if (collectionItem instanceof Usable)
            return new Usable((Usable) collectionItem);
        return null;
    }

    public static void show() {
        view.showShop(collectionItems);
    }

    public static void load() {
        collectibles.clear();
        collectionItems.clear();
        try {
            for (File file : new File("./gameData/Collectibles/").listFiles()) {
                YaGson yaGson = new YaGson();
                collectibles.add(yaGson.fromJson(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())),
                        StandardCharsets.UTF_8), Collectible.class));
            }
            for (File file : new File("./gameData/Heroes/").listFiles()) {
                YaGson yaGson = new YaGson();
                collectionItems.add(yaGson.fromJson(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())),
                        StandardCharsets.UTF_8), Hero.class));
            }
            for (File file : new File("./gameData/Minions/").listFiles()) {
                YaGson yaGson = new YaGson();
                collectionItems.add(yaGson.fromJson(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())),
                        StandardCharsets.UTF_8), Minion.class));
            }
            for (File file : new File("./gameData/SpellCards/").listFiles()) {
                YaGson yaGson = new YaGson();
                collectionItems.add(yaGson.fromJson(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())),
                        StandardCharsets.UTF_8), SpellCard.class));
            }
            for (File file : new File("./gameData/Usables/").listFiles()) {
                YaGson yaGson = new YaGson();
                collectionItems.add(yaGson.fromJson(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())),
                        StandardCharsets.UTF_8), Usable.class));
            }
        } catch (Exception ignored) {}
    }

    public static void showCollection() {
        view.showShopCollection(getAccount().getData().getCollectionItems());
    }
}