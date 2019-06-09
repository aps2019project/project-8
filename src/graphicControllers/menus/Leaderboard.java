package graphicControllers.menus;

import graphicControllers.Menu;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import model.Account;
import view.GUIChangeMenuButton;
import view.NodeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Leaderboard extends Menu {
    private static final VBox vBox = new VBox();
    private static final int LABEL_HEIGHT = 75;
    private static double labelWidth;

    public Leaderboard() {
        super(Id.LEADERBOARD, "Leaderboard", Menu.windowDefaultWidth, Menu.windowDefaultHeight);
        try {
            getView().setBackground(new Image(new FileInputStream("./images/backgrounds/obsidian_woods_background@2x.jpg")));
            getView().setCursor(new Image(new FileInputStream("./images/cursors/mouse_auto.png")));
        } catch (FileNotFoundException ignored) {
        }

        ScrollPane scrollPane = new ScrollPane(vBox);
        labelWidth = windowWidth - 2 * 100;
        scrollPane.setMinWidth(labelWidth);
        scrollPane.setMaxWidth(labelWidth);
        vBox.setFillWidth(true);
        scrollPane.relocate(windowWidth / 2 - labelWidth / 2, 100);
        scrollPane.setMinHeight(windowHeight - 2 * 100);
        scrollPane.setMaxHeight(windowHeight - 2 * 100);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHmax(0);
        vBox.setMaxHeight(windowHeight - 2 * 100);
        getView().getScene().getStylesheets().add("css/scrollPane.css");
        vBox.setAlignment(Pos.CENTER);

        addComponent(new NodeWrapper(scrollPane));

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
        vBox.getChildren().clear();
        Account.getAccounts().forEach(o -> {
            try {
                Label label = new Label();
                label.setMinWidth(labelWidth);
                label.setMinWidth(labelWidth);
                label.setMinHeight(LABEL_HEIGHT);
                label.setMaxHeight(LABEL_HEIGHT);
                label.setAlignment(Pos.CENTER);
                label.setTextFill(Color.GOLDENROD);
                label.setText(o.getName() + " - Wins: " + o.getWins());
                Image image = new Image(new FileInputStream("./images/placeholders/gauntlet_control_bar_bg@2x.png"
                ), labelWidth, LABEL_HEIGHT, true, true);
                label.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
                vBox.getChildren().add(label);
            } catch (FileNotFoundException ignored) {
            }
        });
    }
}
