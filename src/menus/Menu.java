package menus;

import model.Account;
import view.View;

public abstract class Menu {
    protected static Account account = null;
    protected static View view = null;

    public static void setAccount(Account account) {
        Menu.account = account;
    }

    public static void setView(View view) {
        Menu.view = view;
    }
}
