package model;

public class Card extends CollectionItem {
    private int manaCost;

    public Card(int price, int collectionItemID, String name, int manaCost) {
        super(price, collectionItemID, name);
        this.manaCost = manaCost;
    }

    protected Card() {}

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public int getPrice() {
        return super.getPrice();
    }
}
