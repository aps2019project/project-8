package view;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class GUIButton implements MenuComponent {
    private EventHandler<MouseEvent> mouseClick;
    private EventHandler<MouseEvent> mouseEnter;
    private EventHandler<MouseEvent> mouseExit;

    private double frame = 5;
    private double x, y;
    private double width, height;
    protected Button button;
    private Rectangle background;

    private ArrayList<Node> components = new ArrayList<>();

    public GUIButton() {

    }

    public GUIButton(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        frame = Math.min(width, height) / 10;

        button = new Button(".");
        button.relocate(x, y);
        button.setMinSize(width, height);
        button.setMaxSize(width, height);
        button.setOpacity(0);

        background = new Rectangle(x, y, width, height);
        background.setFill(Color.rgb(38, 31, 93));
        background.setOpacity(0);

        mouseEnter = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                background.setOpacity(1);
            }
        };

        mouseExit = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                background.setOpacity(0);
            }
        };
        button.setOnMouseEntered(mouseEnter);
        button.setOnMouseExited(mouseExit);
    }

    public void setImage(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width - frame * 2);
        imageView.setFitHeight(height - frame * 2);
        imageView.relocate(x + frame, y + frame);
        components.add(imageView);
    }

    public void setText(String text) {
        Label label = new Label(text);
        label.setMinSize(width - frame * 2, height - frame * 2);
        label.setMaxSize(width - frame * 2, height - frame * 2);
        label.relocate(x + frame, y + frame);
        label.setAlignment(Pos.CENTER);
        Rectangle rectangle = new Rectangle(x + frame, y + frame, width - frame * 2, height - frame * 2);
        rectangle.setFill(Color.rgb(71, 109, 151));
        components.add(rectangle);
        components.add(label);
    }

    public void setOnMouseClicked(EventHandler<MouseEvent> mouseClick) {
        button.setOnMouseClicked(mouseClick);
        /*
        button.setOnMouseReleased(event -> {
            AnimationTimer animationTimer = new AnimationTimer() {
                boolean hasStarted = false;
                long firstTime = 0;
                long totalAnimationTime = 1000 * 1000 * 200;
                long cnt = 0;

                @Override
                public void handle(long now) {
                    if (!hasStarted) {
                        hasStarted = true;
                        firstTime = now;
                    }

                    long passedTime = now - firstTime;
                    for (Node component: components) {
                        if (passedTime > totalAnimationTime) {
                            if (cnt > 0) {
                                cnt--;
                                component.relocate(component.getLayoutX() - 0.01, component.getLayoutY() - 0.01);
                                if (component instanceof ImageView) {
                                    ((ImageView) component).setFitHeight(((ImageView) component).getFitHeight() + 0.02);
                                    ((ImageView) component).setFitWidth(((ImageView) component).getFitWidth() + 0.02);
                                }
                            } else {
                                stop();
                            }
                        } else {
                            component.relocate(component.getLayoutX() + 0.01, component.getLayoutY() + 0.01);
                            if (component instanceof ImageView) {
                                ((ImageView) component).setFitHeight(((ImageView) component).getFitHeight() - 0.02);
                                ((ImageView) component).setFitWidth(((ImageView) component).getFitWidth() - 0.02);
                            }
                            cnt++;
                        }
                    }
                }
            };
            animationTimer.start();
        });
        */
    }

    public void setOnMouseEntered(EventHandler<MouseEvent> mouseEnter) {
        this.mouseEnter = mouseEnter;
    }

    public void setOnMouseExited(EventHandler<MouseEvent> mouseExit) {
        this.mouseExit = mouseExit;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public GUIButton setX(double x) {
        this.x = x;
        background.setLayoutX(x);
        for (Node node : components)
            node.setLayoutX(x + frame);
        button.setLayoutX(x);
        return this;
    }

    public GUIButton setY(double y) {
        this.y = y;
        background.setLayoutY(y);
        for (Node node : components)
            node.setLayoutY(y + frame);
        button.setLayoutY(y);
        return this;
    }

    public GUIButton setCentreX(double x) {
        return setX(x - width / 2);
    }

    public GUIButton setCentreY(double y) {
        return setY(y - height / 2);
    }

    public void addInGroup(Group group) {
        group.getChildren().add(background);
        for (Node node : components)
            group.getChildren().add(node);
        group.getChildren().add(button);
    }

    public void removeFromGroup(Group group) {
        group.getChildren().remove(background);
        for (Node node : components)
            group.getChildren().remove(node);
        group.getChildren().remove(button);
    }
}
