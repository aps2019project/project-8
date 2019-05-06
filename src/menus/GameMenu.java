package menus;

import model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GameMenu extends InGameMenu {
    private static final String[] commands = {
            "-------Game Menu-------",
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
            "Exit",
            "Show menu"
    };
    private static final int NUMBER_OF_PLAYERS = 2;
    private static String[] endGame = {"End Game"};
    private static boolean hasAI;
    private static Player[] players = new Player[NUMBER_OF_PLAYERS];
    private static int numberOfFlags;
    private static Game game;
    private static Account secondAccount = null;

    public static void help(boolean gameEnded) {
        if (!gameEnded)
            view.showHelp(commands);
        else
            view.showHelp(endGame);
    }

    public static void showGameInfo() {
        game.showGameInfo();
    }

    public static void showMinions(int who) {
        if (who == 0)
            game.showMyMinions();
        else
            game.showOpponentMinions();
    }

    public static void showCardInfo(String cardID) {
        game.showCardInfo(cardID);
    }

    public static void selectCard(String cardID) {
        game.selectCard(cardID);
    }

    public static void moveUnit(int x, int y) {
        game.moveSelectedUnit(x - 1, y - 1);
    }

    public static void attackUnit(String cardID) {
        game.attackTargetCardWithSelectedUnit(cardID);
    }

    public static void attackCombo(String opponentCardID, String[] cardIDs) {
        game.attackCombo(opponentCardID, cardIDs);
    }

    public static void useSpecialPower(int x, int y) {
        game.useHeroSpecialPower(x - 1, y - 1);
    }

    public static void showHand() {
        game.showHand();
    }

    public static void insertCard(String name, int x, int y) {
        game.insertCard(name, x - 1, y - 1);
    }

    public static void endTurn() {
        game.endTurn();
    }

    public static void showAllCollectibles() {
        game.showAllCollectibles();
    }

    public static void selectCollectible(String name) {
        game.selectCollectibleItem(name);
    }

    public static void showCollectibleInfo() {
        game.showCollectibleInfo();
    }

    public static void useCollectible(int x, int y) {
        game.applyCollectible(x - 1, y - 1);
    }

    public static void showNextCard() {
        game.showNextCardInDeck();
    }

    public static String getString() {
        return "";
    }

    //start a single player game with a specified AI
    public static boolean startGame(int aiID) {
        if (checkAccount()) return false;
        AI ai = AI.get(aiID);
        if (ai == null) {
            view.showInvalidParametersError();
            return false;
        }
        game = new Game(getAccount(), ai, AI.getGameType(aiID), AI.getNumberOfFlags(aiID));
        ai.setGame(game);
        game.setPrize(AI.getGamePrize(aiID));
        game.initiateGame();
        hasAI = true;
        return true;
    }

    private static boolean checkAccount() {
        if (getAccount().getMainDeck() == null) {
            view.showNoMainDeckError();
            return true;
        }
        if (!getAccount().getMainDeck().isValid()) {
            view.showInvalidDeckError();
            return true;
        }
        return false;
    }

    //start a single player game with with an AI with a specific deck
    public static boolean startGame(String deckName, int mode, int numberOfFlags) {
        if (checkAccount()) return false;
        if (getAccount().getDeck(deckName) == null) {
            view.showDeckDoesNotExistError();
            return false;
        }
        if (!getAccount().getDeck(deckName).isValid()) {
            view.showInvalidDeckError();
            return false;
        }
        if (checkGameParameters(mode, numberOfFlags)) return false;
        AI ai = new AI(getAccount().getDeck(deckName));
        game = new Game(getAccount(), ai, GameType.get(mode), numberOfFlags);
        ai.setGame(game);
        game.initiateGame();
        hasAI = true;
        return true;
    }

    //start a multiplayer game with a selected account
    public static boolean startGame(Account secondAccount, int mode, int numberOfFlags) {
        if (checkAccount()) return false;
        if (checkGameParameters(mode, numberOfFlags)) return false;
        GameMenu.game = new Game(getAccount(), secondAccount, GameType.get(mode), numberOfFlags);
        game.initiateGame();
        hasAI = false;
        return true;
    }

    private static boolean checkGameParameters(int mode, int numberOfFlags) {
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

    public static void showOptions() {
        game.showAvailableOptions();
    }

    public static void checkGameCondition() {
        switch (game.getGameState()) {
            case WIN_FIRST_PLAYER:
                account.payMoney(game.getPrize());
                account.addMatch(new Match((hasAI ? null : secondAccount), Result.WIN, LocalDateTime.now()));
                if (!hasAI)
                    secondAccount.addMatch(new Match(account, Result.WIN, LocalDateTime.now()));
                secondAccount = null;
                break;
            case WIN_SECOND_PLAYER:
                account.addMatch(new Match((hasAI ? null : secondAccount), Result.WIN, LocalDateTime.now()));
                secondAccount.addMatch(new Match(account, Result.WIN, LocalDateTime.now()));
                secondAccount = null;
                break;
            default:
                return;
        }
    }
}
