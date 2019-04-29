package model;

public class Item extends CollectionItem {
    private String description;

    public Item(int price, int collectionItemID, String name) {
        super(price, collectionItemID, name);
        description = "desc";
    }

    protected Item() {}

    @Override
    public String toString() {
        return getName();
    }
}
