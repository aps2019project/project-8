package model;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

public class Deck {
    private static final int DECK_CARD_CAPACITY = 20;
    private Queue<Card> cards = new ArrayDeque<>();
    private Usable deckUsableItem = null;
    private Hero deckHero = null;
    private String deckName = null;
    private int numberOfTabsOnToString = 0;
    private ArrayList<String> collectionItemsIDs = new ArrayList<>();

    Deck(String deckName) {
        this.deckName = deckName;
    }

    public Deck(Deck deck) {
        if (deck.deckUsableItem != null)
            deckUsableItem = new Usable(deck.deckUsableItem);
        new ArrayList<>(deck.cards).forEach(o -> cards.add(getCopy(o)));
        if (deck.deckHero != null)
            deckHero = new Hero(deck.deckHero);
        deckName = deck.deckName;
        numberOfTabsOnToString = deck.numberOfTabsOnToString;
        collectionItemsIDs.addAll(deck.collectionItemsIDs);
    }

    private static void appendTabs(StringBuilder stringBuilder, int d) {
        for (int i = 0; i < d; i++) {
            stringBuilder.append("\t");
        }
    }

    private Card getCopy(Card card) {
        if (card instanceof Minion)
            return new Minion((Minion) card);
        if (card instanceof SpellCard)
            return new SpellCard((SpellCard) card);
        return null;
    }

    public void removeCollectionItem(String collectionItemID, CollectionItem collectionItem) {
        if (collectionItem instanceof Usable)
            deckUsableItem = null;
        else if (collectionItem instanceof Hero)
            deckHero = null;
        cards.remove(collectionItem);
        collectionItemsIDs.remove(collectionItemID);
    }

    public void deleteNextCard() {
        cards.remove();
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

    public Usable getDeckUsableItem() {
        return deckUsableItem;
    }

    Card getNextCard() {
        return cards.peek();
    }

    public void addCollectionItem(CollectionItem collectionItem, String collectionItemID) {
        if (collectionItem instanceof Hero) {
            deckHero = (Hero) collectionItem;
        } else if (collectionItem instanceof Item) {
            deckUsableItem = (Usable) collectionItem;
        } else {
            cards.add((Card) collectionItem);
        }
        collectionItemsIDs.add(collectionItemID);
    }

    public boolean hasHero() {
        return deckHero != null;
    }

    public boolean hasItem() {
        return deckUsableItem != null;
    }

    public void removeCollectionItem(CollectionItem collectionItem, String collectionItemID) {
        if (collectionItem instanceof Hero) {
            deckHero = null;
        } else if (collectionItem instanceof Item) {
            deckUsableItem = null;
        } else {
            cards.remove(collectionItem);
        }
        collectionItemsIDs.remove(collectionItemID);
    }

    public boolean isFull() {
        return cards.size() == DECK_CARD_CAPACITY;
    }

    public boolean hasCollectionItem(String collectionItemID) {
//        if (collectionItemsIDs == null)
//            System.err.println("FUCK");
        return collectionItemsIDs.contains(collectionItemID);
    }

    public void incrementTabsOnToString() {
        numberOfTabsOnToString++;
    }

    public void decrementTabsOnToString() {
        numberOfTabsOnToString--;
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
        stringBuilder.append("Item :\n");
        if (hasItem()) {
            appendTabs(stringBuilder, numberOfTabsOnToString + 1);
            stringBuilder.append(deckUsableItem.toString());
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

    public Queue<Card> getCards() {
        return cards;
    }
}

