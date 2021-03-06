package model;

import java.util.ArrayList;

public class Hand{
    private static final int HAND_CARD_CAPACITY = 5;
    private ArrayList<Card> cards = new ArrayList<>();

    boolean isFull() {
        return cards.size() == HAND_CARD_CAPACITY;
    }

    void addCard(Card card) {
        cards.add(card);
    }

    void removeCard(Card card) {
        cards.remove(card);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    Card findCard(String cardName) {
        for (Card card : cards) {
            if (card.getName().contentEquals(cardName)) {
                return card;
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hand info:\n");
        for (Card card : cards) {
            stringBuilder.append("\t");
            if (card == null) {
                stringBuilder.append("is null");
                continue;
            }
            stringBuilder.append(card.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}