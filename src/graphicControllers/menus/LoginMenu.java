package graphicControllers.menus;

import graphicControllers.Menu;
import javafx.scene.control.TextField;
import menus.UI;
import view.GUIChangeMenuButton;
import view.NodeWrapper;

public class LoginMenu extends Menu {
    TextField usernameField = new TextField();
    TextField passwordField = new TextField();
    GUIChangeMenuButton enterButton;

    public LoginMenu() {
        super(Id.LOGIN_MENU, "Login Menu", windowDefaultWidth, windowDefaultHeight);

        usernameField = new TextField("enter user name");
        usernameField.setMinSize(100, 30);
        usernameField.setMaxSize(100, 30);
        usernameField.relocate(windowWidth / 2 - 100.0 / 2, windowHeight / 2 - 200 - 30.0 / 2);
        addComponent(new NodeWrapper(usernameField));

        passwordField = new TextField("enter user name");
        passwordField.setMinSize(100, 30);
        passwordField.setMaxSize(100, 30);
        passwordField.relocate(windowWidth / 2 - 100.0 / 2, windowHeight / 2 - 200 - 30.0 / 2 + 30 + 10);
        addComponent(new NodeWrapper(passwordField));

        enterButton = new GUIChangeMenuButton(windowWidth / 2 - 100.0 / 2, windowHeight / 2 - 200 - 30.0 / 2 + 40 + 30 + 10,
                100, 30);
        enterButton.setOnMouseClicked(event -> {
            UI.decide("login " + usernameField.getText());
            UI.decide(passwordField.getText());
        });
        enterButton.setGoalMenuID(Id.ACCOUNT_MENU);
        enterButton.setText("Enter");
        addComponent(enterButton);
    }
}
