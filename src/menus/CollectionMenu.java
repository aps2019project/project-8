package menus;

import model.Card;
import model.CollectionItem;
import model.Item;

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

    private static HashMap<String, Card> cards = null;
    private static HashMap<String, Item> items = null;

    public static String[] help() {
        return commands;
    }

    public static HashMap<String, CollectionItem> show() {
        return account.getCollectionItems();
    }

    public static ArrayList<String> search(String collectionItemName) {
        return account.getCollection().getCollectionItemIDs(collectionItemName);
    }

    public static boolean createDeck(String name) {
        return false;
    }

    public static boolean deleteDeck(String name) {
        return false;
    }

    public static void addCollectionItem(String deckName, String cardName) {
    }

    public static void removeCard(String deckName, String cardName) {
    }

    public static boolean validateDeck(String deckName) {
        return false;
    }

    public static void showAllDecks() {
    }

    public static void showDeck(String name) {
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

    public static boolean selectDeck(String deckName) {
        return false;
    }

    public static HashMap<String, ArrayList<CollectionItem>> getDecks() {
        return null;
    }

    public static ArrayList<CollectionItem> getDeck(String s) {
        return null;
    }
}
