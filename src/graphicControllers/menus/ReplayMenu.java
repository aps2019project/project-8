package graphicControllers.menus;

import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class ReplayMenu extends Menu {
    private transient static int numberOfGamesPlayed;
    private transient static HashMap<String, GameMenu> gamesHistory = new HashMap<>();

    public static GameMenu getGame(String ID) {
        return gamesHistory.get(ID);
    }

    public static ArrayList<String> getGames() {
        ArrayList<String> returnValue = new ArrayList<>();
        gamesHistory.forEach((key, value) -> returnValue.add(key));
        return returnValue;
    }

    public static void addGame(GameMenu gameMenu) {
        numberOfGamesPlayed++;
        gamesHistory.put("Game" + numberOfGamesPlayed, gameMenu);
    }

    int currentPhase = 0;
    GameMenu gameMenu;

    public void setGameMenu(GameMenu gameMenu) {
        this.gameMenu = gameMenu;
    }

    private void setPhase() {
        ArrayList<Node> nodes = gameMenu.history.get(currentPhase);
        getView().getGroup().getChildren().clear();
        getView().getGroup().getChildren().addAll(nodes);
        Rectangle rectangle = new Rectangle(0, 0, windowWidth, windowHeight);
        rectangle.setOpacity(0);
        getView().getGroup().getChildren().add(rectangle);
    }

    private void nextPhase() {
        currentPhase++;
        if (currentPhase >= gameMenu.history.size())
            currentPhase--;
    }

    private void prevPhase() {
        currentPhase--;
        if (currentPhase < 0)
            currentPhase++;
    }

    public ReplayMenu() {
        super(Id.REPLAY_MENU, "Replay", windowDefaultWidth, windowDefaultHeight);
        try {
            setBackGround(new Image(new FileInputStream("images/backgrounds/battlemap0_background@2x.png")));
        } catch (FileNotFoundException ignored) {
        }
        getView().getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                MenuManager.getInstance().setCurrentMenu(Id.MAIN_MENU);
            } else if (event.getCode() == KeyCode.RIGHT) {
                nextPhase();
                setPhase();
            } else if (event.getCode() == KeyCode.LEFT){
                prevPhase();
                setPhase();
            }
        });
    }
}
