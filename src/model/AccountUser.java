package model;

import menus.Menu;

import java.util.Map;
import java.util.*;

public class AccountUser implements Comparable {


    private static ArrayList<AccountUser> accounts = new ArrayList<>();

    private String name;
    private String password;
    private int wins = 0;
    private int money = 0;
    private ArrayList<Match> matchHistory = new ArrayList<>();
    private AccountData data = new AccountData();


    public AccountUser(AccountUser account) {
        accounts.add(account);
    }

    public AccountUser(String name, String password) {
        this.name = name;
        this.password = password;
        accounts.add(this);
    }

    public static ArrayList<AccountUser> getAccounts() {
        return accounts;
    }

    public static AccountUser getAccount(String name) {
        for (AccountUser account : accounts)
            if (account.name.equals(name))
                return account;
        return null;
    }

    public static boolean hasAccount(String name) {
        for (AccountUser account : accounts)
            if (account.name.equalsIgnoreCase(name))
                return true;
        return false;
    }



    public boolean isPasswordValid(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return " - UserName : " + name + " - Wins : " + wins;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof AccountUser))
            return 0;
        return Comparator.comparingInt(o1 -> ((AccountUser) o1).getWins()).reversed().compare(this, o);
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
