import com.google.gson.Gson;
import menus.*;
import model.Account;
import view.CommandLineView;
import view.View;

import java.io.*;
import java.util.Scanner;

public class UI {
    private static final String NAME = "\\w+";
    private static final String ACCOUNT_NAME = NAME;
    private static final String CREATE_ACCOUNT = "(?i:create account) " + ACCOUNT_NAME;
    private static final String LOGIN = "(?i:login) " + ACCOUNT_NAME;
    private static final String SHOW_LEADERBOARD = "(?i:show leader[ ]?board)";
    private static final String HELP = "(?i:help)";
    private static final String EXIT = "(?i:exit)";
    private static final String COLLECTION = "(?i:collection)";
    private static final String SHOP = "(?i:shop)";
    private static final String BATTLE = "(?i:battle)";
    private static final String LOGOUT = "(?i:logout)";
    private static final String SAVE = "(?i:save)";
    private static final String SINGLE_PLAYER = "(?i:single[ ]?player)";
    private static final String MULTIPLAYER = "(?i:multi[ ]?player)";
    private static final String STORY = "(?i:story)";
    private static final String CUSTOM_GAME = "(?i:custom game)";
    private static final String SHOW = "(?i:show)";
    private static final String COLLECTION_ITEM_NAME = NAME;
    private static final String SEARCH = "(?i:search) " + COLLECTION_ITEM_NAME;
    private static final String DECK_NAME = NAME;
    private static final String CREATE_DECK = "(?i:create deck) " + DECK_NAME;
    private static final String DELETE_DECK = "(?i:delete deck) " + DECK_NAME;
    private static final String ID = NAME;
    private static final String ADD_COLLECTION_ITEM_TO_DECK = "(?i:add) " + ID + " (?i:to deck) " + DECK_NAME;
    private static final String REMOVE_COLLECTION_ITEM_FROM_DECK = "(?i:remove) " + ID + " (?i:from deck) " + DECK_NAME;
    private static final String VALIDATE_DECK = "(?i:validate deck) " + DECK_NAME;
    private static final String SELECT_DECK = "(?i:select deck) " + DECK_NAME;
    private static final String SHOW_ALL_DECKS = "(?i:show all decks)";
    private static final String SHOW_DECK = "(?i:show deck) " + DECK_NAME;
    private static final String SHOW_COLLECTION = "(?i:show collection)";
    private static final String SEARCH_COLLECTION = "(?i:search collection) " + COLLECTION_ITEM_NAME;
    private static final String BUY = "(?i:buy) " + COLLECTION_ITEM_NAME;
    private static final String SELL = "(?i:sell) " + ID;
    private static final String LEVEL = "\\d";
    private static final String START_CUSTOM_GAME = "(?i:start game) " + DECK_NAME + " \\d( \\d)*";
    private static final String SELECT_USER = "(?i:select user) " + NAME;
    private static final String START_MULTIPLAYER_GAME = "(?i:start multiplayer game) \\d( \\d)*";
    private static final String GAME_INFO = "(?i:game info)";
    private static final String SHOW_MY_MINIONS = "(?i:show my minions)";
    private static final String SHOW_OPPONENT_MINIONS = "(?i:show opponent minions)";
    private static final String SHOW_CARD_INFO = "(?i:show card info) " + ID;
    private static final String SELECT_COLLETION_ITME = "(?i:select) " + ID;
    private static final String COORDINATES = "\\(\\d+, \\d+\\)";
    private static final String MOVE = "(?i:move to) " + COORDINATES;
    private static final String ATTACK = "(?i:attack) " + ID;
    private static final String ATTACK_COMBO = "(?i:attack combo) " + ID + " (" + ID + ")+";
    private static final String SPECIAL_POWER = "(?i:use special power) " + COORDINATES;
    private static final String SHOW_HAND = "(?i:show hand)";
    private static final String INSERT = "(?i:insert) " + COLLECTION_ITEM_NAME + " (?i:in) " + COORDINATES;
    private static final String END_TURN = "(?i:end turn)";
    private static final String SHOW_COLLECTIBLES = "(?i:show collectables)";
    private static final String SHOW_INFO = "(?i:show info)";
    private static final String USE_COLLECTIBLE = "(?i:use) " + COORDINATES;
    private static final String SHOW_NEXT_CARD = "(?i:show next card)";
    private static final String ENTER_GRAVEYARD = "(?i:enter graveyard)";
    private static final String GRAVEYARD_SHOW_INFO = "(?i:show info) " + ID;
    private static final String SHOW_CARDS = "(?i:show cards)";
    private static final String END_GAME = "(?i:end game)";
    private static final String SHOW_MENU = "(?i:show menu)";

    private static final String[] commands = {
            "create account [user name]",
            "login [user name]",
            "show leaderboard",
            "help",
            "exit"
    };

    private static Scanner scanner = new Scanner(System.in);
    private static View view = new CommandLineView();
    private static Menus menu = Menus.LOGIN;
    private static String command = null;

