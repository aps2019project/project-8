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
import model.Player;
import view.ComponentSet;
import view.NodeSet;
import view.NodeWrapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameMenu extends Menu {
    ComponentSet firstPlayerBar, secondPlayerBar;
    ComponentSet gridCells;

    public GameMenu() {
        super(Id.IN_GAME_MENU, "Game Menu", windowDefaultWidth, windowDefaultHeight);

        // setting up the back ground
        try {
            setBackGround(new Image(new FileInputStream("images/backgrounds/battlemap0_background@2x.png")));
        } catch (FileNotFoundException ignored) {
        }


        try {
            ImageView middleGround = new ImageView(new Image(new FileInputStream("images/gameIcons/middleGround/battlemap0_middleground.png")));
            middleGround.setFitHeight(windowHeight);
            middleGround.setFitWidth(windowWidth);
            addComponent(new NodeWrapper(middleGround));
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

        gridCells = makeGrid();
        addComponent(gridCells);

        refresh();
    }

    private ComponentSet makeManaBar(int mana) {
        ComponentSet manaBar = new ComponentSet();
        for (int i = 0; i < mana; i++) {
            try {
                ImageView imageView = new ImageView(new Image(new FileInputStream("images/gameIcons/MenuIcons/icon_mana.png")));
                imageView.setFitWidth(20);
                imageView.setFitHeight(20);
                imageView.relocate(i * 20 + i * 5, 0);
                manaBar.addMenuComponent(new NodeWrapper(imageView));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Label label = new Label(mana + "/" + mana);
            label.relocate(mana * 20 + 15, 2.5);
            manaBar.addMenuComponent(new NodeWrapper(label));
        }
        return manaBar;
    }

    private ComponentSet makeGrid() {
        ComponentSet grid = new ComponentSet();
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 9; j++) {
                ComponentSet cell = new ComponentSet();
                try {
                    ImageView imageView = new ImageView(new Image(new FileInputStream("images/gameIcons/Cells/inactive_cell.png")));
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(30);
                    imageView.relocate(j * 55, i * 30);
                    imageView.setOnMouseEntered(e -> {
                        try {
                            imageView.setImage(new Image(new FileInputStream("images/gameIcons/Cells/active_cell.png")));
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    });
                    imageView.setOnMouseExited(e -> {
                        try {
                            imageView.setImage(new Image(new FileInputStream("images/gameIcons/Cells/inactive_cell.png")));
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    });
                    cell.addMenuComponent(new NodeWrapper(imageView));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                grid.addMenuComponent(cell, i + "," + j);
            }
        grid.relocate(200, 200);
            grid.resize(1.5, 2);
        return grid;
    }

    public ComponentSet makeFirstPlayerStat(Player player) {
        ComponentSet statBar = new ComponentSet();

        ImageView backGround = null;
        try {
            backGround = new ImageView(new Image(new FileInputStream("images/gameIcons/Hero/hero_background.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        backGround.setFitHeight(50);
        backGround.setFitWidth(50);
        statBar.addMenuComponent(new NodeWrapper(backGround));

        Label playerName = new Label(player.getName());
        playerName.relocate(50 + 10, 5);
        statBar.addMenuComponent(new NodeWrapper(playerName));


        statBar.addMenuComponent(makeManaBar(player.getMana()), "ManaBar");
        ((ComponentSet) statBar.getComponentByID("ManaBar")).relocate(50 + 10, 30);
        //statBar.reflectVertically();
        statBar.relocate(30, 30);
        statBar.resize(1.7, 1.7);
        return statBar;
    }

    @Override
    public void refresh() {
        if (UI.getGame() == null)
            return;
        Player[] players = UI.getGame().getPlayers();
        if (firstPlayerBar != null) {
            removeComponent(firstPlayerBar);
            firstPlayerBar = null;
        }
        firstPlayerBar = makeFirstPlayerStat(players[0]);
        addComponent(firstPlayerBar);

        if (secondPlayerBar != null) {
            removeComponent(secondPlayerBar);
            secondPlayerBar = null;
        }
        secondPlayerBar = makeSecondPlayerStat(players[1]);
        addComponent(secondPlayerBar);
    }

    private ComponentSet makeSecondPlayerStat(Player player) {
        System.err.println(player.getMana());
        ComponentSet statBar = makeFirstPlayerStat(player);
        statBar.reflectVertically();
        statBar.relocate(statBar.getX() + 770, statBar.getY());
        return statBar;
    }
}
