package menus;

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

    public static String[] help() {
        return commands;
    }

    public static void show() {
    }

    public static void search(String name) {
    }

    public static void createDeck(String name) {
    }

    public static void deleteDeck(String name) {
    }

    public static void addCard(String cardName, String deckName) {
    }

    public static void removeCard(String cardName, String deckName) {
    }

    public static void validateDeck(String deckName) {
    }

    public static void showAllDecks() {
    }

    public static void showDeck(String name) {
    }

    public static String getString() {
        return "";
    }
}
