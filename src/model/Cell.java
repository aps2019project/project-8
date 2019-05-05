package model;

import java.util.ArrayList;

public class Cell {

    private Object content;
    private Player objectOwner;
    private ArrayList<Buff> effects;
    private int numberOfFlags;

    public Cell() {
        content = null;
        objectOwner = null;
        effects = new ArrayList<>();
        numberOfFlags = 0;
    }

    void addEffect(Buff buff) {
        effects.add(buff);
    }

    void passTurn() {
    }

    Object getContent() {
        return this.content;
    }

    void modifyFlags(int number) {

    }

    ArrayList<Buff> getEffects() {
        return this.effects;
    }

    void setContent(Object content) {
        this.content = content;
    }

    public Player getObjectOwner() {
        return objectOwner;
    }
}
