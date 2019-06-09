package menus;

import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import view.GUIButton;
import view.NodeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ShopMenu extends Menu {
    public ShopMenu() {
        super(Id.SHOP_MENU, "Shop Menu", 1240, 720);

        try {
            ImageView imageView = new ImageView(new Image(new FileInputStream("images/logos/brand_duelyst@2x.png")));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(windowWidth / 3);
            imageView.relocate(windowWidth * 2 / 3 - 10, 10);
            addComponent(new NodeWrapper(imageView));
        } catch (
                FileNotFoundException e) {
        }

        try {
            getView().setBackground(new Image(new FileInputStream("images/backgrounds/background@2x.jpg")));
            getView().setCursor(new Image(new FileInputStream("./images/cursors/mouse_auto.png")));
        } catch (FileNotFoundException ignored) {
        }

        GUIButton showCollection = new GUIButton(windowWidth / 3 - 180.0 / 2, windowHeight
                / 2 - 50.0 / 2 - 10 - 50, 180, 50);
        try {
            showCollection.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            showCollection.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            showCollection.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        showCollection.setText("Show Collection");
        addComponent(showCollection);

        GUIButton search = new GUIButton(windowWidth / 3 - 180.0 / 2, windowHeight
                / 2 - 50.0 / 2, 180, 50);
        try {
            search.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            search.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            search.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        search.setText("Search");
        addComponent(search);

        GUIButton searchCollection = new GUIButton(windowWidth / 3 - 180.0 / 2, windowHeight
                / 2 - 50.0 / 2 + 10 + 50, 180, 50);
        try {
            searchCollection.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            searchCollection.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            searchCollection.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        searchCollection.setText("Search Collection");
        addComponent(searchCollection);

        GUIButton buy = new GUIButton(2 * windowWidth / 3 - 180.0 / 2, windowHeight
                / 2 - 50.0 / 2 - 10 - 50, 180, 50);
        try {
            buy.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            buy.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            buy.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        buy.setText("Buy");
        addComponent(buy);

        GUIButton sell = new GUIButton(2 * windowWidth / 3 - 180.0 / 2, windowHeight
                / 2 - 50.0 / 2, 180, 50);
        try {
            sell.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            sell.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            sell.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        sell.setText("Sell");
        addComponent(sell);

        GUIButton show = new GUIButton(2 * windowWidth / 3 - 180.0 / 2, windowHeight
                / 2 - 50.0 / 2 + 10 + 50, 180, 50);
        try {
            show.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            show.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            show.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        show.setText("Show");
        addComponent(show);

        GUIButton back = new GUIButton(windowWidth / 2 - 100.0 / 2, windowHeight - 50,
                100, 50);
        try {
            back.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            back.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            back.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        back.setText("Back");
        back.setOnMouseClicked(e -> {
            UI.decide("exit");
            MenuManager.getInstance().setCurrentMenu(Id.MAIN_MENU);
        });
        addComponent(back);
    }
}
