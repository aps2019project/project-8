package model;

import java.util.Iterator;

public class Item extends CollectionItem {
    protected String description;

    // Main constructor
    protected Item(CollectionItem collectionItem, String description) {
        super(collectionItem);
        this.description = "this is an empty description!! viva shengdebao!! haha!!!";
    }

    // Copy constructor
    public Item(Item item) {
        this.price = item.price;
        this.collectionItemID = item.collectionItemID;
        this.name = item.name;
        this.description = item.description;
    }

    protected Item() {}


    @Override
    public String toString() {
        return getName();
    }
}
