package graphicControllers;

import graphicControllers.menus.GameMenu;
import javafx.stage.Stage;
import view.MenuChangeComponent;

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
        if (currentMenu instanceof GameMenu)
            currentMenu = new GameMenu();
        currentMenu.refresh();
        currentMenu.stopMedia();
        this.currentMenu = currentMenu;
//        currentMenu.playMedia();
    }

    public void setCurrentMenu(int menuID) {
        currentMenu = menusIDs.get(menuID);
        if (currentMenu instanceof GameMenu)
            currentMenu = new GameMenu();
        currentMenu.refresh();
        listenForMenuChange();
    }

    public void startProcessOnStage() {
        instance = this;
        listenForMenuChange();
        stage.setResizable(false);
        stage.show();
    }
}