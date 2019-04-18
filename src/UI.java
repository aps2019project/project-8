import menus.*;
import model.Account;
import view.CommandLineView;
import view.View;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class UI {
    private static final String ACCOUNT_NAME = "\\w+";
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
    private static Account account = null;
    private static String command = null;

    public static void main(String[] args) {
        load();
        Menu.setView(view);
        view.showHelp(help());
        command = scanner.nextLine();
        while (true) {
            if (decide(command)) {
                scanner.close();
                return;
            }
            command = scanner.nextLine();
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
        else if (command.matches(SAVE))
            MainMenu.save();
        else if (command.matches(LOGOUT))
            switchTo(Menus.LOGIN);
        else
            view.showInvalidCommandError();
        return false;
    }

    private static void actCollection(String command) {
        if (command.matches(EXIT))
            switchTo(Menus.MAIN_MENU);
        else if (command.matches(HELP))
            view.showHelp(CollectionMenu.help());
    }

    private static void actShop(String command) {
        if (command.matches(EXIT))
            switchTo(Menus.MAIN_MENU);
        else if (command.matches(HELP))
            view.showHelp(Shop.help());
    }

    private static void actBattle(String command) {
        if (command.matches(SINGLE_PLAYER))
            switchTo(Menus.SINGLE_PLAYER);
        else if (command.matches(MULTIPLAYER))
            switchTo(Menus.MULTIPLAYER);
        else
            view.showInvalidCommandError();
    }

    private static void actSinglePlayer(String command) {
        if (command.matches(STORY))
            switchTo(Menus.STORY);
        else if (command.matches(CUSTOM_GAME))
            switchTo(Menus.CUSTOM_GAME);
        else
            view.showInvalidCommandError();
    }

    private static void actStory(String command) {
    }

    private static void actCustomGame(String command) {
        view.showHelp(CustomGame.help());
    }

    private static void actMultiplayer(String command) {
        view.showHelp(Multiplayer.help());
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
        account = Account.getAccount(name);
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
        account = null;
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
                account = null;
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
                String[] accountSaveData = new BufferedReader(new FileReader(file)).readLine().split(":");
                new Account(file.getName().split("\\.")[0], accountSaveData[0],
                        Integer.parseInt(accountSaveData[1]));
            }
        } catch (IOException ignored) {}
    }
}
