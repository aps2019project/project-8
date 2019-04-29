package model;

import java.util.*;
import java.util.Map;

public class Collection {
    private int newID = 0;
    private HashMap<String, CollectionItem> collectionItems = new HashMap<>();
    private HashMap<String, Deck> decks = new HashMap<>();

    public CollectionItem getCollectionItemByID(String collectionItemID) {
        return collectionItems.get(collectionItemID);
    }

    public void removeCollectionItem(String collectionItemID) {
        collectionItems.remove(collectionItemID);
    }

    public void addCollectionItem(CollectionItem collectionItem) {
        collectionItems.put(getNewID(), collectionItem);
    }

    private String getNewID() {
        return String.valueOf(newID++);
    }

    public ArrayList<String> getCollectionItemIDs(String collectionItemName) {
        ArrayList<String> collectionItemIDs = new ArrayList<>();
        Iterator<Map.Entry<String, CollectionItem>> iterator = collectionItems.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = iterator.next();
            if (((CollectionItem) pair.getValue()).getName().equals(collectionItemName))
                collectionItemIDs.add(String.valueOf(pair.getKey()));
        }
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
        return decks.containsKey(name);
    }

    public Deck getDeck(String deckName) {
        return decks.get(deckName);
    }

    public ArrayList<Deck> getDecks() {
        return new ArrayList<>(decks.values());
    }
    public CollectionItem getCollectionItem(String collectionItemID) {
        return collectionItems.get(collectionItemID);
    }

    public boolean hasCollectionItem(String collectionItemID) {
        return collectionItems.containsKey(collectionItemID);
    }

}
