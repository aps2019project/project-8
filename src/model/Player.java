package model;

import java.util.ArrayList;

public class Player {
    private Hand hand;
    private Deck deck;
    private Hero hero;
    private ArrayList<Unit> units;
    private int mana;
    private ArrayList<Card> graveYard;
    private ArrayList<Collectible> collectibles;

    private void getCurrentDeck() {

    }

    private Hero initialize() {
        return new Hero();
    }

}
