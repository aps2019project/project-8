package graphicControllers;

import graphicControllers.menus.*;
import javafx.application.Application;
import javafx.stage.Stage;
import graphicControllers.menus.ShopMenu;
import menus.UI;

import java.io.File;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
            menuManager.addMenu(new GameMenu());
            menuManager.addMenu(new Graveyard());
        } catch (MenuAlreadyCreatedException e) {
            e.printStackTrace();
        }
        menuManager.setCurrentMenu(initialMenu);
    }

    @Override
    public void start(Stage primaryStage) {
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
