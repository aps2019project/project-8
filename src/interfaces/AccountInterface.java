package interfaces;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import model.AccountUser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AccountInterface {

    private ArrayList<AccountUser> accounts = new ArrayList<>();

    public AccountInterface() {
        loadAccounts();
    }

    public AccountUser getAccount(String accountName) {
        for (AccountUser account : accounts)
            if (account.getName().equals(accountName))
                return account;
        return null;
    }

    private boolean hasAccount(String accountName) {
        return getAccount(accountName) != null;
    }

    private void addAccount(AccountUser account) {
        accounts.add(account);
    }

    public boolean createAccount(String accountName, String password) {
        if (hasAccount(accountName))
            return false;
        AccountUser account = new AccountUser(accountName, password);
        addAccount(account);
        account.saveAccount();
        return true;
    }

    public boolean login(String accountName, String password) {
        AccountUser account = getAccount(accountName);
        return account != null && account.isPasswordValid(password);
    }

    private void loadAccounts() {
        accounts.clear();
        try {
            for (File file : new File("./database/Accounts").listFiles()) {
                YaGson yaGson = new YaGson();
                AccountUser account = yaGson.fromJson(new BufferedReader(new FileReader(file)), AccountUser.class);
                addAccount(account);
            }
        } catch (IOException e) {
            System.err.println("no directory named \"database\\Accounts\" in project directory");
        }
    }


    public String[] getLeaderboard() {
        Collections.sort(accounts);
        int length = Math.min(10, accounts.size());
        String[] names = new String[length];
        for (int i = 0; i < length; i++) {
            names[i] = accounts.get(i).getName() + " - Wins : " + accounts.get(i).getWins();
        }
        return names;
    }
}
