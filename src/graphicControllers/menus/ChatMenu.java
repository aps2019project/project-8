package graphicControllers.menus;

import graphicControllers.Menu;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import view.GUIChangeMenuButton;
import view.NodeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ChatMenu extends Menu {
    public ChatMenu() {
        super(Menu.Id.CHAT_MENU, "Chat Menu", 800, 600);
        VBox vBox = new VBox();
        vBox.setMinHeight(600 - 50);
        vBox.setMaxHeight(600 - 50);
        vBox.setMinWidth(800);
        vBox.setMaxWidth(800);
        vBox.getStylesheets().add("css/vBox.css");
        HBox top = new HBox();
        top.setMinHeight(600 - 50 - 50);
        top.setMaxHeight(600 - 50 - 50);
        top.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        HBox bottom = new HBox();
        bottom.setMinHeight(50);
        bottom.setMaxHeight(50);
        bottom.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        vBox.getChildren().add(top);
        vBox.getChildren().add(bottom);
        vBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        addComponent(new NodeWrapper(vBox));

        try {
            setBackGround(new Image(new FileInputStream("images/backgrounds/bg@2x.jpg")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        back.setGoalMenuID(Id.MAIN_MENU);
        addComponent(back);
    }
}