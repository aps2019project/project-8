package model;

import java.util.ArrayList;

public class Player {
    private Hand hand;
    private Deck deck;
    private ArrayList<Unit> units;
    private int mana;
    private ArrayList<Card> graveYard;
    private ArrayList<Collectible> collectibles;

    public Player(Deck deck) {
        this.deck = deck;
    }

    public Hero getHero() {
        return getDeck().getHero();
    }

    public Usable getUsable() {
        return getDeck().getDeckUsableItem();
    }

    public ArrayList<Card> getGraveYard() {
        return graveYard;
    }

    public void initiateHand() {
        deck.shuffle();

        hand.addCard(deck.getNextCard());
    }

    private Hero initialize() {
        return new Hero();
    }

    Card findCardInHand(String cardName) {
        return hand.findCard(cardName);
    }

    Card findCardInDeck(String cardName) {
        return deck.findCard(cardName);
    }

    Card findCardInGraveyard(String cardName) {
        for (Card card : graveYard) {
            if (card.getName().equals(cardName)) {
                return card;
            }
        }
        return null;
    }

    public Deck getDeck() {
        return deck;
    }

    public Hand getHand() {
        return hand;
    }

    int getMana() {
        return mana;
    }

    void decreaseMana(int mana) {
        this.mana -= mana;
    }

}
