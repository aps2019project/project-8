package graphicControllers.menus;

import graphicControllers.Menu;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import menus.UI;
import view.ComponentSet;
import view.GUIButton;
import view.GUIChangeMenuButton;
import view.NodeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class AccountMenu extends Menu {

    public AccountMenu() {
        super(Id.ACCOUNT_MENU, "Account Menu", 800, 600);
        try {
            ImageView imageView = new ImageView(new Image(new FileInputStream("images/logos/bnea-logo@2x.png")));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(50);
            imageView.relocate(10, 10);
            addComponent(new NodeWrapper(imageView));
            ImageView logo = new ImageView(new Image(new FileInputStream("images/logos/icon.png")));
            logo.setPreserveRatio(true);
            logo.setFitWidth(140);
            logo.relocate(windowWidth / 2 - 140.0 / 2, 40);
            addComponent(new NodeWrapper(logo));
        } catch (FileNotFoundException ignored) {
        }

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
            gotoCreateAccount.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        gotoCreateAccount.setText("Create Account");
        gotoCreateAccount.setGoalMenuID(Id.SIGN_IN_MENU);
        addComponent(gotoCreateAccount);

        GUIChangeMenuButton gotoLogin = new GUIChangeMenuButton(windowWidth / 2 - 170.0 / 2, windowHeight / 2 -
                50.0 / 2, 170, 50);
        try {
            gotoLogin.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            gotoLogin.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            gotoLogin.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        gotoLogin.setText("Login");
        gotoLogin.setGoalMenuID(Id.LOGIN_MENU);
        addComponent(gotoLogin);

        GUIChangeMenuButton gotoLeaderboard = new GUIChangeMenuButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2 + 10 + 50, 170, 50);
        try {
            gotoLeaderboard.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            gotoLeaderboard.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            gotoLeaderboard.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        gotoLeaderboard.setText("Leaderboard");
        gotoLeaderboard.setGoalMenuID(Id.LEADERBOARD);
        addComponent(gotoLeaderboard);

        GUIButton exit = new GUIButton(windowWidth - 100, windowHeight - 50, 100, 50);
        try {
            exit.setImage(new Image(new FileInputStream("./images/buttons/button_cancel@2x.png")));
            exit.setActiveImage(new Image(new FileInputStream("./images/buttons/button_cancel_glow@2x.png")));
            exit.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        exit.setText("Exit");
        addComponent(exit);
        exit.setOnMouseClicked(e -> System.exit(0));

        setUpQuickStart();
    }

    private void setUpQuickStart() {
        GUIChangeMenuButton guiButton = new GUIChangeMenuButton(windowWidth / 2 - 150, windowHeight - 150, 300, 100);
        guiButton.setText("QUICK START");
        try {
            guiButton.setImage(new Image(new FileInputStream("images/buttons/button_cancel@2x.png")));
            guiButton.setActiveImage(new Image(new FileInputStream("images/buttons/button_cancel_glow@2x.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        guiButton.setOnMouseClicked(e -> {
            UI.decide("login aa");
            UI.decide("aa");
            UI.decide("battle");
            UI.decide("singleplayer");
            UI.decide("story");
            UI.decide("1");
            GameMenu.getInstance().refresh();
        });
        guiButton.setGoalMenuID(Id.IN_GAME_MENU);
        addComponent(guiButton);
    }

}
