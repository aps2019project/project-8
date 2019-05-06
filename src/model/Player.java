package model;

import java.util.ArrayList;

public class Player {
    private Hand hand;
    private Deck deck;
    private ArrayList<Unit> units = new ArrayList<>();
    private int mana;
    private ArrayList<Card> graveYard;
    private ArrayList<Collectible> collectibles = new ArrayList<>();
    private int numberOfFlagTurns = 0;

    String name = "";

    public Player(Deck deck) {
        this.deck = new Deck(deck);
        hand = new Hand();
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public Hero getHero() {
        return getDeck().getHero();
    }

    public Usable getUsable() {
        return getDeck().getDeckUsableItem();
    }

    public ArrayList<Card> getGraveYard() {
        return graveYard;
    }

    public void initiateHand() {
        deck.shuffle();

        for (int i = 0; i < 5; i++) {
            hand.addCard(deck.getNextCard());
            deck.deleteNextCard();
        }
    }
    private Hero initialize() {
        return new Hero();
    }

    Card findCardInHand(String cardName) {
        return hand.findCard(cardName);
    }

    Card findCardInDeck(String cardName) {
        return deck.findCard(cardName);
    }

    Card findCardInGraveyard(String cardID) {
        for (Card card : graveYard) {
            if (card.getCollectionItemID().equals(cardID)) {
                return card;
            }
        }
        return null;
    }

    public Deck getDeck() {
        return deck;
    }

    public Hand getHand() {
        return hand;
    }

    int getMana() {
        return mana;
    }

    void decreaseMana(int mana) {
        this.mana -= mana;
    }

    public void addMana(int mana) {
        this.mana += mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getNumberOfFlags() {
        int numberOfFlags = 0;
        for (Unit unit : units) {
            numberOfFlags += unit.getNumberOfFlags();
        }
        return numberOfFlags;
    }

    public int getNumberOfFlagTurns() {
        return numberOfFlagTurns;
    }

    public void addUnit(Unit unit) {

        //
        System.err.println(name + " ADDING: " + unit);
        //

        units.add(unit);
    }

    public ArrayList<Collectible> getCollectibles() {
        return collectibles;
    }

    public void addCollectible(Collectible collectible) {
        collectibles.add(collectible);
    }

    public void addToGraveyard(SpellCard spellCard) {
        graveYard.add(spellCard);
    }

    public Collectible getCollectible(String collectibleID) {
        for (Collectible collectible : collectibles) {
            if (collectible.getCollectionItemID().equals(collectibleID))
                return collectible;
        }
        return null;
    }

    public Unit getUnit(String unitID) {
        for (Unit unit : units) {
            if (unit.getCollectionItemID().equals(unitID))
                return unit;
        }
        return null;
    }
}
