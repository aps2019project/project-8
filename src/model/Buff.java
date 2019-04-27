package model;

import java.util.ArrayList;

public class Buff {
    private int duration;
    private int holy;
    private int power;
    private int poison;
    private int weaknessAP;
    private int weaknessHP;
    private boolean stun;
    private boolean disarm;
    private int unholy;


    public void decrementDuration() {
        duration--;
        if (duration < 0) { // ?

        }
    }

    ArrayList<Integer> getBuff() {
        ArrayList<Integer> characteristics;
        characteristics = new ArrayList<Integer>(0);
//        characteristics.add(duration);
//        characteristics.add()
        return characteristics;
    }

}
