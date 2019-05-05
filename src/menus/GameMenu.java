package menus;

import model.*;

public class GameMenu extends InGameMenu {
    private static final String[] commands = {
            "Game info",
            "Show my minions",
            "Show opponent minions",
            "Show card info [card id]",
            "Select [card id]",
            "Move to ([x], [y])",
            "Attack [opponent card id]",
            "Attack combo [opponent card id] [my card id] [my card id] [...]",
            "Use special power ([x], [y])",
            "Show hand",
            "Insert [card name] in ([x], [y])",
            "End turn",
            "Show collectibles",
            "Select [collectible id]",
            "Show info",
            "Use ([x], [y])",
            "Show next card",
            "Enter graveyard",
            "Help",
            "End game",
            "Exit",
            "Show menu"
    };
    private static final int NUMBER_OF_PLAYERS = 2;
    private static boolean hasAI;
    private static Player[] players = new Player[NUMBER_OF_PLAYERS];
    private static int numberOfFlags;
    private static Game game;

    public static void setGame(Game game) {
        GameMenu.game = game;
    }

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
        game.selectCard(cardID);
    }

    public static void moveUnit(int x, int y) {
        game.moveSelectedUnit(x, y);
    }

    public static void attackUnit(String cardID) {
        game.attackTargetCardWithSelectedUnit(cardID);
    }

    public static void attackCombo(String opponentCardID, String[] cardIDs) {
        game.attackCombo(opponentCardID, cardIDs);
    }

    public static void useSpecialPower(int x, int y) {
    }

    public static void showHand() {
        game.showHand();
    }

    public static void insertCard(String name, int x, int y) {
        game.insertCard(name, x, y);
    }

    public static void endTurn() {
        game.endTurn();
    }

    public static void showAllCollectibles() {
    }

    public static void selectCollectible(String name) {
        game.selectCollectibleItem(name);
    }

    public static void showCollectibleInfo() {
    }

    public static void useCollectible(int x, int y) {
        game.applyCollectible(x, y);
    }

    public static void showNextCard() {
        game.showNextCardInDeck();
    }

    public static String getString() {
        return "";
    }

    //start a single player game with a specified AI
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

    //start a single player game with with an AI with a specific deck
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

    //start a multiplayer game with a selected account
    public static boolean startGame(Account secondAccount, int mode, int numberOfFlags) {
        if (checkGame(mode, numberOfFlags)) return false;
        hasAI = false;
        players[1] = secondAccount.getPlayer();
        players[0] = getAccount().getPlayer();
        GameMenu.game = new Game(getAccount(), secondAccount);
        game.initiateGame();
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

    public static void select(String collectionItemDescriptor) {
        if (collectionItemDescriptor.contains("_"))
            selectCard(collectionItemDescriptor);
        else
            selectCollectible(collectionItemDescriptor);
    }
}
