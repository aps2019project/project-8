package interfaces;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

//project-8 Server.iml

public class ShopInterface {

    private ArrayList<CollectionItem> collectionItems = new ArrayList<>();
    private ArrayList<Collectible> collectibles = new ArrayList<>();

    public ShopInterface() {
        load();
//        for (CollectionItem collectionItem : collectionItems) {
//            collectionItem.setCount(6);
//            saveData(collectionItem);
//        }
//
//        for (Collectible collectible : collectibles) {
//            collectible.setCount(6);
//            saveData(collectible);
//        }
    }

//    public ArrayList<CollectionItem> getCollectionItems() {
//        return collectionItems;
//    }

    private CollectionItem getCollectionItemByName(String collectionItemName) {
        for (CollectionItem collectionItem : collectionItems) {
            if (collectionItem.equalsName(collectionItemName))
                return collectionItem;
        }
        return null;
    }

//    public void search(String collectionItemName) {
//        CollectionItem collectionItem = getCollectionItemByName(collectionItemName);
//        if (collectionItem == null) {
//            view.showNoSuchCollectionItemError();
//            return;
//        }
//        view.showName(collectionItem.getName());
//        view.showInfoOfCollectionItem(collectionItem);
//    }
//

    public String sell(String collectionItemID, AccountUser accountUser) {
        if (accountUser.getData().getCollection().getCollectionItemByID(collectionItemID) != null) {
            CollectionItem collectionItem = accountUser.getData().getCollection().getCollectionItemByID(collectionItemID);
            accountUser.receiveMoney(collectionItem.getPrice());
            accountUser.getData().getCollection().removeCollectionItem(collectionItemID);
            accountUser.saveAccount();
            return "you successfully sold this item";
        }
        return "no collection item with id " + collectionItemID;
    }

//    public String getString() {
//        return "";
//    }
//
//    public boolean hasCollectionItem(String collectionItemName) {
//        return getCollectionItemByName(collectionItemName) != null;
//    }
//
//    public int getPrice(String collectionItemName) {
//        return getCollectionItemByName(collectionItemName).getPrice();
//    }
//
//    public boolean isItem(String collectionItemName) {
//        return getCollectionItemByName(collectionItemName) instanceof Item;
//    }



    public String buy(String collectionItemName, AccountUser accountUser) {
        CollectionItem collectionItem = getCollectionItemByName(collectionItemName);
        if (collectionItem == null) {
            return "no collection item with name " + collectionItemName;
        }
        if (collectionItem.getCount() <= 0) {
            return "shop has ran out of " + collectionItemName;
        }
        if (accountUser.getMoney() < collectionItem.getPrice()) {
            return "not enough money";
        }
        if (accountUser.getData().hasThreeItems() && collectionItem instanceof Item) {
            return "you already have three items! you can't have more!";
        }
        collectionItem.decreaseCount();
        saveData(collectionItem);
        accountUser.payMoney(collectionItem.getPrice());
        accountUser.getData().getCollection().addCollectionItem(getCopy(collectionItem));
        accountUser.saveAccount();
        return "you successfully bought " + collectionItemName + " for " + collectionItem.getPrice() + "$";
    }

    private CollectionItem getCopy(CollectionItem collectionItem) {
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

    public String getItemCount(String collectionItemName) {
        CollectionItem collectionItem = getCollectionItemByName(collectionItemName);
        if (collectionItem == null)
            return "no collection item with name " + collectionItemName;
        return String.valueOf(collectionItem.getCount());
    }

//    public void show() {
//        view.showShop(collectionItems);
//    }

    private void load() {
        collectibles.clear();
        collectionItems.clear();
        try {
            for (File file : Objects.requireNonNull(new File("./database/CollectionItems/Collectibles/").listFiles())) {
                YaGson yaGson = new YaGson();
                collectibles.add(yaGson.fromJson(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())),
                        StandardCharsets.UTF_8), Collectible.class));
            }
            for (File file : Objects.requireNonNull(new File("./database/CollectionItems/Heroes/").listFiles())) {
                YaGson yaGson = new YaGson();
                collectionItems.add(yaGson.fromJson(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())),
                        StandardCharsets.UTF_8), Hero.class));
            }
            for (File file : Objects.requireNonNull(new File("./database/CollectionItems/Minions/").listFiles())) {
                YaGson yaGson = new YaGson();
                collectionItems.add(yaGson.fromJson(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())),
                        StandardCharsets.UTF_8), Minion.class));
            }
            for (File file : Objects.requireNonNull(new File("./database/CollectionItems/SpellCards/").listFiles())) {
                YaGson yaGson = new YaGson();
                collectionItems.add(yaGson.fromJson(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())),
                        StandardCharsets.UTF_8), SpellCard.class));
            }
            for (File file : Objects.requireNonNull(new File("./database/CollectionItems/Usables/").listFiles())) {
                YaGson yaGson = new YaGson();
                collectionItems.add(yaGson.fromJson(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())),
                        StandardCharsets.UTF_8), Usable.class));
            }
        } catch (Exception ignored) {}
    }

    public void saveData(CollectionItem collectionItem) {
        try {
            YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
            FileWriter out;
            if (collectionItem instanceof Hero) {
                out = new FileWriter("./database/CollectionItems/Heroes/" + collectionItem.getName() + ".json", false);
                out.write(yaGson.toJson(collectionItem, Hero.class));
            } else if (collectionItem instanceof SpellCard) {
                out = new FileWriter("./database/CollectionItems/SpellCards/" + collectionItem.getName() + ".json", false);
                out.write(yaGson.toJson(collectionItem, SpellCard.class));
            } else if (collectionItem instanceof Minion) {
                out = new FileWriter("./database/CollectionItems/Minions/" + collectionItem.getName() + ".json", false);
                out.write(yaGson.toJson(collectionItem, Minion.class));
            } else if (collectionItem instanceof Usable) {
                out = new FileWriter("./database/CollectionItems/Usables/" + collectionItem.getName() + ".json", false);
                out.write(yaGson.toJson(collectionItem, Usable.class));
            } else if (collectionItem instanceof Collectible) {
                out = new FileWriter("./database/CollectionItems/Collectibles/" + collectionItem.getName() + ".json", false);
                out.write(yaGson.toJson(collectionItem, Collectible.class));
            } else {
                System.err.println("object given is not an instance of an appropriate class");
                throw new IOException();
            }
            out.flush();
        } catch (IOException ignored) {
        }
    }

//    public void showCollection() {
//        view.showShopCollection(getAccount().getData().getCollectionItems());
//    }
}
