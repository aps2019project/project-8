package graphicControllers.menus;

import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

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

    public void replay(GameMenu gameMenu) {
        new Thread(() -> {

            for (ArrayList<Node> nodes : gameMenu.history) {
                Platform.runLater(() -> {
                    getView().getGroup().getChildren().clear();
                    getView().getGroup().getChildren().addAll(nodes);
                    Rectangle rectangle = new Rectangle(0, 0, windowWidth, windowHeight);
                    rectangle.setOpacity(0);
                    getView().getGroup().getChildren().add(rectangle);
                });

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public ReplayMenu() {
        super(Id.REPLAY_MENU, "Replay", windowDefaultWidth, windowDefaultHeight);
        getView().getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                MenuManager.getInstance().setCurrentMenu(Id.MAIN_MENU);
            }
        });
    }
}
