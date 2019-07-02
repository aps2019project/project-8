package menus;

import client.Connection;
import model.AccountUser;
import view.View;

public abstract class Menu {

    protected static Connection connection;
    protected static AccountUser account = null;
    protected static View view = null;

    public static void setView(View view) {
        Menu.view = view;
    }

    public static void setConnection(Connection connection) {
        Menu.connection = connection;
    }

    public static AccountUser getAccount() {
        return account;
    }

    public static void setAccount(AccountUser accountUser) {
        Menu.account = accountUser;
    }
}
