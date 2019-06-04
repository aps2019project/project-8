package graphicControllers.menus;

import graphicControllers.Menu;
import view.GUIChangeMenuButton;

public class MainMenu extends Menu {
    public MainMenu() {
        super(Id.MAIN_MENU, "Main Menu", 1240, 720);

        GUIChangeMenuButton guiChangeMenuButton = new GUIChangeMenuButton(0, 0, 100, 100);
        guiChangeMenuButton.setText("Back");
        guiChangeMenuButton.setGoalMenuID(Id.ACCOUNT_MENU);
        addComponent(guiChangeMenuButton);
    }
}
