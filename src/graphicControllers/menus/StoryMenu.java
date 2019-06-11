package graphicControllers.menus;

import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import menus.Menus;
import menus.UI;
import view.GUIButton;
import view.NodeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class StoryMenu extends Menu {
    public StoryMenu() {
        super(Id.STORY_MODE, "Story Mode Menu", windowDefaultWidth, windowDefaultHeight);

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

        GUIButton diveSefid = new GUIButton(windowWidth / 3 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2, 170, 50);
        try {
            diveSefid.setImage(new Image(new FileInputStream("images/placeholders/diamond_main_menu_container@2x.png")));
            diveSefid.setActiveImage(new Image(new FileInputStream("images/placeholders/gold_main_menu_container@2x.png")));
            diveSefid.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
            diveSefid.setTooltip(new Tooltip("Elimination"));
        } catch (FileNotFoundException ignored) {
        }
        diveSefid.setText("Div e Sefid");
        diveSefid.setOnMouseClicked(e -> {
            UI.decide("1");
            if (UI.getMenu() == Menus.GAME_MENU)
                MenuManager.getInstance().setCurrentMenu(Id.IN_GAME_MENU);
            else
                showPopUp("You have no valid main deck to play with.");
        });
        addComponent(diveSefid);

        GUIButton zahhaak = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2, 170, 50);
        try {
            zahhaak.setImage(new Image(new FileInputStream("images/placeholders/diamond_main_menu_container@2x.png")));
            zahhaak.setActiveImage(new Image(new FileInputStream("images/placeholders/gold_main_menu_container@2x.png")));
            zahhaak.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
            zahhaak.setTooltip(new Tooltip("Hold the Flag"));
        } catch (FileNotFoundException ignored) {
        }
        zahhaak.setText("Zahhaak");
        zahhaak.setOnMouseClicked(e -> {
            UI.decide("2");
            if (UI.getMenu() == Menus.GAME_MENU)
                MenuManager.getInstance().setCurrentMenu(Id.IN_GAME_MENU);
            else
                showPopUp("You have no main deck to play with.");
        });
        addComponent(zahhaak);

        GUIButton Aarash = new GUIButton(2 * windowWidth / 3 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2, 170, 50);
        try {
            Aarash.setImage(new Image(new FileInputStream("images/placeholders/diamond_main_menu_container@2x.png")));
            Aarash.setActiveImage(new Image(new FileInputStream("images/placeholders/gold_main_menu_container@2x.png")));
            Aarash.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
            Aarash.setTooltip(new Tooltip("Collect the Flags"));
        } catch (FileNotFoundException ignored) {
        }
        Aarash.setText("Aarash");
        Aarash.setOnMouseClicked(e -> {
            UI.decide("3");
            if (UI.getMenu() == Menus.GAME_MENU)
                MenuManager.getInstance().setCurrentMenu(Id.IN_GAME_MENU);
            else
                showPopUp("You have no main deck to play with.");
        });
        addComponent(Aarash);
    }
}
