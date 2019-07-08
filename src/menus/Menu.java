package menus;

import model.AccountUser;
import view.View;

public abstract class Menu {

//    protected static Connection connection;
    protected AccountUser account = null;
    protected View view = null;

    public void setView(View view) {
        this.view = view;
    }
//
//    public static void setConnection(Connection connection) {
//        Menu.connection = connection;
//    }
//
//    public static Connection getConnection() {
//        return connection;
//    }

//    public static AccountUser getAccount() {
//        return account;
//    }

    public View getView() {
        return view;
    }

    public void setAccount(AccountUser accountUser) {
        this.account = accountUser;
    }
}
