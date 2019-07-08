package graphicControllers.menus;

import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import menus.UI;
import view.GUIButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;

public class WaitingMenu extends Menu {
    private Checker checker = new Checker();
    public WaitingMenu() {
        super(Id.WAITING_MENU, "Waiting Menu", 800, 600);
        try {
            getView().setBackground(new Image(new FileInputStream("images/backgrounds/background@2x.jpg")));
            getView().setCursor(new Image(new FileInputStream("./images/cursors/mouse_auto.png")));
        } catch (FileNotFoundException ignored) {
        }

        try {
            getView().setBackground(new Image(new FileInputStream("images/backgrounds/background@2x.jpg")));
            getView().setCursor(new Image(new FileInputStream("./images/cursors/mouse_auto.png")));
        } catch (FileNotFoundException ignored) {
        }

        GUIButton cancel = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2, 170, 50);
        try {
            cancel.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            cancel.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            cancel.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        cancel.setText("Cancel");
        cancel.setOnMouseClicked(e -> {
            checker.interrupt();
            menus.Menu.getConnection().enterMultiplayerMenu(false, 0, 0);
            MenuManager.getInstance().setCurrentMenu(Id.CHOOSE_BATTLE_MENU);
        });
        addComponent(cancel);
    }

    @Override
    public void refresh() {
        checker = new Checker();
        checker.start();
    }

    private class Checker extends Thread {
        @Override
        public void run() {
            while (!interrupted()) {
                System.out.println("hera");
                if (menus.Menu.getConnection().inGame().equals("yes")) {
                    Platform.runLater(() -> {
                        MenuManager.getInstance().setCurrentMenu(Id.IN_GAME_MENU);
                    });
                    interrupt();
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    interrupt();
//                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}
