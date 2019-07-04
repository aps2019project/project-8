package model;

public enum Result {
    WIN,
    LOSE;

    @Override
    public String toString() {
        switch (this) {
            case WIN:
                return "Win";
            default:
                return "Lose";
        }
    }
}

