package menus;

import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
    private static AccountUser secondAccount = null;

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
        GraveyardMenu.setGame(game);
        return true;
    }

    private static boolean checkAccount() {
        if (getAccount().getData().getMainDeck() == null) {
            view.showNoMainDeckError();
            return true;
        }
        if (!getAccount().getData().getMainDeck().isValid()) {
            view.showInvalidMainDeckError();
            return true;
        }
        return false;
    }

    //start a single player game with with an AI with a specific deck
    public static boolean startGame(String deckName, int mode, int numberOfFlags) {

        if (checkAccount()) return false;
        if (getAccount().getData().getDeck(deckName) == null) {
            view.showDeckDoesNotExistError();
            return false;
        }
        if (!getAccount().getData().getDeck(deckName).isValid()) {
            view.showInvalidMainDeckError();
            return false;
        }
        if (checkGameParameters(mode, numberOfFlags))
            return false;
        AI ai = new AI(getAccount().getData().getDeck(deckName));
        game = new Game(getAccount(), ai, GameType.get(mode), numberOfFlags);
        ai.setGame(game);
        game.initiateGame();
        hasAI = true;
        GraveyardMenu.setGame(game);
        return true;

    }

    //start a multiplayer game with a selected account
    public static boolean startGame(AccountUser secondAccount, int mode, int numberOfFlags) {
        if (checkAccount()) return false;
        if (checkGameParameters(mode, numberOfFlags)) return false;
        GameMenu.game = new Game(getAccount(), secondAccount, GameType.get(mode), numberOfFlags);
        game.initiateGame();
        hasAI = false;
        GraveyardMenu.setGame(game);
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

    public static void select(String collectionItemID) {
        if (game.hasCollectible(collectionItemID))
            game.selectCollectible(collectionItemID);
        else
            game.selectCard(collectionItemID);
    }

    public static void showOptions() {
        ArrayList<Card>[] availableOptions = game.showAvailableOptions();
        view.showUnitsReadyToMove(availableOptions[0]);
        view.showUnitsAvailableForAttack(availableOptions[1]);
        view.showCardsReadyToBePlayed(availableOptions[2]);
    }

    public static void checkGameCondition() {
        switch (game.getGameState()) {
            case WIN_FIRST_PLAYER:
                account.payMoney(game.getPrize());
                account.addMatch(new Match((hasAI ? null : secondAccount), Result.WIN, LocalDateTime.now()));
                account.addWin();
                if (!hasAI) {
                    secondAccount.addMatch(new Match(account, Result.WIN, LocalDateTime.now()));
                }
                secondAccount = null;
                view.showWinner(account, game.getPrize());
                UI.endGame();
                break;
            case WIN_SECOND_PLAYER:
                account.addMatch(new Match((hasAI ? null : secondAccount), Result.WIN, LocalDateTime.now()));
                if (!(secondAccount == null)) {
                    secondAccount.addWin();
                    secondAccount.addMatch(new Match(account, Result.WIN, LocalDateTime.now()));
                    secondAccount.payMoney(game.getPrize());
                    view.showWinner(secondAccount, game.getPrize());
                } else
                    view.alertCPUWin();
                secondAccount = null;
                UI.endGame();
                break;
            default:
                return;
        }
    }

    public static void shengdeShow() {
        game.shengdeShow();
    }

    public static void kill(String targetID) {
        game.killInstantly(targetID);
    }

    public static void exit() {
        AccountUser account = game.getOtherAccount();
        if (account != null) {
            account.receiveMoney(game.getPrize());
        }
    }

    public static Game getGame() {
        return game;
    }

    public static void getDead() {
        if (getGame() == null)
            return;
        game.getDead();
    }
}
