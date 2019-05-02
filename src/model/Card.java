package model;

public class Card extends CollectionItem {
    protected int manaCost;

    // Main constructor
    protected Card(CollectionItem collectionItem, int manaCost) {
        super(collectionItem);
        this.manaCost = manaCost;
    }

    // Copy Constructor
    public Card(Card card) {
        this.price = card.price;
        this.collectionItemID = card.collectionItemID;
        this.name = card.name;
        this.manaCost = card.manaCost;
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
