package model;

import java.util.ArrayList;

public class Hand{
    private static final int HAND_CARD_CAPACITY = 5;
    private ArrayList<Card> cards;

    boolean isFull() {
        return cards.size() == HAND_CARD_CAPACITY;
    }

    void addCard(Card card) {
        cards.add(card);
    }

    void removeCard(Card card) {
        cards.remove(card);
    }

    public String toString() {
        return "blah blah";
    }
}