    public static void main(String[] args) {
        load();
        Menu.setView(view);
        view.showHelp(help());
        command = scanner.nextLine();
        command = command.trim();
        while (true) {
            if (decide(command)) {
                scanner.close();
                return;
            }
            command = scanner.nextLine();
            command = command.trim();
        }
    }

    private static boolean decide(String command) {
        switch (menu) {
            case LOGIN:
                return actLogin(command);
            case MAIN_MENU:
                return actMainMenu(command);
            case COLLECTION:
                actCollection(command);
                break;
            case SHOP:
                actShop(command);
                break;
            case BATTLE:
                actBattle(command);
                break;
            case SINGLE_PLAYER:
                actSinglePlayer(command);
                break;
            case MULTIPLAYER:
                actMultiplayer(command);
                break;
            case STORY:
                actStory(command);
                break;
            case CUSTOM_GAME:
                actCustomGame(command);
                break;
            case GAME_MENU:
                actInGame(command);
                break;
            case GRAVEYARD_MENU:
                actGraveyard(command);
        }
        return false;
    }

    private static boolean actLogin(String command) {
        if (command.matches(EXIT))
            return true;
        else if (command.matches(CREATE_ACCOUNT))
            createAccount(command.split(" ")[2]);
        else if (command.matches(LOGIN))
            login(command.split(" ")[1]);
        else if (command.matches(SHOW_LEADERBOARD))
            showLeaderboard();
        else if (command.matches(HELP))
            help();
        else
            view.showInvalidCommandError();
        return false;
    }

    private static boolean actMainMenu(String command) {
        if (command.matches(EXIT))
            return true;
        else if (command.matches(COLLECTION))
            switchTo(Menus.COLLECTION);
        else if (command.matches(SHOP))
            switchTo(Menus.SHOP);
        else if (command.matches(BATTLE))
            switchTo(Menus.BATTLE);
        else if (command.matches(HELP))
            view.showHelp(MainMenu.help());
        else if (command.matches(SAVE)) {
            save();
            view.alertSave();
        } else if (command.matches(LOGOUT))
            switchTo(Menus.LOGIN);
        else
            view.showInvalidCommandError();
        return false;
    }

    private static void actCollection(String command) {
        String[] commandSplit = command.split(" ");
        if (command.matches(EXIT))
            switchTo(Menus.MAIN_MENU);
        else if (command.matches(HELP))
            view.showHelp(CollectionMenu.help());
        else if (command.matches(SHOW))
            view.showCollection(CollectionMenu.show());
        else if (command.matches(SEARCH))
            view.showID(CollectionMenu.search(commandSplit[1]));
        else if (command.matches(SAVE))
            save();
        else if (command.matches(CREATE_DECK))
            if (CollectionMenu.createDeck(commandSplit[2]))
                view.alertDeckCreation();
            else
                view.showAlreadyExistingDeckError();
        else if (command.matches(DELETE_DECK))
            if (CollectionMenu.deleteDeck(commandSplit[2]))
                view.alertDeckDeletion();
            else
                view.showNoSuchDeckError();
        else if (command.matches(ADD_COLLECTION_ITEM_TO_DECK)) {
            String collectionItemID = commandSplit[1];
            String deckName = commandSplit[4];
            if (!CollectionMenu.hasCollectionItem(collectionItemID))
                view.showNoSuchCollectionItemError();
            else if (CollectionMenu.isCollectionItemInDeck(deckName, collectionItemID))
                view.showDeckAlreadyHasCollectionItemError();
            else if (CollectionMenu.isDeckFull(deckName))
                view.showDeckIsFullError();
            else if (CollectionMenu.isAddingASecondHero(deckName, collectionItemID))
                view.showAddingASecondHeroError();
            else {
                CollectionMenu.addCollectionItem(deckName, collectionItemID);
                view.alertCollectionItemAddedToDeck();
            }
        } else if (command.matches(REMOVE_COLLECTION_ITEM_FROM_DECK)) {
            String collectionItemID = commandSplit[1];
            String deckName = commandSplit[4];
            if (CollectionMenu.isCollectionItemInDeck(deckName, collectionItemID)) {
                CollectionMenu.removeCard(deckName, collectionItemID);
                view.alertCollectionItemRemovedFromDeck();
            } else
                view.showDeckHasNoSuchCollectionItemError();
        } else if (command.matches(VALIDATE_DECK))
            if (CollectionMenu.validateDeck(commandSplit[2]))
                view.alertValidDeck();
            else
                view.showInvalidDeckError();
        else if (command.matches(SELECT_DECK))
            if (CollectionMenu.selectDeck(commandSplit[2]))
                view.alertDeckSelection();
            else
                view.showInvalidDeckError();
        else if (command.matches(SHOW_ALL_DECKS))
            view.showDecks(CollectionMenu.getDecks());
        else if (command.matches(SHOW_DECK))
            view.showDeck(CollectionMenu.getDeck(commandSplit[2]));
        else
            view.showInvalidCommandError();
    }

