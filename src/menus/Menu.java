package menus;

import client.Connection;
import client.Connector;
import model.Account;
import view.View;

public abstract class Menu {

    protected static Connection connection;
    protected static Account account = null;
    protected static View view = null;

    public static void setAccount(Account account) {
        Menu.account = account;
    }

    public static void setView(View view) {
        Menu.view = view;
    }

    public static Account getAccount() {
        return account;
    }

    public static void setConnection(Connection connection) {
        Menu.connection = connection;
    }
}
