package view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GraphicsView {
    private Scene scene;
    private Group group;

    public GraphicsView(int windowWidth, int windowHeight, String title) {
        this.group = new Group();
        this.scene = new Scene(group, windowWidth, windowHeight);
    }

    public void addComponent(MenuComponent menuComponent) {
        if (menuComponent instanceof NodeWrapper)
            group.getChildren().add(((NodeWrapper) menuComponent).getValue());
        if (menuComponent instanceof GUIButton)
            ((GUIButton) menuComponent).addInGroup(group);
        /**
         * you can add new menu components here
         */
    }

    public void removeComponent(MenuComponent menuComponent) {
        if (menuComponent instanceof NodeWrapper)
            group.getChildren().remove(((NodeWrapper) menuComponent).getValue());
        if (menuComponent instanceof GUIButton)
            ((GUIButton) menuComponent).removeFromGroup(group);
        /**
         * you can remove menu components here
         */
    }

    public Scene getScene() {
        return scene;
    }
}
