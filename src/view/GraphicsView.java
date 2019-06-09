package view;

import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class GraphicsView {
    private Scene scene;
    private Group group;

    public GraphicsView(double windowWidth, double windowHeight, String title) {
        this.group = new Group();
        this.scene = new Scene(group, windowWidth, windowHeight);
    }

    public void setBackground(Image image) {
        scene.setFill(new ImagePattern(image));
    }

    public void setCursor(Image image) {
        scene.setCursor(new ImageCursor(image));
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
