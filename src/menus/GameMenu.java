package menus;

import model.AI;
import model.Player;

public class GameMenu extends InGameMenu {
    private static boolean hasAI;
    private static Player[] players;

    /*    public static  void setGame(Game game) {
                menus.GameMenu.game = game;
            }*/
    public static String[] help() {
        return null;
    }

    public static void showGameInfo() {
    }

    public static void showMinions(String who) {
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
    }

    public static void endTurn() {
    }

    public static void showAllCollectibles() {
    }

    public static void selectCollectibe(String name) {
    }

    public static void showCollectible(String name) {
    }

    public static void useCollectible(int x, int y) {
    }

    public static void showNextCard() {
    }

    public static String getString() {
        return "";
    }

    public static void startGame(AI ai) {
        hasAI = true;
        players[1] = ai.getPlayer();
    }
}
