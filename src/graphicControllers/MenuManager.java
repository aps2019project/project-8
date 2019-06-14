package graphicControllers;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.GUIChangeMenuButton;
import view.MenuChangeComponent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MenuManager {

    private static MenuManager instance = null;
    /**
     * Stage should be removed!
     */
    private Stage stage;
    private Menu currentMenu;
    private Map<Integer, Menu> menusIDs = new HashMap<>();

    private Optional[] gameMode = new Optional[]{Optional.empty(), Optional.empty()};

    public static MenuManager getInstance() {
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void listenForMenuChange() {
        stage.setScene(currentMenu.getView().getScene());
        for (MenuChangeComponent changeComponent : currentMenu.getMenuChangeComponents()) {
            if (changeComponent.isReady()) {
                changeComponent.setOnAction(event -> {
                    try {
                        int goalMenuID = changeComponent.getGoalMenuID();
                        boolean success = false;
                        for (Map.Entry<Integer, Menu> entry : menusIDs.entrySet()) {
                            if (entry.getKey().equals(goalMenuID)) {
                                System.err.println(entry.getValue());
                                setCurrentMenu(entry.getValue());
                                success = true;
                                break;
                            }
                        }
                        if (!success) {
                            throw new MenuDoesNotExistException();
                        }
                        listenForMenuChange();
                    } catch (MenuDoesNotExistException e1) {
                        e1.printStackTrace();
                    }
                });
            }
        }
    }

    public void addMenu(Menu menu) throws MenuAlreadyCreatedException {
        int id = menu.getId();
        if (menusIDs.containsKey(id))
            throw new MenuAlreadyCreatedException();
        menusIDs.put(menu.getId(), menu);
    }

    public void back() {
        setCurrentMenu(this.currentMenu.getParentMenu());
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        currentMenu.refresh();
        this.currentMenu = currentMenu;
    }

    public void setCurrentMenu(int menuID) {
        currentMenu = menusIDs.get(menuID);
        currentMenu.refresh();
        listenForMenuChange();
    }

    public void startProcessOnStage() {
        instance = this;
//        setUpPopUp();
//        setUpGetGameMode();
//        setUpGetText();
        listenForMenuChange();
        stage.setResizable(false);
        stage.show();
    }
}