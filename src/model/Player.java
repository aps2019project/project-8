package model;

import java.util.ArrayList;

public class Player {
    private Hand hand;
    private Deck deck;
    private Hero hero;
    private ArrayList<Unit> units;
    private int mana;
    private ArrayList<Card> graveYard;
    private ArrayList<Collectible> collectibles;

    public Hero getHero() {
        return hero;
    }
    private void getCurrentDeck() {

    }

    public void initiateHand() {
        deck.shuffle();

        hand.addCard(deck.getNextCard());
    }

    private Hero initialize() {
        return new Hero();
    }

    Card findCard(String cardName) {
        return hand.findCard(cardName);
    }

    int getMana() {
        return mana;
    }

    void decreaseMana(int mana) {
        this.mana -= mana;
    }

}
