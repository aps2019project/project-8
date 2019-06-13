package graphicControllers.menus;

import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import menus.UI;
import view.GUIButton;
import view.NodeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BattleMenu extends Menu {
    public BattleMenu() {
        super(Id.CHOOSE_BATTLE_MENU, "Battle Menu", windowDefaultWidth, windowDefaultHeight);

        try {
            ImageView imageView = new ImageView(new Image(new FileInputStream("images/logos/brand_duelyst@2x.png")));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(windowWidth / 3);
            imageView.relocate(windowWidth * 2 / 3 - 10, 10);
            addComponent(new NodeWrapper(imageView));
        } catch (FileNotFoundException e) {
        }

        try {
            getView().setBackground(new Image(new FileInputStream("images/backgrounds/background@2x.jpg")));
            getView().setCursor(new Image(new FileInputStream("./images/cursors/mouse_auto.png")));
        } catch (FileNotFoundException ignored) {
        }


        GUIButton gotoSinglePlayer = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 - 5, 170, 50);
        try {
            gotoSinglePlayer.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            gotoSinglePlayer.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            gotoSinglePlayer.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        gotoSinglePlayer.setText("Single Player");
        gotoSinglePlayer.setOnMouseClicked(e -> {
            UI.decide("singleplayer");
            MenuManager.getInstance().setCurrentMenu(Id.SINGLEPLAYER_MENU);
        });
        addComponent(gotoSinglePlayer);

        GUIButton gotoMultiplayer = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 + 5, 170, 50);
        try {
            gotoMultiplayer.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            gotoMultiplayer.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            gotoMultiplayer.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        gotoMultiplayer.setText("Multiplayer");
        gotoMultiplayer.setOnMouseClicked(e -> {
            UI.decide("multiplayer");
            MenuManager.getInstance().setCurrentMenu(Id.MULTIPLAYER_MENU);
        });
        addComponent(gotoMultiplayer);
    }
}
