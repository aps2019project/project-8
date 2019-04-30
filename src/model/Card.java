package model;

public class Card extends CollectionItem {
    private int manaCost;

    public Card(int price, int collectionItemID, String name, int manaCost) {
        super(price, collectionItemID, name);
        this.manaCost = manaCost;
    }

    public Card(Card card) {
        this(card.getPrice(), card.getCollectionItemID(), card.getName(), card.getManaCost());
    }

    public Card() {}

    public int getManaCost() {
        return manaCost;
    }

    public int getPrice() {
        return super.getPrice();
    }
}
