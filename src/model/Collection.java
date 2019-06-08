package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Collection {
    private int newID = 0;
    private HashMap<String, CollectionItem> collectionItems = new HashMap<>();
    private HashMap<String, Deck> decks = new HashMap<>();

    public CollectionItem getCollectionItemByID(String collectionItemID) {
        return collectionItems.get(collectionItemID);
    }

    public CollectionItem getCollectionItem(String collectionItemID) {
        return collectionItems.get(collectionItemID);
    }

    public void addCollectionItem(CollectionItem collectionItem) {
        collectionItems.put(getNewID(), collectionItem);
    }

    private String getNewID() {
        return String.valueOf(newID++);
    }

    public ArrayList<String> getCollectionItemIDs(String collectionItemName) {
        ArrayList<String> collectionItemIDs = new ArrayList<>();
        collectionItems.forEach((collectionItemID, collectionItem) -> {
            if (collectionItem.getName().equals(collectionItemName))
                collectionItemIDs.add(collectionItemID);
        });
        return collectionItemIDs;
    }

    HashMap<String, CollectionItem> getCollectionItems() {
        return collectionItems;
    }

    public void createDeck(String deckName) { // deck hasn't been created before
        decks.put(deckName, new Deck(deckName));
    }

    public void deleteDeck(String deckName) {
        decks.remove(deckName);
    }

    public boolean hasDeck(String name) {
        return decks.keySet().stream().anyMatch(key -> key.equalsIgnoreCase(name));
    }

    public Deck getDeck(String deckName) {
        return decks.get(deckName);
    }

    public ArrayList<Deck> getDecks() {
        return new ArrayList<>(decks.values());
    }

    public boolean hasCollectionItem(String collectionItemID) {
        return collectionItems.containsKey(collectionItemID);
    }

    public boolean hasThreeItems() {
        int numberOfItems = 0;
        for (CollectionItem collectionItem : new ArrayList<>(collectionItems.values())) {
            if (collectionItem instanceof Item)
                numberOfItems++;
        }
        return numberOfItems == 3;
    }

    public void removeCollectionItem(String collectionItemID) {
        CollectionItem collectionItem = getCollectionItem(collectionItemID);
        collectionItems.remove(collectionItemID);
        decks.values().forEach(o -> o.removeCollectionItem(collectionItemID, collectionItem));
    }

    public void importDeck(Deck newDeck) {
        decks.put(newDeck.getDeckName(), newDeck);
    }
}
