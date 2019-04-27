package model;

import java.util.ArrayList;
import java.util.Queue;

public class Deck {
    private static final int DECK_CARD_CAPACITY = 20;
    private Queue<Card> cards;
    private Item deckItem;
    private Hero deckHero;
    private String deckName;

    boolean isValid() {
        return (deckItem != null) && (deckHero != null) && (cards.size() == DECK_CARD_CAPACITY);
    }

    void shuffle() {
    }

    Hero getHero() {
        return deckHero;
    }

    Card getNextCard() {
        return cards.peek();
    }

    void addCard(Card card) {

    }

    void removeCard(Card card) {

    }

    public String toString() {
        return "blah blah!";
    }

}
