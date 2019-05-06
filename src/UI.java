import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import menus.*;
import model.AI;
import model.Account;
import view.CommandLineView;
import view.View;

import java.io.*;
import java.util.Arrays;
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
    private static final String SELECT_COLLECTION_ITEM = "(?i:select) " + ID;
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
    private static boolean selectingUser = true;

    public static void main(String[] args) {
        load();
        Menu.setView(view);
        help();
        command = scanner.nextLine();
        command = command.trim();
        while (true) {
            try {
                if (decide(command)) {
                    scanner.close();
                    return;
                }
            } catch (Exception ignored) {
                System.err.println(ignored);
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
            MainMenu.help();
        else if (command.matches(SAVE))
            save();
        else if (command.matches(LOGOUT))
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
            CollectionMenu.help();
        else if (command.matches(SHOW))
            CollectionMenu.show();
        else if (command.matches(SEARCH))
            CollectionMenu.search(commandSplit[1]);
        else if (command.matches(SAVE))
            save();
        else if (command.matches(CREATE_DECK))
            CollectionMenu.createDeck(commandSplit[2]);
        else if (command.matches(DELETE_DECK))
            CollectionMenu.deleteDeck(commandSplit[2]);
        else if (command.matches(ADD_COLLECTION_ITEM_TO_DECK)) {
            String collectionItemID = commandSplit[1];
            String deckName = commandSplit[4];
            CollectionMenu.addCollectionItem(deckName, collectionItemID);
        } else if (command.matches(REMOVE_COLLECTION_ITEM_FROM_DECK)) {
            String collectionItemID = commandSplit[1];
            String deckName = commandSplit[4];
            CollectionMenu.removeCard(deckName, collectionItemID);
        } else if (command.matches(VALIDATE_DECK))
            CollectionMenu.validateDeck(commandSplit[2]);
        else if (command.matches(SELECT_DECK))
            CollectionMenu.selectDeck(commandSplit[2]);
        else if (command.matches(SHOW_ALL_DECKS))
            CollectionMenu.showAllDecks();
        else if (command.matches(SHOW_DECK))
            CollectionMenu.showDeck(commandSplit[2]);
        else
            view.showInvalidCommandError();
    }

    private static void actShop(String command) {
        String[] commandSplit = command.split(" ");
        if (command.matches(EXIT))
            switchTo(Menus.MAIN_MENU);
        else if (command.matches(HELP))
            Shop.help();
        else if (command.matches(SHOW_COLLECTION))
            Shop.showCollection();
        else if (command.matches(SEARCH_COLLECTION))
            CollectionMenu.search(commandSplit[2]);
        else if (command.matches(SEARCH))
            Shop.search(commandSplit[1]);
        else if (command.matches(BUY)) {
            String collectionItemName = commandSplit[1];
            Shop.buy(collectionItemName);
        } else if (command.matches(SELL)) {
            String collectionItemID = commandSplit[1];
            Shop.sellCollectionItem(collectionItemID);
        } else if (command.matches(SHOW))
            Shop.show();
        else
            view.showInvalidCommandError();
    }

    private static void actBattle(String command) {
        if (command.matches(EXIT))
            switchTo(Menus.MAIN_MENU);
        else if (command.matches(HELP))
            Battle.help();
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
        else if (command.matches(HELP))
            SinglePlayer.help();
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
        else if (command.matches(HELP))
            Story.help();
        else if (command.matches(LEVEL)) {
            if (GameMenu.startGame(Integer.parseInt(command))) {
                switchTo(Menus.GAME_MENU);
            }
        }
        else
            view.showInvalidCommandError();
    }

    private static void actCustomGame(String command) {
        String[] commandSplit = command.split(" ");
        if (command.matches(EXIT))
            switchTo(Menus.SINGLE_PLAYER);
        else if (command.matches(HELP))
            CustomGame.help();
        else if (command.matches(START_CUSTOM_GAME)) {
            if (GameMenu.startGame(commandSplit[2], Integer.parseInt(commandSplit[3]), (commandSplit.length > 4 ?
                    Integer.parseInt(commandSplit[4]) : -1)))
                switchTo(Menus.GAME_MENU);
        }
        else
            view.showInvalidCommandError();
    }

    private static void actMultiplayer(String command) {
        String[] commandSplit = command.split(" ");
        if (command.matches(EXIT)) {
            selectingUser = true;
            switchTo(Menus.BATTLE);
        } else if (command.matches(HELP))
            Multiplayer.help(selectingUser);
        else if (selectingUser && command.matches(SELECT_USER)) {
            if (Multiplayer.selectUser(commandSplit[2])) {
                selectingUser = false;
                Multiplayer.help(false);
            }
        } else if (!selectingUser && command.matches(START_MULTIPLAYER_GAME)) {
            if (GameMenu.startGame(Multiplayer.getSecondAccount(), Integer.parseInt(commandSplit[3]), (commandSplit.length > 4 ?
                    Integer.parseInt(commandSplit[4]) : -1))) {
                switchTo(Menus.GAME_MENU);
            }
        } else
            view.showInvalidCommandError();
    }

    private static boolean gameEnded = false;

    public static void endGame() {
        gameEnded = true;
    }

    private static void actInGame(String command) {
        if (!gameEnded) {
            if (command.matches(EXIT))
                switchTo(Menus.MAIN_MENU);
            else if (command.matches(SHOW_MENU))
                GameMenu.help(false);
            else if (command.matches(HELP))
                GameMenu.showOptions();
            else if (command.matches(ENTER_GRAVEYARD))
                switchTo(Menus.GRAVEYARD_MENU);
            else if (command.matches(GAME_INFO))
                GameMenu.showGameInfo();
            else if (command.matches(SHOW_MY_MINIONS))
                GameMenu.showMinions(0);
            else if (command.matches(SHOW_OPPONENT_MINIONS))
                GameMenu.showMinions(1);
            else if (command.matches(SHOW_CARD_INFO))
                GameMenu.showCardInfo(command.split(" ")[3]);
            else if (command.matches(SELECT_COLLECTION_ITEM))
                GameMenu.select(command.split(" ")[1]);
            else if (command.matches(MOVE)) {
                int[] coordinates = getCoordinates(command);
                GameMenu.moveUnit(coordinates[0], coordinates[1]);
            } else if (command.matches(ATTACK))
                GameMenu.attackUnit(command.split(" ")[1]);
            else if (command.matches(ATTACK_COMBO)) {
                String[] commandSplit = command.split(" ");
                GameMenu.attackCombo(commandSplit[2], Arrays.copyOfRange(commandSplit, 3, commandSplit.length));
            } else if (command.matches(SPECIAL_POWER)) {
                int[] coordinates = getCoordinates(command);
                GameMenu.useSpecialPower(coordinates[0], coordinates[1]);
            } else if (command.matches(SHOW_HAND))
                GameMenu.showHand();
            else if (command.matches(INSERT)) {
                int[] coordinates = getCoordinates(command);
                GameMenu.insertCard(command.split(" ")[1], coordinates[0], coordinates[1]);
            } else if (command.matches(END_TURN))
                GameMenu.endTurn();
            else if (command.matches(SHOW_COLLECTIBLES))
                GameMenu.showAllCollectibles();
            else if (command.matches(SHOW_INFO))
                GameMenu.showCollectibleInfo();
            else if (command.matches(USE_COLLECTIBLE)) {
                int[] coordinates = getCoordinates(command);
                GameMenu.useCollectible(coordinates[0], coordinates[1]);
            } else if (command.matches(SHOW_NEXT_CARD))
                GameMenu.showNextCard();
            else
                view.showInvalidCommandError();
            GameMenu.checkGameCondition();
        } else {
            if (command.matches(END_GAME)) {
                switchTo(Menus.MAIN_MENU);
                gameEnded = false;
            } else if (command.matches(HELP))
                GameMenu.help(true);
            else
                view.showInvalidCommandError();
        }
    }

    private static int[] getCoordinates(String command) {
        command = command.split("\\(")[1];
        command = command.split("\\)")[0];
        String[] commandSplit = command.split(",");
        return new int[] {Integer.parseInt(commandSplit[0]), Integer.parseInt(commandSplit[1].trim())};
    }

    private static void actGraveyard(String command) {
        if (command.matches(EXIT))
            switchTo(Menus.GAME_MENU);
        else if (command.matches(HELP))
            GraveyardMenu.help();
        else if (command.matches(SHOW_CARD_INFO))
            GraveyardMenu.showInfo(command.split(" ")[2]);
        else if (command.matches(SHOW_CARDS))
            GraveyardMenu.showCards();
        else
            view.showInvalidCommandError();
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

    private static void help() {
        view.showHelp(commands);
    }

    private static void switchTo(Menus menu) {
        UI.menu = menu;
        switch (menu) {
            case LOGIN:
                help();
                break;
            case MAIN_MENU:
                MainMenu.help();
                break;
            case COLLECTION:
                CollectionMenu.help();
                break;
            case SHOP:
                Shop.help();
                break;
            case BATTLE:
                Battle.help();
                break;
            case SINGLE_PLAYER:
                SinglePlayer.help();
                break;
            case MULTIPLAYER:
                Multiplayer.help(selectingUser);
                break;
            case STORY:
                Story.help();
                break;
            case CUSTOM_GAME:
                CustomGame.help();
                break;
            case GAME_MENU:
                GameMenu.help(false);
                break;
            case GRAVEYARD_MENU:
                GraveyardMenu.help();
                break;
        }
    }

    private static void load() {
        try {
            for (File file : new File("./save").listFiles()) {
                YaGson yaGson = new YaGson();
                new Account(yaGson.fromJson(new BufferedReader(new FileReader(file)), Account.class));
            }
            Shop.load();
            AI.load();
        } catch (IOException ignored) {}
    }

    public static void save() {
        try {
            FileWriter out = new FileWriter("./save/" + Menu.getAccount().getName() + ".json", false);
            YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
            out.write(yaGson.toJson(Menu.getAccount(), Account.class));
            out.flush();
            view.alertSave();
        } catch (IOException ignored) {}
    }
}
