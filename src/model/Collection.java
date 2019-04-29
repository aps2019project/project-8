package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Collection {
    private int newID = 0;
    private HashMap<String, CollectionItem> collectionItems = new HashMap<>();

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

    public HashMap<String, CollectionItem> getCollectionItems() {
        return collectionItems;
    }
}
