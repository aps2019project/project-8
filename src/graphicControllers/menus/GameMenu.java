package graphicControllers.menus;

import graphicControllers.Menu;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.NodeWrapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameMenu extends Menu {
    Label[] playerNames = new Label[]{new Label(), new Label()};
    public GameMenu() {
        super(Id.IN_GAME_MENU, "Game Menu", 1240, 720);

        try {
            getView().setBackground(new Image(new FileInputStream("images/backgrounds/battlemap0_background@2x.png")));
        } catch (FileNotFoundException ignored) {
        }

        try {
            ImageView map = new ImageView(new Image(new FileInputStream("images/maps/battlemap0_middleground@2x.png")));
            map.setFitWidth(1240);
            map.setFitHeight(720);
            addComponent(new NodeWrapper(map));
        } catch (FileNotFoundException ignored) {
        }

        try {
            ImageView foreGround = new ImageView(new Image(new FileInputStream("images/foregrounds/battlemap0_foreground_001@2x.png")));
            foreGround.setPreserveRatio(true);
            foreGround.setFitHeight(windowHeight / 3);
            foreGround.relocate( 0, 2 * windowHeight / 3);
            addComponent(new NodeWrapper(foreGround));
        } catch (FileNotFoundException ignored) {
        }

        try {
            ImageView foreGround = new ImageView(new Image(new FileInputStream("images/foregrounds/battlemap0_foreground_002@2x.png")));
            foreGround.setPreserveRatio(true);
            foreGround.setFitWidth(windowHeight / 3);
            foreGround.relocate( windowWidth - foreGround.getFitWidth(), windowHeight - foreGround.getFitWidth() * foreGround.getImage().getHeight() / foreGround.getImage().getWidth());
            addComponent(new NodeWrapper(foreGround));
        } catch (FileNotFoundException ignored) {
        }

        playerNames[0].relocate(windowWidth / 4, windowHeight / 5);
    }

    @Override
    public void refresh() {

    }
}
