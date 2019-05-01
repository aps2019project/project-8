package menus;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class CollectionMenu extends Menu {
    private static final String[] commands = {
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
        view.showCollection(getAccount().getCollectionItems());
    }

    public static void search(String collectionItemName) {
        view.showID(account.getCollection().getCollectionItemIDs(collectionItemName));
    }

    public static void createDeck(String name) {
        if (getAccount().getCollection().hasDeck(name)) {
            view.showAlreadyExistingDeckError();
            return;
        }
        getAccount().getCollection().createDeck(name);
        view.alertDeckCreation();
    }

    public static void deleteDeck(String name) {
        if (!getAccount().getCollection().hasDeck(name)) {
            view.showDeckDoesNotExistError();
            return;
        }
        getAccount().getCollection().deleteDeck(name);
        view.alertDeckDeletion();
    }

    private static Deck getDeck(String deckName) { // if successful returns false
        if (!getAccount().getCollection().hasDeck(deckName)) {
            view.showDeckDoesNotExistError();
            return null;
        }
        return getAccount().getCollection().getDeck(deckName);
    }

    private static CollectionItem getCollectionItem(String collectionItemID) {
        if (!getAccount().getCollection().hasCollectionItem(collectionItemID)) {
            view.showNoSuchCollectionItemError();
            return null;
        }
        return getAccount().getCollection().getCollectionItem(collectionItemID);
    }

    public static void addCollectionItem(String deckName, String collectionItemID) {
        Deck deck = getDeck(deckName);
        CollectionItem collectionItem = getCollectionItem(collectionItemID);
        if (deck == null || collectionItem == null) {
            return;
        }

        if (deck.hasCollectionItem(collectionItem)) {
            view.showDeckAlreadyHasCollectionItemError();
            return;
        }

        if (collectionItem instanceof Card && deck.isFull()) {
            view.showDeckIsFullError();
            return;
        }

        if (collectionItem instanceof Item && deck.hasItem()) {
            view.showAddingASecondItemToDeckError();
            return;
        }

        if (collectionItem instanceof Hero && deck.hasHero()) {
            view.showAddingASecondHeroToDeckError();
            return;
        }

        deck.addCollectionItem(collectionItem);
        view.alertCollectionItemAddedToDeck();
    }

    public static void removeCard(String deckName, String collectionItemID) {
        Deck deck = getDeck(deckName);
        CollectionItem collectionItem = getCollectionItem(collectionItemID);
        if (deck == null || collectionItem == null) {
            return;
        }

        if (!deck.hasCollectionItem(collectionItem)) {
            view.showDeckHasNoSuchCollectionItemError();
            return;
        }

        deck.removeCollectionItem(collectionItem);
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
        view.showDecks(getAccount().getCollection().getDecks());
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
        return account.getCollection().getCollectionItemByID(collectionItemID) != null;
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
        getAccount().setMainDeck(deck);
        view.alertDeckSelection();
    }

    public static HashMap<String, ArrayList<CollectionItem>> getDecks() {

        return null;
    }

    /*public static ArrayList<CollectionItem> getDeck(String s) {
        return null;
    }*/
}
