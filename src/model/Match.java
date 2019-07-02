package model;

import java.time.LocalDateTime;

public class Match {
    private AccountUser opponent;
    boolean win;
    LocalDateTime date;
    Result result;

    public Match(AccountUser account, Result result, LocalDateTime date) {
        opponent = account;
        this.result = result;
        this.date = date;
    }

    public String toString() {
        int minutes = LocalDateTime.now().getMinute() - date.getMinute();
        return opponent.getName() + " : " + result + " at " + (minutes / 60 > 0 ? minutes / 60 : minutes);
    }
}
