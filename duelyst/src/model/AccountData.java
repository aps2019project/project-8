package model;

import java.util.Map;
import java.util.*;

public class AccountData {

    private Collection collection = new Collection();
    private Deck mainDeck = null;

    public boolean hasThreeItems() {
        return collection.hasThreeItems();
    }

    public Collection getCollection() {
        return collection;
    }

    public HashMap<String, CollectionItem> getCollectionItems() {
        return collection.getCollectionItems();
    }

    public Deck getDeck(String deckName) {
        return collection.getDeck(deckName);
    }

    public Deck getMainDeck() {
        return mainDeck;
    }

    public void setMainDeck(Deck mainDeck) {
        this.mainDeck = mainDeck;
    }

    public Player getPlayer() {
        return new Player(getMainDeck());
    }

    public ArrayList<Deck> getDecks() {
        return collection.getDecks();
    }

    public void importDeck(Deck deck) throws Exception {
        HashMap<String, CollectionItem> collectionItems = new HashMap<>(collection.getCollectionItems());
        String deckName = deck.getDeckName();
        int index = 0;
        while (collection.hasDeck(deckName + index))
            index++;
        Deck newDeck = new Deck(deck.getDeckName() + index);
        Optional<Map.Entry<String, CollectionItem>> hero = collectionItems.entrySet().stream().filter(e -> e.getValue().
                equals(deck.getHero())).findAny();
        Exception importedCardException = new Exception("Imported card not found.");
        if (hero.isPresent()) {
            Map.Entry<String, CollectionItem> heroEntry = hero.get();
            newDeck.addCollectionItem(heroEntry.getValue(), heroEntry.getKey());
        } else
            throw importedCardException;
        if (deck.getDeckUsableItem() != null) {
            Optional<Map.Entry<String, CollectionItem>> usableItem = collectionItems.entrySet().stream().filter(e -> e.getValue().
                    equals(deck.getDeckUsableItem())).findAny();
            if (usableItem.isPresent()) {
                Map.Entry<String, CollectionItem> itemEntry = usableItem.get();
                newDeck.addCollectionItem(itemEntry.getValue(), itemEntry.getKey());
            } else
                throw importedCardException;
        }
        StringBuilder problem = new StringBuilder();
        deck.getCards().forEach(o -> {
            Optional<Map.Entry<String, CollectionItem>> card = collectionItems.entrySet().stream().filter(e -> e.getValue().
                    equals(o)).findAny();
            if (card.isPresent()) {
                Map.Entry<String, CollectionItem> cardEntry = card.get();
                newDeck.addCollectionItem(cardEntry.getValue(), cardEntry.getKey());
                collectionItems.remove(cardEntry.getKey());
            } else
                problem.append("problem");
        });
        if (problem.length() != 0)
            throw importedCardException;
        collection.importDeck(newDeck);
    }
}
