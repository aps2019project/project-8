package model;

import java.util.HashMap;

public class Collection {
    private HashMap<String, CollectionItem> collectionItems;
    public CollectionItem getCollectionItem(String collectionItemID) {
        return collectionItems.get(collectionItemID);
    }

    public void removeCollectionItem(String collectionItemID) {
        collectionItems.remove(collectionItemID);
    }

    public void addCollectionItem(CollectionItem collectionItem) {
        collectionItems.put(getNewID(), collectionItem);
    }

    private String getNewID() {
        return "ADFSDFS";
    }
}
