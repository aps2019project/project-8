package model;

import java.util.ArrayList;

public class Account implements Comparable {
    static ArrayList<Account> accounts = new ArrayList<>();
    String name;
    String password;
    int wins = 0;

    public Account(String name, String password) {
        this.name = name;
        this.password = password;
        accounts.add(this);
    }

    public static ArrayList<Account> getAccounts() {
        return accounts;
    }

    public static Account getAccount(String name) {
        for (Account account : accounts)
            if (account.name.equals(name))
                return account;
        return null;
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
        if (o instanceof Account)
            return wins - ((Account)o).wins;
        return 0;
    }
}
