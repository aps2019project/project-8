package graphicControllers.menus;

import graphicControllers.Menu;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import menus.UI;
import view.*;

public class AccountMenu extends Menu {

    public AccountMenu() {
        super(Id.ACCOUNT_MENU, "Account Menu", Menu.windowDefaultWidth, Menu.windowDefaultHeight);

        GUIChangeMenuButton gotoLogin = new GUIChangeMenuButton(windowWidth / 2 - 100.0 / 2, windowHeight / 2 - 30.0 / 2,
                100, 30);
        gotoLogin.setText("Login");
        gotoLogin.setGoalMenuID(Id.LOGIN_MENU);
        addComponent(gotoLogin);

        GUIChangeMenuButton gotoCreateAccount = new GUIChangeMenuButton(windowWidth / 2 - 100.0 /2, windowHeight / 2 - 30.0 / 2 + 30 + 10,
                100, 30);
        gotoCreateAccount.setText("Create Account");
        gotoCreateAccount.setGoalMenuID(Id.SIGN_IN_MENU);
        addComponent(gotoCreateAccount);
    }


}
