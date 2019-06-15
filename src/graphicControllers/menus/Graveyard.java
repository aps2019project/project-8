package graphicControllers.menus;

import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import menus.UI;
import view.CardView;
import view.GUIButton;
import view.MenuComponent;
import view.NodeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Graveyard extends Menu {
    private static final ArrayList<MenuComponent> components = new ArrayList<>();
    private ScrollPane scrollPane;

    public Graveyard() {
        super(Id.GRAVE_YARD_MENU, "Graveyard", windowDefaultWidth, windowDefaultHeight);
        new Thread(() -> {
            try {
                setBackGround(new Image(new FileInputStream("images/backgrounds/magaari_ember_highlands_background@2x.jpg")));
            } catch (FileNotFoundException ignored) {
            }
            GUIButton back = new GUIButton(windowWidth / 2 - 100.0 / 2, windowHeight - 50,
                    100, 50);
            try {
                back.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                back.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                back.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            back.setText("Back");
            back.setOnMouseClicked(e -> {
                components.forEach(this::removeComponent);
                MenuManager.getInstance().setCurrentMenu(Id.IN_GAME_MENU);
            });
            addComponent(back);

            scrollPane = new ScrollPane();
            scrollPane.relocate(windowWidth / 2 - (windowDefaultWidth - 2 * 100) / 2, 100);
            scrollPane.setMinHeight(windowHeight - 2 * 100);
            scrollPane.setPrefHeight(windowHeight - 2 * 100);
            scrollPane.setMaxHeight(windowHeight - 2 * 100);
            scrollPane.setMinWidth(windowDefaultWidth - 2 * 100);
            scrollPane.setPrefWidth(windowDefaultWidth - 2 * 100);
            scrollPane.setMaxWidth(windowDefaultWidth - 2 * 100);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setHmax(0);
            getView().getScene().getStylesheets().add("css/scrollPane.css");

            addComponent(new NodeWrapper(scrollPane));
        }).start();
    }

    @Override
    public void refresh() {
        VBox vBox = new VBox();
        vBox.setMinWidth(windowDefaultWidth - 2 * 100);
        vBox.setPrefWidth(windowDefaultWidth - 2 * 100);
        vBox.setMaxWidth(windowDefaultWidth - 2 * 100);
        vBox.setAlignment(Pos.CENTER);
        vBox.setFillWidth(true);
        scrollPane.setContent(vBox);
        int size = UI.getGame().getCurrentPlayer().getGraveYard().size();
        for (int i = 0; i < (size + 1) / 2; i++) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            vBox.getChildren().add(hBox);
            hBox.getChildren().add(new CardView(UI.getGame().getCurrentPlayer().getGraveYard().get(2 * i)).getStackPane());
            if (2 * i + 1 < size)
                hBox.getChildren().add(new CardView(UI.getGame().getCurrentPlayer().getGraveYard().get(2 * i + 1)).getStackPane());
        }
    }
}
