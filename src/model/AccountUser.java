package model;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class AccountUser implements Comparable {
    private String name;
    private String password;
    private int wins = 0;
    private int money = 0;
    private ArrayList<Match> matchHistory = new ArrayList<>();
    private AccountData data = new AccountData();

    public AccountUser(String name, String password) {
        this.name = name;
        this.password = password;
//        accounts.add(this);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof AccountUser))
            return 0;
        AccountUser accountUser = (AccountUser) o;
        if (wins - accountUser.wins != 0)
            return wins - accountUser.wins;
        return name.compareTo(accountUser.getName());
    }

    @Override
    public String toString() {
        return " - UserName : " + name + " - Wins : " + wins;
    }

    public boolean isPasswordValid(String password) {
        return this.password.equals(password);
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

    public AccountData getData() {
        return data;
    }

    public void setData(AccountData data) {
        this.data = data;
    }

    public void saveAccount() {
        AccountUser account = this;
        try {
            FileWriter out = new FileWriter("./database/Accounts/" + account.getName() + ".json", false);
            YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
            out.write(yaGson.toJson(account, AccountUser.class));
            out.flush();
//            view.alertSave(); // ***
        } catch (IOException e) {
            System.err.println("no directory named \"database\\Accounts\" in project directory");
        }
    }
}
