package menus;

import model.AI;
import model.Account;
import model.GameType;
import model.Player;

public class GameMenu extends InGameMenu {
    private static final String[] commands = {
            "Exit",
            "Help",
            "Enter graveyard"
    };

    private static boolean hasAI;
    private static final int NUMBER_OF_PLAYERS = 2;
    private static Player[] players = new Player[NUMBER_OF_PLAYERS];
    private static int numberOfFlags;

    /*    public static  void setGame(Game game) {
                menus.GameMenu.game = game;
            }*/
    public static void help() {
        view.showHelp(commands);
    }

    public static void showGameInfo() {
    }

    public static void showMinions(int who) {
    }

    public static void showCardInfo(String cardID) {
    }

    public static void selectCard(String cardID) {
    }

    public static void moveUnit(int x, int y) {
    }

    public static void attackUnit(String cardID) {
    }

    public static void attackCombo(String opponentCardID, String[] cardIDs) {
    }

    public static void useSpecialPower(int x, int y) {
    }

    public static void showHand() {
    }

    public static void insertCard(String name, int x, int y) {
        // ? redundant?
    }

    public static void endTurn() {
    }

    public static void showAllCollectibles() {
    }

    public static void selectCollectible(String name) {
    }

    public static void showCollectibleInfo() {
    }

    public static void useCollectible(int x, int y) {
    }

    public static void showNextCard() {
    }

    public static String getString() {
        return "";
    }

    public static boolean startGame(AI ai) {
        if (ai == null) {
            view.showInvalidParametersError();
            return false;
        }
        hasAI = true;
        players[1] = ai.getPlayer();
        players[0] = getAccount().getPlayer();
        return true;
    }

    public static boolean startGame(String deckName, int mode, int numberOfFlags) {
        if (getAccount().getDeck(deckName) == null) {
            view.showDeckDoesNotExistError();
            return false;
        }
        if (!getAccount().getDeck(deckName).isValid()) {
            view.showInvalidDeckError();
            return false;
        }
        if (checkGame(mode, numberOfFlags)) return false;
        hasAI = true;
        players[1] = (new AI(getAccount().getDeck(deckName), GameType.get(mode))).getPlayer();
        GameMenu.numberOfFlags = numberOfFlags;
        players[0] = getAccount().getPlayer();
        return true;
    }

    public static boolean startGame(Account secondAccount, int mode, int numberOfFlags) {
        if (checkGame(mode, numberOfFlags)) return false;
        hasAI = false;
        players[1] = secondAccount.getPlayer();
        players[0] = getAccount().getPlayer();
        return true;
    }

    private static boolean checkGame(int mode, int numberOfFlags) {
        if (mode < 1 || mode > 3) {
            view.showNoSuchGameModeError();
            return true;
        }
        if (mode == 3 && numberOfFlags < 1) {
            view.showInvalidParametersError();
            return true;
        }
        if (mode != 3 && numberOfFlags != -1) {
            view.showInvalidParametersError();
            return true;
        }
        return false;
    }

    public static void showMenu() {
    }
}
