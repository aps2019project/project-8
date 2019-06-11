package graphicControllers.menus;

import graphicControllers.Menu;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import menus.UI;
import model.Game;
import view.ComponentSet;
import view.NodeSet;
import view.NodeWrapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameMenu extends Menu {
    Label[] playerNames = new Label[]{new Label(), new Label()};

    ImageView[] playerTopIcons = new ImageView[2];

    public GameMenu() {
        super(Id.IN_GAME_MENU, "Game Menu", windowDefaultWidth, windowDefaultHeight);

        // setting up the back ground
        try {
            setBackGround(new Image(new FileInputStream("images/backgrounds/battlemap0_background@2x.png")));
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

        refresh();
    }

    @Override
    public void refresh() {
        if (UI.getGame() == null)
            return;
        String[] playerNames = UI.getGame().getPlayerNames();
        Label firstPlayer = new Label(playerNames[0]);
        firstPlayer.relocate(10, 10);
        Label secondPlayer = new Label(playerNames[1]);
        secondPlayer.relocate(windowWidth - 10 - 50, 10);
        addComponent(new NodeWrapper(firstPlayer));
        addComponent(new NodeWrapper(secondPlayer));
    }
}
