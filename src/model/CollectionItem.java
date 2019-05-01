package model;

public abstract class CollectionItem {
    private int price;
    private String id;
    private String name;

    protected CollectionItem(int price, String id, String name) {
        this.price = price;
        this.id = id;
        this.name = name;
    }

    protected CollectionItem() {}

    public void setCollectionItemID(int collectionItemID) {
        this.id = Integer.toString(collectionItemID);
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
        return id;
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
