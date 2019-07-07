package model;

public enum GameType {
    KILL_OPPONENT_HERO,
    HOLD_THE_FLAG,
    COLLECT_THE_FLAGS;

    public static GameType get(int mode) {
        switch (mode) {
            case 1:
                return KILL_OPPONENT_HERO;
            case 2:
                return HOLD_THE_FLAG;
            default:
                return COLLECT_THE_FLAGS;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case KILL_OPPONENT_HERO:
                return "Kill opponent hero";
            case HOLD_THE_FLAG:
                return "Hold the flag";
            default:
                return "Collect the flags";
        }
    }
}
