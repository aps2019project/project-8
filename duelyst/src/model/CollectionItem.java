package model;

import menus.Menu;

public class CollectionItem {
    protected static final String DASH = " - ";
    private static final String EQUALS = "equals";
    protected int price;
    protected String collectionItemID;
    protected String name;
    protected String description;
    int count = 0;

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

    protected CollectionItem() {
    }

    public String getCollectionItemID() {
        return collectionItemID;
    }

    public void setCollectionItemID(String collectionItemID) {
        this.collectionItemID = collectionItemID;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public String getShopInfo() {
        return name + " " + Menu.getConnection().getItemCount(name) + "x  for " + getPrice() + "$";
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }

    public String showInfo() {
        return "name: " + name + " price: " + price + " description: " + description;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CollectionItem))
            return false;
        return collectionItemID.equals(((CollectionItem) obj).collectionItemID);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
