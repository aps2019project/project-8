package model;

import java.util.ArrayList;

public class AI {
    private static ArrayList<AI> listOfAIs = new ArrayList<>();
    private Deck deck = new Deck("");
    private GameType gameType = GameType.KILL_OPPONENT_HERO;

    public static ArrayList<AI> getAIs() {
        return null;
    }

    public static AI getAI(int number) {
        return listOfAIs.get(number - 1);
    }

    public Deck getDeck() {
        return deck;
    }

    public GameType getMode() {
        return gameType;
    }

    public Player getPlayer() {
        return new Player();
    }
}
