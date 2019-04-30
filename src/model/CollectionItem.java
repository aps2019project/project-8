package model;

public abstract class CollectionItem {
    private int price;
    private int collectionItemID;
    private String name;

    protected CollectionItem(int price, int collectionItemID, String name) {
        this.price = price;
        this.collectionItemID = collectionItemID;
        this.name = name;
    }

    protected CollectionItem() {}

    public void setCollectionItemID(int collectionItemID) {
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

    public int getCollectionItemID() {
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
