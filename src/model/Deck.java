package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

public class Deck {
    private static final int DECK_CARD_CAPACITY = 20;
    private Queue<Card> cards = new ArrayDeque<>();
    private Item deckItem = null;
    private Hero deckHero = null;
    private String deckName = null;
    private int numberOfTabsOnToString = 0;
    private ArrayList<String> collectionItemsIDs = new ArrayList<>();

    Deck(String deckName) {
        this.deckName = deckName;
    }

    public void removeCollectionItem(String collectionItemID, CollectionItem collectionItem) {
        cards.remove(collectionItem);
        collectionItemsIDs.remove(collectionItemID);
    }

    public boolean isValid() {
        return (deckHero != null) && (cards.size() == DECK_CARD_CAPACITY);
    }

    void shuffle() {
        ArrayList<Card> shuffled = new ArrayList<>(cards);
        Collections.shuffle(shuffled);
        cards.clear();
        cards.addAll(shuffled);
    }

    public String getDeckName() {
        return deckName;
    }

    public Hero getHero() {
        return deckHero;
    }

    Card getNextCard() {
        return cards.peek();
    }

    public void addCollectionItem(CollectionItem collectionItem, String collectionItemID) {
        if (collectionItem instanceof Hero) {
            deckHero = (Hero) collectionItem;
        } else if (collectionItem instanceof Item) {
            deckItem = (Item) collectionItem;
        } else {
            cards.add((Card) collectionItem);
        }
        collectionItemsIDs.add(collectionItemID);
    }

    public boolean hasHero() {
        return deckHero != null;
    }

    public boolean hasItem() {
        return deckItem != null;
    }

    public void removeCollectionItem(CollectionItem collectionItem, String collectionItemID) {
        if (collectionItem instanceof Hero) {
            deckHero = null;
        } else if (collectionItem instanceof Item) {
            deckItem = null;
        } else {
            cards.remove(collectionItem);
        }
        collectionItemsIDs.remove(collectionItemID);
    }

    public boolean isFull() {
        return cards.size() == DECK_CARD_CAPACITY;
    }


    public boolean hasCollectionItem(String collectionItemID) {
        if (collectionItemsIDs == null)
            System.err.println("FUCK");
        return collectionItemsIDs.contains(collectionItemID);
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        appendTabs(stringBuilder, numberOfTabsOnToString);
        stringBuilder.append("Hero :\n");
        if (hasHero()) {
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

        return stringBuilder.toString();
    }

    public Card findCard(String cardName) {
        for (Card card : cards) {
            if (card.getName().equals(cardName)) {
                return card;
            }
        }
        return null;
    }
}
