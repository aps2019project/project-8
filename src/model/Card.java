package model;


public class Card extends CollectionItem {
    protected int manaCost;
    private Player player = null;

    // Main constructor
    public Card(CollectionItem collectionItem, int manaCost) {
        super(collectionItem);
        this.manaCost = manaCost;
    }

    // Copy Constructor
    public Card(Card card) {
        super(card);
        this.manaCost = card.manaCost;
    }

    protected Card() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

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

