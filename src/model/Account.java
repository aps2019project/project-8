package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Account implements Comparable {
    private static ArrayList<Account> accounts = new ArrayList<>();
    private String name;
    private String password;
    private int wins = 0;
    private int money = 0;
    private Collection collection = new Collection();
    private Deck mainDeck = null;

    public Account(Account account) {
        accounts.add(account);
    }

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

    public void setMainDeck(Deck mainDeck) {
        this.mainDeck = mainDeck;
    }

    @Override
    public String toString() {
        return " - UserName : " + name + " - Wins : " + wins;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Account)
            return wins - ((Account) o).wins;
        return 0;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public boolean hasThreeItems() {
        return false;
    }

    public Collection getCollection() {
        return collection;
    }

    public void payMoney(int sum) {
        money -= sum;
    }

    public void receiveMoney(int sum) {
        money += sum;
    }

    public HashMap<String, CollectionItem> getCollectionItems() {
        return collection.getCollectionItems();
    }
}
