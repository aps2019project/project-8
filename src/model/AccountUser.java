package model;

import java.util.Map;
import java.util.*;

public class AccountUser implements Comparable {
    private String name;
    private String password;
    private int wins = 0;
    private int money = 0;
    private ArrayList<Match> matchHistory = new ArrayList<>();
    AccountData data;

    public boolean isPasswordValid(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return " - UserName : " + name + " - Wins : " + wins;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Account))
            return 0;
        return Comparator.comparingInt(o1 -> ((Account) o1).getWins()).reversed().compare(this, o);
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public void payMoney(int sum) {
        money -= sum;
    }

    public void receiveMoney(int sum) {
        money += sum;
    }

    public void addMatch(Match match) {
        matchHistory.add(match);
    }

    public void addWin() {
        wins++;
    }

    public int getWins() {
        return wins;
    }

    public void setData(AccountData data) {
        this.data = data;
    }
    public AccountData getData() {
        return data;
    }
}
