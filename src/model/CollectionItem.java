package model;

abstract public class CollectionItem {
    private int price;
    private int collectionItemID;
    private String name;

    public int getPrice() {
        return price;
    }

    public int getCollectionItemID() {
        return collectionItemID;
    }

    public boolean equalsName(String collectionItemName) {
        return name.equals(collectionItemName);
    }

    public String getName() {
        return name;
    }
}
