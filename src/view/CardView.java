package view;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.CollectionItem;
import model.SpellCard;
import model.Unit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class CardView implements MenuComponent {
    private static final double SHADOW_WIDTH = 250;
    private static final double SHADOW_HEIGHT = 320;

    private static final double BACKGROUND_WIDTH = 201;
    private static final double BACKGROUND_HEIGHT = 268;

    private static final double IMAGE_WIDTH = 50;
    private static final double IMAGE_HEIGHT = 50;

    private static final double GLOWLINE_WIDTH = 225;
    private static final double GLOWLINE_HEIGHT = 292;

    private ArrayList<ImageView> components = new ArrayList();

    public CardView(CollectionItem collectionItem) {
        try {
            fixComponent(new ImageView(new Image(new FileInputStream("images/cards/card_shadow_map.png"))), SHADOW_WIDTH, SHADOW_HEIGHT);
            ImageView background = new ImageView();
            if (collectionItem instanceof Unit)
                background.setImage(new Image(new FileInputStream("images/cards/unusable_prismatic_unit@2x.png")));
            else if (collectionItem instanceof SpellCard)
                background.setImage(new Image(new FileInputStream("images/cards/unusable_prismatic_spell@2x.png")));
            else
                background.setImage(new Image(new FileInputStream("images/cards/unusable_prismatic_artifact@2x.png")));
            fixComponent(background, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
            fixComponent(new ImageView(new Image(new FileInputStream("images/gameIcons/gifs/" + collectionItem.getName() + "/breathing.gif"))), IMAGE_WIDTH, IMAGE_HEIGHT);
            fixComponent(new ImageView(new Image(new FileInputStream("images/cards/card_glow_line_new@2x.png"))), GLOWLINE_WIDTH, GLOWLINE_HEIGHT);
        } catch (FileNotFoundException ignored) {
        }
    }

    private void fixComponent(ImageView shadow, double width, double height) {
        components.add(shadow);
        setDimensions(shadow, width, height);
    }

    private void setDimensions(ImageView glowLine, double width, double height) {
        glowLine.setFitWidth(width);
        glowLine.setFitHeight(height);
    }

    public CardView relocate(double x, double y) {
        components.forEach(o -> o.relocate(x - o.getFitWidth() / 2, y - o.getFitHeight() / 2));
        return this;
    }

    public void addInGroup(Group group) {
        components.forEach(o -> group.getChildren().add(o));
    }
}
