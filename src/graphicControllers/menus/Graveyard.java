package graphicControllers.menus;

import graphicControllers.Menu;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import view.GUIChangeMenuButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Graveyard extends Menu {
    public Graveyard() {
        super(Id.GRAVE_YARD_MENU, "Graveyard", windowDefaultWidth, windowDefaultHeight);
        new Thread(() -> {
            try {
                setBackGround(new Image(new FileInputStream("images/backgrounds/magaari_ember_highlands_background@2x.jpg")));
            } catch (FileNotFoundException ignored) {
            }
            GUIChangeMenuButton back = new GUIChangeMenuButton(windowWidth / 2 - 100.0 / 2, windowHeight - 50,
                    100, 50);
            try {
                back.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                back.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                back.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            back.setText("Back");
            back.setGoalMenuID(Id.IN_GAME_MENU);
            addComponent(back);
        }).start();
    }
}
