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

public class SinglePlayerMenu extends Menu {
    public SinglePlayerMenu() {
        super(Id.SINGLEPLAYER_MENU, "Single Player Menu", windowDefaultWidth, windowDefaultHeight);

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


        GUIButton gotoStory = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 - 5, 170, 50);
        try {
            gotoStory.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            gotoStory.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            gotoStory.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        gotoStory.setText("Story");
        gotoStory.setOnMouseClicked(e -> {
            UI.decide("story");
            MenuManager.getInstance().setCurrentMenu(Id.STORY_MODE);
        });
        addComponent(gotoStory);

        GUIButton gotoCustomGame = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 + 5, 170, 50);
        try {
            gotoCustomGame.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            gotoCustomGame.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            gotoCustomGame.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        gotoCustomGame.setText("Custom Game");
        gotoCustomGame.setOnMouseClicked(e -> {
            UI.decide("custom game");
            MenuManager.getInstance().setCurrentMenu(Id.START_CUSTOM_GAME_MENU);
        });
        addComponent(gotoCustomGame);
    }
}
