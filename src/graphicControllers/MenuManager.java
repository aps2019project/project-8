package graphicControllers;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MenuChangeComponent;

import java.util.HashMap;
import java.util.Map;

public class MenuManager {

    /**
     * Stage should be removed!
     */
    Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }


    private Menu currentMenu;

    private Map<Integer, Menu> menusIDs = new HashMap<>();

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }


    void listenForMenuChange() {
        stage.setScene(currentMenu.getView().getScene());
        for (MenuChangeComponent changeComponent: currentMenu.getMenuChangeComponents()) {
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

    public void startProcessOnStage() {
        listenForMenuChange();
        stage.show();
    }
}
