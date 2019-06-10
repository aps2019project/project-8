package graphicControllers.menus;

import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import menus.Menus;
import menus.UI;
import view.GUIButton;
import view.GUIChangeMenuButton;
import view.NodeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LoginMenu extends Menu {
    TextField usernameField = new TextField();
    TextField passwordField = new TextField();
    GUIButton enterButton;

    public LoginMenu() {
        super(Id.LOGIN_MENU, "Login Menu", windowDefaultWidth, windowDefaultHeight);

        try {
            getView().setBackground(new Image(new FileInputStream("./images/backgrounds/color-plate-bg-orange@2x.png")));
            getView().setCursor(new Image(new FileInputStream("./images/cursors/mouse_auto.png")));
        } catch (FileNotFoundException ignored) {
        }
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMinSize(100, 30);
        usernameField.setMaxSize(100, 30);
        usernameField.relocate(windowWidth / 2 - 100.0 / 2, windowHeight / 2 - 30.0 - 5);
        addComponent(new NodeWrapper(usernameField));

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMinSize(100, 30);
        passwordField.setMaxSize(100, 30);
        passwordField.relocate(windowWidth / 2 - 100.0 / 2, windowHeight / 2 + 5);
        addComponent(new NodeWrapper(passwordField));

        enterButton = new GUIButton(windowWidth / 2 - 100.0 / 2, windowHeight / 2 + 5 + 30 + 20,
                100, 50);
        enterButton.setOnMouseClicked(event -> {
            if (!usernameField.getText().isEmpty()) {
                if (!passwordField.getText().isEmpty()) {
                    UI.decide("login " + usernameField.getText());
                    if (UI.getMenu() == Menus.LOGIN_ACCOUNT) {
                        UI.decide(passwordField.getText());
                        if (UI.getMenu() == Menus.MAIN_MENU)
                            MenuManager.getInstance().setCurrentMenu(Id.MAIN_MENU);
                        else
                            MenuManager.getInstance().showPopUp("Incorrect password entered");
                    } else
                        MenuManager.getInstance().showPopUp("No account with this name exists.");
                } else
                    MenuManager.getInstance().showPopUp("Please enter the password.");
            } else
                MenuManager.getInstance().showPopUp("Please enter a username.");
        });
        try {
            enterButton.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            enterButton.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            enterButton.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        enterButton.setText("Enter");
        addComponent(enterButton);

        GUIChangeMenuButton back = new GUIChangeMenuButton(windowWidth / 2 - 100.0 / 2, windowHeight - 50,
                100, 50);
        try {
            back.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            back.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            back.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        back.setText("Back");
        back.setGoalMenuID(Id.ACCOUNT_MENU);
        addComponent(back);
    }

    @Override
    public void refresh() {
        usernameField.clear();
        passwordField.clear();
    }
}
