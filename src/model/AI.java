package model;

import java.util.ArrayList;
// SmsS is great
public class AI {
    private static ArrayList<AI> listOfAIs = new ArrayList<>();
    private Deck deck = new Deck("");
    private GameType gameType = GameType.KILL_OPPONENT_HERO;

    public AI(Deck deck, GameType gameType) {
        this.deck = deck;
        this.gameType = gameType;
    }

    public static ArrayList<AI> getAIs() {
        return listOfAIs;
    }

    public static AI getAI(int number) {
        return (number <= listOfAIs.size() ? listOfAIs.get(number - 1) : null);
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
