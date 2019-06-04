package graphicControllers.menus;

import graphicControllers.Menu;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import view.*;

public class AccountMenu extends Menu {

    public AccountMenu() {
        super(Id.ACCOUNT_MENU, "Account Menu", 1240, 720);

        GUIChangeMenuButton goToMenu = new GUIChangeMenuButton(10, 10, 200, 50);
        goToMenu.setText("Go to main menu");
        goToMenu.setGoalMenuID(Id.MAIN_MENU);
        goToMenu.setOnMouseClicked(event -> System.out.println("shengdebao gone!"));
        addComponent(goToMenu);

        Button button = new Button("this is a butt!");
        button.relocate(300, 300);
        button.setOnKeyPressed(e -> System.out.println("DSAHF"));
        button.setOnMouseClicked(e -> System.out.println("Mouse clicked!"));
        addComponent(new NodeWrapper(button));
        Label label = new Label("masiri");
        label.relocate(500, 500);
        addComponent(new NodeWrapper(label));
    }


}
