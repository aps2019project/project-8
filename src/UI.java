import model.Account;
import view.CommandLineView;
import view.View;
import menus.MainMenu;
import menus.Menus;

import java.util.Scanner;

public class UI {
    private static final String ACCOUNT_NAME = "\\w+";
    private static final String CREATE_ACCOUNT = "(?i:create account) " + ACCOUNT_NAME;
    private static final String LOGIN = "(?i:login) " + ACCOUNT_NAME;
    private static final String SHOW_LEADERBOARD = "(?i:show leaderboard)";
    private static final String HELP = "(?i:help)";
    private static final String EXIT = "(?i:exit)";

    private static final String[] commands = {
            "create account [user name]",
            "login [user name]",
            "show leaderboard",
            "exit"
    };

    private static Scanner scanner = new Scanner(System.in);
    private static View view = new CommandLineView();
    private static Menus currentMenu = Menus.LOGIN;
    private static Account currentAccount = null;
    private static String command = null;

    public static void main(String[] args) {
        command = scanner.nextLine();
        while (true) {
            if (decide(command))
                return;
            command = scanner.nextLine();
        }
    }

    private static boolean decide(String command) {
        switch (currentMenu) {
            case LOGIN:
                return actLogin(command);
            case MAIN_MENU:
                actMainMenu(command);
                break;
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
            case IN_GAME:
                actInGame(command);
                break;
            case GRAVEYARD:
                actGraveyard(command);
        }
        return false;
    }

    private static boolean actLogin(String command) {
        if (command.matches(EXIT))
            return true;
        if (command.matches(CREATE_ACCOUNT))
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

    private static void actMainMenu(String command) {
    }

    private static void actCollection(String command) {
    }

    private static void actShop(String command) {
    }

    private static void actBattle(String command) {
    }

    private static void actSinglePlayer(String command) {
    }

    private static void actStory(String command) {
    }

    private static void actCustomGame(String command) {
    }

    private static void actMultiplayer(String command) {
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
        currentAccount = Account.getAccount(name);
        if (currentAccount == null) {
            view.showNoSuchAccountError();
            return;
        }
        view.promptPassword();
        String password = scanner.nextLine();
        if (currentAccount.isPasswordValid(password)) {
            view.alertLogin();
            MainMenu.setAccount(currentAccount);
            currentMenu = Menus.MAIN_MENU;
            return;
        }
        currentAccount = null;
        view.showIncorrectPasswordError();
    }

    private static void showLeaderboard() {
        view.showLeaderboard(Account.getAccounts());
    }

    private static void help() {
        view.showHelp(commands);
    }
}
