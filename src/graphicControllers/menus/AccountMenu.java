package graphicControllers.menus;

import graphicControllers.Menu;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import view.GUIButton;
import view.GUIChangeMenuButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AccountMenu extends Menu {

    public AccountMenu() {
        super(Id.ACCOUNT_MENU, "Account Menu", Menu.windowDefaultWidth, Menu.windowDefaultHeight);
        try {
            getView().setBackground(new Image(new FileInputStream("./images/backgrounds/obsidian_woods_background@2x.jpg")));
            getView().setCursor(new Image(new FileInputStream("./images/cursors/mouse_auto.png")));
        } catch (FileNotFoundException ignored) {
        }

        GUIChangeMenuButton gotoCreateAccount = new GUIChangeMenuButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2 - 10 - 50, 170, 50);
        try {
            gotoCreateAccount.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            gotoCreateAccount.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            gotoCreateAccount.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        gotoCreateAccount.setText("Create Account");
        gotoCreateAccount.setGoalMenuID(Id.SIGN_IN_MENU);
        addComponent(gotoCreateAccount);

        GUIChangeMenuButton gotoLogin = new GUIChangeMenuButton(windowWidth / 2 - 100.0 / 2, windowHeight / 2 -
                50.0 / 2, 100, 50);
        try {
            gotoLogin.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            gotoLogin.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            gotoLogin.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        gotoLogin.setText("Login");
        gotoLogin.setGoalMenuID(Id.LOGIN_MENU);
        addComponent(gotoLogin);

        GUIChangeMenuButton gotoLeaderboard = new GUIChangeMenuButton(windowWidth / 2 - 150.0 / 2, windowHeight
                / 2 - 50.0 / 2 + 10 + 50, 150, 50);
        try {
            gotoLeaderboard.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            gotoLeaderboard.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            gotoLeaderboard.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        gotoLeaderboard.setText("Leaderboard");
        gotoLeaderboard.setGoalMenuID(Id.LEADERBOARD);
        addComponent(gotoLeaderboard);

        GUIButton exit = new GUIButton(windowWidth - 100, windowHeight - 50, 100, 50);
        try {
            exit.setImage(new Image(new FileInputStream("./images/buttons/button_cancel@2x.png")));
            exit.setActiveImage(new Image(new FileInputStream("./images/buttons/button_cancel_glow@2x.png")));
            exit.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        exit.setText("Exit");
        addComponent(exit);
        exit.setOnMouseClicked(e -> System.exit(0));
    }


}