    private static void actShop(String command) {
        String[] commandSplit = command.split(" ");
        if (command.matches(EXIT))
            switchTo(Menus.MAIN_MENU);
        else if (command.matches(HELP))
            view.showHelp(Shop.help());
        else if (command.matches(SHOW_COLLECTION))
            view.showCollection(CollectionMenu.show());
        else if (command.matches(SEARCH_COLLECTION))
            view.showID(CollectionMenu.search(commandSplit[2]));
        else if (command.matches(SEARCH))
            view.showName(Shop.search(commandSplit[1]));
        else if (command.matches(BUY)) {
            String collectionItemName = commandSplit[2];
            if (!Shop.hasCollectionItem(collectionItemName))
                view.showNoSuchCollectionItemError();
            else if (Menu.getAccount().getMoney() < Shop.getPrice(collectionItemName))
                view.showNotEnoughMoneyError();
            else if (Menu.getAccount().hasThreeItems() && Shop.isItem(collectionItemName))
                view.showFourthItemError();
            else {
                Shop.buy(collectionItemName);
                view.alertBuy();
            }
        } else if (command.matches(SELL)) {
            String collectionItemID = commandSplit[1];
            if (CollectionMenu.hasCollectionItem(collectionItemID)) {
                Shop.sellCollectionItem(collectionItemID);
                view.alertSell();
            }
            else
                view.showNoSuchCollectionItemError();
        } else if (command.matches(SHOW))
            view.showShop(Shop.show());
    }

    private static void actBattle(String command) {
        if (command.matches(EXIT))
            switchTo(Menus.MAIN_MENU);
        else if (command.matches(SINGLE_PLAYER))
            switchTo(Menus.SINGLE_PLAYER);
        else if (command.matches(MULTIPLAYER))
            switchTo(Menus.MULTIPLAYER);
        else
            view.showInvalidCommandError();
    }

    private static void actSinglePlayer(String command) {
        if (command.matches(EXIT))
            switchTo(Menus.BATTLE);
        else if (command.matches(STORY))
            switchTo(Menus.STORY);
        else if (command.matches(CUSTOM_GAME))
            switchTo(Menus.CUSTOM_GAME);
        else
            view.showInvalidCommandError();
    }

    private static void actStory(String command) {
        if (command.matches(EXIT))
            switchTo(Menus.SINGLE_PLAYER);
        else
            view.showInvalidCommandError();
    }

    private static void actCustomGame(String command) {
        if (command.matches(EXIT))
            switchTo(Menus.SINGLE_PLAYER);
        else
            view.showInvalidCommandError();
    }

    private static void actMultiplayer(String command) {
        if (command.matches(EXIT))
            switchTo(Menus.BATTLE);
        else
            view.showInvalidCommandError();
    }

    private static void actInGame(String command) {
    }

    private static void actGraveyard(String command) {
    }

    private static void createAccount(String name) {
        if (Account.getAccount(name) != null) {
            view.showAccountCreationError();
            return;
        }
        view.promptPassword();
        String password = scanner.nextLine();
        new Account(name, password);
        view.alertAccountCreation();
    }

    private static void login(String name) {
        Account account = Account.getAccount(name);
        if (account == null) {
            view.showNoSuchAccountError();
            return;
        }
        view.promptPassword();
        String password = scanner.nextLine();
        if (account.isPasswordValid(password)) {
            view.alertLogin();
            Menu.setAccount(account);
            switchTo(Menus.MAIN_MENU);
            menu = Menus.MAIN_MENU;
            return;
        }
        view.showIncorrectPasswordError();
    }

    private static void showLeaderboard() {
        view.showLeaderboard(Account.getAccounts());
    }

    private static String[] help() {
        return commands;
    }

    private static void switchTo(Menus menu) {
        UI.menu = menu;
        switch (menu) {
            case LOGIN:
                view.showHelp(help());
                break;
            case MAIN_MENU:
                view.showHelp(MainMenu.help());
                break;
            case COLLECTION:
                view.showHelp(CollectionMenu.help());
                break;
            case SHOP:
                view.showHelp(Shop.help());
                break;
            case BATTLE:
                view.showHelp(Battle.help());
                break;
            case SINGLE_PLAYER:
                view.showHelp(SinglePlayer.help());
                break;
            case MULTIPLAYER:
                view.showHelp(Multiplayer.help());
                break;
            case STORY:
                view.showHelp(Story.help());
                break;
            case CUSTOM_GAME:
                view.showHelp(CustomGame.help());
                break;
        }
    }

    private static void load() {
        try {
            for (File file : new File("./save").listFiles()) {
                Gson gson = new Gson();
                new Account(gson.fromJson(new BufferedReader(new FileReader(file)).readLine(), Account.class));
            }
        } catch (IOException ignored) {
        }
    }

    public static void save() {
        try {
            FileWriter out = new FileWriter("./save/" + Menu.getAccount().getName() + ".txt", false);
            Gson gson = new Gson();
            out.write(gson.toJson(Menu.getAccount()));
            out.flush();
        } catch (IOException ignored) {
        }
    }
}
