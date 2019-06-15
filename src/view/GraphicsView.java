package view;

import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.ImagePattern;
import menus.UI;


public class GraphicsView {
    private Scene scene;
    private Group group;

    boolean consoleOn = false;
    TextField consoleTextField;

    public GraphicsView(double windowWidth, double windowHeight, String title) {
        this.group = new Group();
        this.scene = new Scene(group, windowWidth, windowHeight);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (!consoleOn) {
                    consoleTextField = new TextField();
                    consoleTextField.setMinSize(windowWidth - 30, 30);
                    consoleTextField.relocate(15, windowHeight - 15 - 30);
                    consoleTextField.setPromptText("Enter in console");
                    group.getChildren().add(consoleTextField);
                    consoleOn = true;
                } else {
                    group.getChildren().removeAll(consoleTextField);
                    consoleOn = false;
                }
            } else if (event.getCode() == KeyCode.ENTER) {
                if (consoleOn) {
                    UI.decide(consoleTextField.getText());
                    consoleTextField.clear();
                    consoleTextField.setPromptText("Enter in console");
                }
            }
        });
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
        if (menuComponent instanceof CardView)
            ((CardView) menuComponent).addInGroup(group);
        /**
         * you can add new menu components here
         */
    }

    public void removeComponent(MenuComponent menuComponent) {
        if (menuComponent instanceof NodeWrapper)
            group.getChildren().remove(((NodeWrapper) menuComponent).getValue());
        if (menuComponent instanceof GUIButton)
            ((GUIButton) menuComponent).removeFromGroup(group);
        if (menuComponent instanceof CardView)
            ((CardView) menuComponent).removeFromGroup(group);
        /**
         * you can remove menu components here
         */
    }

    public Scene getScene() {
        return scene;
    }
}
