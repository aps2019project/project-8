package graphicControllers;

import graphicControllers.menus.*;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import menus.ShopMenu;
import menus.UI;

import java.io.File;

public class GUI extends Application {

    private Stage currentStage;

    /**
     * here you must initiate the menuManager
     * add menus in format menuManager.addMenu(new "YOUR MENU CONSTRUCTOR")
     * then enter menuManager.serCurrentMenu("INITIAL MENU")
     */

    private void initiate(MenuManager menuManager) {
        Menu initialMenu = new AccountMenu();
        try {
            menuManager.addMenu(initialMenu);
            menuManager.addMenu(new LoginMenu());
            menuManager.addMenu(new MainMenu());
            menuManager.addMenu(new SignInMenu());
            menuManager.addMenu(new Leaderboard());
            menuManager.addMenu(new BattleMenu());
            menuManager.addMenu(new SinglePlayerMenu());
            menuManager.addMenu(new CollectionMenu());
            menuManager.addMenu(new ShopMenu());
            menuManager.addMenu(new StoryMenu());
            menuManager.addMenu(new CustomGameMenu());
            menuManager.addMenu(new MultiplayerMenu());
        } catch (MenuAlreadyCreatedException e) {
            e.printStackTrace();
        }
        menuManager.setCurrentMenu(initialMenu);
    }

    @Override
    public void start(Stage primaryStage) {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("music/mainmenu_v2c_looping.m4a").toURI().toString()));
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        mediaPlayer.play();
        UI.initiate();
        MenuManager menuManager = new MenuManager();
        primaryStage.setTitle("Duelyst");
        initiate(menuManager);
        menuManager.setStage(primaryStage);
        menuManager.startProcessOnStage();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
