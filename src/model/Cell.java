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

    void setContent(Object content) {
        this.content = content;
    }

    Object getContent() {
        return this.content;
    }

    public boolean hasContent() {
        return content != null;
    }

    void modifyFlags(int number) {

    }

    ArrayList<Buff> getEffects() {
        return this.effects;
    }

    void setContent(Object content, Player player) {
        this.content = content;
        objectOwner = player;
    }

    public Player getObjectOwner() {
        return objectOwner;
    }

    public void setObjectOwner(Player player) {
        objectOwner = player;
    }




    @Override
    public String toString() {
        return (content == null ? "NONE" : "FULL");
    }

    public int getNumberOfFlags() {
        return numberOfFlags;
    }

    public void addFlag(int i) {
        numberOfFlags += i;
    }

    public void decreaseNumberOfFlags() {
        numberOfFlags--;
        if (numberOfFlags < 0) {
            numberOfFlags = 0;
        }
    }
}
