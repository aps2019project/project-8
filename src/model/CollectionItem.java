package model;

public class CollectionItem {
    protected int price;
    protected String collectionItemID;
    protected String name;
    protected String description;

    // Main constructor
    public CollectionItem(int price, String collectionItemID, String name, String description) {
        this.price = price;
        this.collectionItemID = collectionItemID;
        this.name = name;
        this.description = description;
    }

    // Copy constructor
    public CollectionItem(CollectionItem collectionItem) {
        this.price = collectionItem.price;
        this.collectionItemID = collectionItem.collectionItemID;
        this.name = collectionItem.name;
        this.description = collectionItem.description;
    }

    protected CollectionItem() {}

    public void setCollectionItemID(String collectionItemID) {
        this.collectionItemID = collectionItemID;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public String getID() {
        return collectionItemID;
    }

    public boolean equalsName(String collectionItemName) {
        return name.equals(collectionItemName);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
