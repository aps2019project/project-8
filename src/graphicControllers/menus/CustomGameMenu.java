package graphicControllers.menus;

import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import menus.UI;
import view.NodeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

public class CustomGameMenu extends Menu {
    private static final VBox vBox = new VBox();
    private static final int LABEL_HEIGHT = 40;
    private static double labelWidth;
    private static final Media sound = new Media(new File("sfx/sfx_unit_onclick.m4a").toURI().toString());

    public CustomGameMenu() {
        super(Id.START_CUSTOM_GAME_MENU, "Custom Game Menu", 1240, 720);

        try {
            ImageView imageView = new ImageView(new Image(new FileInputStream("images/logos/brand_duelyst@2x.png")));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(windowWidth / 3);
            imageView.relocate(windowWidth * 2 / 3 - 10, 10);
            addComponent(new NodeWrapper(imageView));
        } catch (FileNotFoundException ignored) {
        }

        try {
            getView().setBackground(new Image(new FileInputStream("images/backgrounds/background@2x.jpg")));
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
    }

    @Override
    public void refresh() {
        vBox.getChildren().clear();
        UI.getAccount().getDecks().forEach(o -> {
            if (o.isValid()) {
                try {
                    Label label = new Label();
                    label.setMinWidth(labelWidth);
                    label.setMinWidth(labelWidth);
                    label.setMinHeight(LABEL_HEIGHT);
                    label.setMaxHeight(LABEL_HEIGHT);
                    label.setAlignment(Pos.CENTER);
                    label.setTextFill(Color.AQUAMARINE);
                    label.setText(o.getDeckName());
                    label.setFont(Font.loadFont(new FileInputStream("./fonts/averta-black-webfont.ttf"), 17));
                    Image image = new Image(new FileInputStream("images/placeholders/diamond_main_menu_container@2x.png"
                    ), labelWidth, LABEL_HEIGHT, true, true);
                    Background inactive = new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
                    Background active = new Background(new BackgroundImage(new Image(new FileInputStream("images/placeholders/gold_main_menu_container@2x.png"), labelWidth, LABEL_HEIGHT, true, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
                    label.setBackground(inactive);
                    label.setOnMouseEntered(e -> {
                        new MediaPlayer(sound).play();
                        label.setBackground(active);
                    });
                    label.setOnMouseExited(e -> label.setBackground(inactive));
                    label.setTooltip(new Tooltip(o.getHero().getName()));
                    vBox.getChildren().add(label);
                    label.setOnMouseClicked(e -> {
                        Optional<Integer>[] gameType = popUpGetGameType();
                        if (gameType[0].isPresent()) {
                            UI.decide("start game " + label.getText() + " " + gameType[0].get() + (gameType[1].isPresent() ? " " + gameType[1].get() : ""));
                            MenuManager.getInstance().setCurrentMenu(Id.IN_GAME_MENU);
                        }
                    });
                } catch (FileNotFoundException ignored) {
                }
            }
        });
    }
}
