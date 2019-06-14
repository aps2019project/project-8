package view;

import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;


public class NodeWrapper implements MenuComponent {

    private Node value;

    public NodeWrapper(Node node) {this.value = node;}

    public Node getValue() {return this.value;}

    public void setColor(Color color) {
        if (value instanceof ImageView) {
            ImageView imageView = (ImageView) value;
            ColorAdjust monochrome = new ColorAdjust();
            monochrome.setSaturation(-1.0);

            Blend blush = new Blend(
                    BlendMode.MULTIPLY,
                    monochrome,
                    new ColorInput(
                            0,
                            0,
                            imageView.getFitWidth(),
                            imageView.getFitHeight(),
                            color
                    )
            );

            imageView.setEffect(blush);
            imageView.setCache(true);
            imageView.setCacheHint(CacheHint.SPEED);
        }
    }

    public void disableMouseEvents() {
        value.setOnMouseClicked(e -> {});
        value.setOnMousePressed(e -> {});
        value.setOnDragDetected(e -> {});
        value.setOnMouseDragEntered(e -> {});
        value.setOnMouseDragExited(e -> {});
        value.setOnMouseDragReleased(e -> {});
        value.setOnMouseEntered(e -> {});
        value.setOnMouseExited(e -> {});
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof NodeWrapper)
            return value.equals(((NodeWrapper) object).getValue());
        if (object instanceof Node)
            return value.equals(object);
        return false;
    }
}
