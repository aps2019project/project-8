package model;

public class Card extends CollectionItem {
    private int manaCost;

    public Card(int price, String id, String name, int manaCost) {
        super(price, id, name);
        this.manaCost = manaCost;
    }

    public Card(Card card) {
        this(card.getPrice(), card.getID(), card.getName(), card.getManaCost());
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
