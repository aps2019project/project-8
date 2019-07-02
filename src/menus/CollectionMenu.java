package menus;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CollectionMenu extends Menu {
    private static final String[] commands = {
            "-------Collection Menu-------",
            "exit",
            "show",
            "search [card name | item name]",
            "save",
            "create deck [deck name]",
            "delete deck [deck name]",
            "add [card id | hero id] to deck [deck name]",
            "remove [card id | hero id] from deck [deck name]",
            "validate deck [deck name]",
            "select deck [deck name]",
            "show all decks",
            "show deck [deck name]",
            "help"
    };

    public static void help() {
        view.showHelp(commands);
    }

    public static void show() {
        view.showCollection(getAccount().getData().getCollectionItems());
    }

    public static void search(String collectionItemName) {
        view.showID(account.getData().getCollection().getCollectionItemIDs(collectionItemName));
    }

    public static void createDeck(String name) {
        if (getAccount().getData().getCollection().hasDeck(name)) {
            view.showAlreadyExistingDeckError();
            return;
        }
        getAccount().getData().getCollection().createDeck(name);
        view.alertDeckCreation();
    }

    public static void deleteDeck(String name) {
        if (!getAccount().getData().getCollection().hasDeck(name)) {
            view.showDeckDoesNotExistError();
            return;
        }
        getAccount().getData().getCollection().deleteDeck(name);
        view.alertDeckDeletion();
    }

    private static Deck getDeck(String deckName) { // if successful returns false
        if (!getAccount().getData().getCollection().hasDeck(deckName)) {
            view.showDeckDoesNotExistError();
            return null;
        }
        return getAccount().getData().getCollection().getDeck(deckName);
    }

    private static CollectionItem getCollectionItem(String collectionItemID) {
        if (!getAccount().getData().getCollection().hasCollectionItem(collectionItemID)) {
            view.showNoSuchCollectionItemError();
            return null;
        }
        return getAccount().getData().getCollection().getCollectionItem(collectionItemID);
    }

    public static void addCollectionItem(String deckName, String collectionItemID) {
        Deck deck = getDeck(deckName);
        CollectionItem collectionItem = getCollectionItem(collectionItemID);
        if (deck == null || collectionItem == null) {
            return;
        }

        if (deck.hasCollectionItem(collectionItemID)) {
            view.showDeckAlreadyHasCollectionItemError();
            return;
        }

        if (collectionItem instanceof Hero && deck.hasHero()) {
            view.showAddingASecondHeroToDeckError();
            return;
        }

        if (!(collectionItem instanceof Hero) && collectionItem instanceof Card && deck.isFull()) {
            view.showDeckIsFullError();
            return;
        }

        if (collectionItem instanceof Item && deck.hasItem()) {
            view.showAddingASecondItemToDeckError();
            return;
        }
        deck.addCollectionItem(collectionItem, collectionItemID);
        view.alertCollectionItemAddedToDeck();
    }

    public static void removeCard(String deckName, String collectionItemID) {
        Deck deck = getDeck(deckName);
        CollectionItem collectionItem = getCollectionItem(collectionItemID);
        if (deck == null || collectionItem == null) {
            return;
        }
        if (!deck.hasCollectionItem(collectionItemID)) {
            view.showDeckHasNoSuchCollectionItemError();
            return;
        }

        deck.removeCollectionItem(collectionItem, collectionItemID);
        view.alertCollectionItemRemovedFromDeck();
    }

    public static void validateDeck(String deckName) {
        Deck deck = getDeck(deckName);
        if (deck == null)
            return;
        if (deck.hasHero() && deck.isFull()) {
            view.alertValidDeck();
            return;
        }
        view.showInvalidDeckError();
    }

    public static void showAllDecks() {
        view.showDecks(getAccount().getData().getCollection().getDecks());
    }

    public static void showDeck(String deckName) {
        Deck deck = getDeck(deckName);
        if (deck == null)
            return;
        view.showDeck(deck);
    }

    public static String getString() {
        return "";
    }

    public static boolean hasCollectionItem(String collectionItemID) {
        return account.getData().getCollection().getCollectionItemByID(collectionItemID) != null;
    }

    public static boolean isCollectionItemInDeck(String deckName, String collectionItemID) {
        return false;
    }

    public static boolean isDeckFull(String deckName) {
        return false;
    }

    public static boolean isAddingASecondHero(String deckName, String cardName) {
        return false;
    }

    public static void selectDeck(String deckName) {
        Deck deck = getDeck(deckName);
        if (deck == null)
            return;
        if (!deck.isFull() || !deck.hasHero()) {
            view.showInvalidDeckError();
            return;
        }
        getAccount().getData().setMainDeck(deck);
        view.alertDeckSelection();
    }

    public static void exportDeck() {
        if (getAccount().getData().getMainDeck() == null) {
            view.showNoMainDeckError();
            return;
        }
        try {
            String fileName = getAccount().getName() + "_" + getAccount().getData().getMainDeck().getDeckName();
            int index = 0;
            while (new File("./export/" + fileName + index + ".json").exists())
                index++;
            fileName = fileName + index;
            FileWriter out = new FileWriter("./export/" + fileName + ".json", false);
            YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
            out.write(yaGson.toJson(getAccount().getData().getMainDeck(), Deck.class));
            out.flush();
            view.alertExport(fileName);
        } catch (IOException ignored) {
        }
    }

    public static void importDeck(String deckPath) {
        File file = new File("./export/" + deckPath + ".json");
        try {
            getAccount().getData().importDeck(new Deck(new YaGson().fromJson(new BufferedReader(new FileReader(file)), Deck.class)
            ));
        } catch (FileNotFoundException ignored) {
            view.showNoSuchFileError();
        } catch (Exception e) {
            if (e.getMessage().startsWith("Imported")) {
                view.showImportedCardError();
            } else
                view.showInvalidFileError();
        }
        view.showSuccessfulImport();
    }
}
