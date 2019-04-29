package model;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;

public class Deck {
    private static final int DECK_CARD_CAPACITY = 20;
    private Queue<Card> cards = new ArrayDeque<>();
    private Item deckItem = null;
    private Hero deckHero = null;
    private String deckName = null;
    private int numberOfTabsOnToString = 0;

    Deck(String deckName) {
        this.deckName = deckName;
    }

    boolean isValid() {
        return (deckItem != null) && (deckHero != null) && (cards.size() == DECK_CARD_CAPACITY);
    }

    void shuffle() {
    }

    public String getDeckName() {
        return deckName;
    }

    Hero getHero() {
        return deckHero;
    }

    Card getNextCard() {
        return cards.peek();
    }

    public void addCollectionItem(CollectionItem collectionItem) {
        if (collectionItem instanceof Hero) {
            deckHero = (Hero) collectionItem;
        } else if (collectionItem instanceof Item) {
            deckItem = (Item) collectionItem;
        } else {
            cards.add((Card) collectionItem);
        }
    }

    public boolean hasHero() {
        return deckHero != null;
    }

    public boolean hasItem() {
        return deckItem != null;
    }

    public void removeCollectionItem(CollectionItem collectionItem) {
        if (collectionItem instanceof Hero) {
            deckHero = null;
        } else if (collectionItem instanceof Item) {
            deckItem = null;
        } else {
            cards.remove((Card) collectionItem);
        }
    }

    public boolean isFull() {
        return cards.size() == DECK_CARD_CAPACITY;
    }


    public boolean hasCollectionItem(CollectionItem collectionItem) {
        if (collectionItem instanceof Hero) {
            return deckHero != null && !collectionItem.getName().equals(deckHero.getName());
        } else if (collectionItem instanceof Item) {
            return deckItem != null && !collectionItem.getName().equals(deckItem.getName());
        } else {
            for (Card card : cards) {
                if (card.getName().equals(collectionItem.getName())) {
                    return true;
                }
            }
            return false;
        }
    }

    public void incrementTabsOnToString() {
        numberOfTabsOnToString++;
    }

    public void decrementTabsOnToString() {
        numberOfTabsOnToString--;
    }

    private static void appendTabs(StringBuilder stringBuilder, int d) {
        for (int i = 0; i < d; i++) {
            stringBuilder.append("\t");
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        appendTabs(stringBuilder, numberOfTabsOnToString);
        stringBuilder.append("Heros :\n");
        if (hasHero()) {
            stringBuilder.append("1 : ");
            appendTabs(stringBuilder, numberOfTabsOnToString + 1);
            stringBuilder.append(deckHero.toString());
            stringBuilder.append("\n");
        }

        appendTabs(stringBuilder, numberOfTabsOnToString);
        stringBuilder.append("Items :\n");
        if (hasItem()) {
            appendTabs(stringBuilder, numberOfTabsOnToString + 1);
            stringBuilder.append("1 : ");
            stringBuilder.append(deckItem.toString());
            stringBuilder.append("\n");
        }

        appendTabs(stringBuilder, numberOfTabsOnToString);
        stringBuilder.append("Cards :\n");
        Object[] cardArray = cards.toArray();
        for (int i = 1; i <= cardArray.length; i++) {
            appendTabs(stringBuilder, numberOfTabsOnToString + 1);
            stringBuilder.append(i);
            stringBuilder.append(" : ");
            stringBuilder.append(cardArray[i - 1].toString());
            stringBuilder.append("\n");
        }

        appendTabs(stringBuilder, numberOfTabsOnToString + 1);
        stringBuilder.append(deckHero.toString());
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

}
