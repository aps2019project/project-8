package graphicControllers.menus;

import graphicControllers.Menu;
import javafx.scene.ParallelCamera;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import menus.UI;
import model.Game;
import model.Map;
import model.Player;
import view.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameMenu extends Menu {
    private ComponentSet firstPlayerBar, secondPlayerBar;
    private ComponentSet gridCells;
    private ComponentSet cardBar;
    private ComponentSet menuButtons;

    private String getUIOutputAsString(String command) {
        PrintStream prevOut = System.out;
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));
        UI.decide(command);
        System.setOut(prevOut);
        return out.toString();
    }

    private void setUpBackGround() {
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
            foreGround.relocate(windowWidth - foreGround.getFitWidth(), windowHeight - foreGround.getFitWidth() * foreGround.getImage().getHeight() / foreGround.getImage().getWidth());
            addComponent(new NodeWrapper(foreGround));
        } catch (FileNotFoundException ignored) {
        }
    }


    public GameMenu() {
        super(Id.IN_GAME_MENU, "Game Menu", windowDefaultWidth, windowDefaultHeight);
        setUpBackGround();
        setUpMenuButtons();
    }

    private void setOnEnterAndExitEffect(GUIButton guiButton, String text, String enterImagePath, String exitImagePath) {
        try {
            guiButton.setImage(new Image(new FileInputStream(exitImagePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        guiButton.setText(text);
        guiButton.setOnMouseEntered(e -> {
            System.err.println("PESHGEL");
            try {
                guiButton.setImage(new Image(new FileInputStream(enterImagePath)));
                guiButton.setText("PESHGEL");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        guiButton.setOnMouseExited(e -> {
            try {
                guiButton.setImage(new Image(new FileInputStream(exitImagePath)));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void setUpMenuButtons() {
        menuButtons = new ComponentSet();
        GUIButton endTurnButton = new GUIButton(0, 0, 200, 100);
        setOnEnterAndExitEffect(endTurnButton, "END TURN", "images/gameIcons/menuButtons/end_turn_glow.png"
                , "images/gameIcons/menuButtons/end_turn_normal.png");
        menuButtons.addMenuComponent(endTurnButton);

        GUIChangeMenuButton mainMenu = new GUIChangeMenuButton(0, 100, 100, 50);
        setOnEnterAndExitEffect(mainMenu, "Main Menu", "images/gameIcons/menuButtons/ui_left_glowing.png",
                "images/gameIcons/menuButtons/ui_left_normal.png");
        mainMenu.setOnMouseClicked(e -> {
            UI.decide("exit");
        });
        mainMenu.setGoalMenuID(Id.MAIN_MENU);
        menuButtons.addMenuComponent(mainMenu);


        menuButtons.relocate(windowWidth - 250, windowHeight - 200);
        addComponent(menuButtons);
    }

    private ComponentSet setHandOptions(String deckCapacityNumber) {
        ComponentSet handOptions = new ComponentSet();
        try {
            ImageView backGround = new ImageView(new Image(new FileInputStream("images/gameIcons/hand_options_background.png")));
            backGround.setFitWidth(70);
            backGround.setFitHeight(70);
            handOptions.addMenuComponent(new NodeWrapper(backGround));
            ImageView textBackground = new ImageView(new Image(new FileInputStream("images/gameIcons/simple_text_background.png")));
            textBackground.setFitHeight(10);
            textBackground.setFitWidth(20);
            textBackground.relocate(35 - 10, 35 - 5);
            handOptions.addMenuComponent(new NodeWrapper(textBackground));
            Label deckNameLabel = new Label("Deck");
            deckNameLabel.relocate(35 - 10 + 4, 35 - 5 + 2);
            deckNameLabel.setTextFill(Color.AZURE);
            deckNameLabel.setFont(new Font(5));
            handOptions.addMenuComponent(new NodeWrapper(deckNameLabel));
            Label deckCapacity = new Label(deckCapacityNumber + "/" + 20);
            deckCapacity.relocate(35 - 10, 35 - 5 + 2 + 15);
            deckCapacity.setTextFill(Color.AZURE);
            deckCapacity.setFont(new Font(7));
            handOptions.addMenuComponent(new NodeWrapper(deckCapacity));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return handOptions;
    }

    private void setUpGrid() {
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
        addComponent(grid);
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

    public ComponentSet makeFirstPlayerStat(String playerName, String usableName, String mana) {
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

        Label playerNameLabel = new Label(playerName);
        playerNameLabel.relocate(50 + 10, 5);
        statBar.addMenuComponent(new NodeWrapper(playerNameLabel));


        statBar.addMenuComponent(makeManaBar(Integer.parseInt(mana)), "ManaBar");
        ((ComponentSet) statBar.getComponentByID("ManaBar")).relocate(50 + 10, 30);
        statBar.relocate(30, 30);
        statBar.resize(1.7, 1.7);
        return statBar;
    }

    private ComponentSet makeSecondPlayerStat(String playerName, String usableName, String mana) {
        ComponentSet statBar = makeFirstPlayerStat(playerName, usableName, mana);
        statBar.reflectVertically();
        statBar.relocate(statBar.getX() + 770, statBar.getY());
        return statBar;
    }

    @Override
    public void refresh() {
        if (UI.getGame() == null)
            return;

        String firstPlayerName = null, secondPlayerName = null, firstPlayerMana = null, secondPlayerMana = null;
        String firstPlayerDeckCapacity = null;
        ArrayList<String> handCardNames = new ArrayList<>();
        ArrayList<String> handCardManaCosts = new ArrayList<>();
        String playerOneUsableItemName = null, playerTwoUsableItemName = null;
        String[][] gridStrings = new String[Map.NUMBER_OF_ROWS][Map.NUMBER_OF_COLUMNS];

        String[] shengdeShow = getUIOutputAsString("shengdebao").split("\\n");
        for (int i = 0; i < shengdeShow.length; i++) {
            if (i == 0) {
                Pattern pattern = Pattern.compile("(.+) Mana\\((\\d+)\\) deck\\((\\d+)\\) hand:.*");
                Matcher matcher = pattern.matcher(shengdeShow[i]);
                if (matcher.find()) {
                    firstPlayerName = matcher.group(1);
                    firstPlayerMana = matcher.group(2);
                    firstPlayerDeckCapacity = matcher.group(3);
                }
            } else if (i == 1) {
                Pattern pattern = Pattern.compile("([a-zA-Z0-9]+)\\((\\d+)\\)");
                Matcher matcher = pattern.matcher(shengdeShow[i]);
                while (matcher.find()) {
                    handCardNames.add(matcher.group(1));
                    handCardManaCosts.add(matcher.group(2));
                }
            } else if (i == 2) {
                Pattern pattern = Pattern.compile("Player 1 usable item is: (.+)");
                Matcher matcher = pattern.matcher(shengdeShow[i]);
                if (matcher.find()) {
                    String s = matcher.group(1);
                    if (!s.equals("No Items selected"))
                        playerOneUsableItemName = s;
                }
            } else if (i == shengdeShow.length - 3) {
                Pattern pattern = Pattern.compile("(.+) Mana\\((\\d+)\\) deck\\(\\d+\\) hand:.*");
                Matcher matcher = pattern.matcher(shengdeShow[i]);
                if (matcher.find()) {
                    secondPlayerName = matcher.group(1);
                    secondPlayerMana = matcher.group(2);
                }
            } else if (i == shengdeShow.length - 1) {
                Pattern pattern = Pattern.compile("Player 2 usable item is: (.+)");
                Matcher matcher = pattern.matcher(shengdeShow[i]);
                if (matcher.find()) {
                    playerTwoUsableItemName = matcher.group(1);
                    if (playerTwoUsableItemName.equals("No Items selected"))
                        playerTwoUsableItemName = null;
                }
            } else if (i < 3 + Map.NUMBER_OF_ROWS) {
                String[] strings = shengdeShow[i].split(" +");
                gridStrings[i - 3] = Arrays.copyOf(strings, gridStrings[i - 3].length);
            }
        }

        removeAndReplace(firstPlayerBar, makeFirstPlayerStat(firstPlayerName, playerOneUsableItemName, firstPlayerMana));
        removeAndReplace(secondPlayerBar, makeSecondPlayerStat(secondPlayerName, playerTwoUsableItemName, secondPlayerMana));
        removeAndReplace(cardBar, makeCardBar(firstPlayerDeckCapacity, handCardNames, handCardManaCosts));
        //removeAndReplace(gridCells, makeGridCells(gridStrings));
    }

    private ComponentSet makeGridCells(String[][] gridStrings) {
        return null;
    }

    private ComponentSet makeCardBar(String firstPlayerDeckCapacity, ArrayList<String> handCardNames, ArrayList<String> handCardManaCosts) {

        int size = handCardNames.size();
        cardBar = new ComponentSet();
        for (int i = 0; i < 5; i++) {
            ComponentSet handCard = new ComponentSet();
            try {
                ImageView imageView = new ImageView(new Image(new FileInputStream("images/gameIcons/card_background.png")));
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                imageView.relocate(i * 55, 0);
                if (i < size) {
                    imageView.setOnMouseEntered(e -> {
                        try {
                            imageView.setImage(new Image(new FileInputStream("images/gameIcons/cardbackground_highlight.png")));
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    });
                    imageView.setOnMouseExited(e -> {
                        try {
                            imageView.setImage(new Image(new FileInputStream("images/gameIcons/card_background.png")));
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    });
                }
                handCard.addMenuComponent(new NodeWrapper(imageView));

                ImageView manaIcon = new ImageView(new Image(new FileInputStream("images/gameIcons/MenuIcons/icon_mana.png")));
                if (i >= size) {
                    manaIcon.setImage(new Image(new FileInputStream("images/gameIcons/MenuIcons/icon_mana_inactive.png")));
                }
                manaIcon.setFitWidth(20);
                manaIcon.setFitHeight(20);
                manaIcon.relocate(i * 55 + 25 - 5, 50 - 5);
                handCard.addMenuComponent(new NodeWrapper(manaIcon));

                if (i < size) {
                    String x = handCardManaCosts.get(i);
                    Label label = new Label(x);
                    label.relocate(i * 55 + 25, 50);
                    handCard.addMenuComponent(new NodeWrapper(label));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ComponentSet handOptions = setHandOptions(firstPlayerDeckCapacity);
            handOptions.relocate(-80, -7);
            handOptions.resize(1.2, 1.2);
            cardBar.addMenuComponent(handOptions);

            cardBar.addMenuComponent(handCard, "card_" + i);
        }
        cardBar.resize(2.5, 2.5);
        cardBar.relocate(300, windowHeight - 200);
        return cardBar;
    }

    private void removeAndReplace(ComponentSet component, ComponentSet value) {
        if (value == null)
            return;
        if (component != null)
            removeComponent(component);
        component = value;
        addComponent(component);
    }

}
