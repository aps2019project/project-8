package model;

import java.util.ArrayList;

public class Cell {
    private Object content;
    private ArrayList<Buff> effects = new ArrayList<>();
    private int numberOfFlags;

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
}
