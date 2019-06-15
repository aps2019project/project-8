package view;

import gen.NamesAndTypes;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class CardView implements MenuComponent {
    private static final double SHADOW_WIDTH = 250;
    private static final double SHADOW_HEIGHT = 320;

    private static final double BACKGROUND_WIDTH = 201;
    private static final double BACKGROUND_HEIGHT = 268;

    private static final double SYMBOL_WIDTH = 100.5;
    private static final double SYMBOL_HEIGHT = 134;

    private static final double GLOWLINE_WIDTH = 225;
    private static final double GLOWLINE_HEIGHT = 290;

    private final StackPane stackPane = new StackPane();
    private ArrayList<ImageView> components = new ArrayList<>();

    public CardView(CollectionItem collectionItem) {
        stackPane.setMinWidth(SHADOW_WIDTH + 100);
        stackPane.setPrefWidth(SHADOW_WIDTH + 100);
        stackPane.setMaxWidth(SHADOW_WIDTH + 100);
        stackPane.setMinHeight(SHADOW_HEIGHT);
        stackPane.setPrefHeight(SHADOW_HEIGHT);
        stackPane.setMaxHeight(SHADOW_HEIGHT);
        stackPane.setAlignment(Pos.CENTER);
        try {
            ImageView shadow = new ImageView(new Image(new FileInputStream("images/cards/card_shadow_map.png")));
            fixComponent(shadow, SHADOW_WIDTH, SHADOW_HEIGHT);
            ImageView background = new ImageView();
            ImageView symbol = new ImageView();
            setCard(collectionItem, background, symbol);
            fixComponent(background, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
            fixComponent(symbol, SYMBOL_WIDTH, SYMBOL_HEIGHT);
            Label name = new Label(fixCase(collectionItem.getName()));
            name.setTextFill(Color.BLANCHEDALMOND);
            name.setFont(Font.loadFont(new FileInputStream("./fonts/averta-black-webfont.ttf"), 15));
            Label type = new Label(fixCase(NamesAndTypes.getType(collectionItem.getName())));
            type.setTextFill(Color.AQUAMARINE);
            type.setFont(Font.loadFont(new FileInputStream("./fonts/averta-extrathin-webfont.ttf"), 15));
            Label description = new Label(collectionItem.getDescription());
            description.setWrapText(true);
            description.setTextFill(Color.AQUA);
            description.setFont(Font.loadFont(new FileInputStream("./fonts/averta-extrathin-webfont.ttf"), 10));
            description.setMaxWidth(180);
            description.setAlignment(Pos.CENTER);
            description.setTextAlignment(TextAlignment.CENTER);
            ImageView glowLine = new ImageView(new Image(new FileInputStream("images/cards/card_glow_line_new@2x.png")));
            fixComponent(glowLine, GLOWLINE_WIDTH, GLOWLINE_HEIGHT);
            stackPane.getChildren().add(shadow);
            stackPane.getChildren().add(background);
            stackPane.getChildren().add(symbol);
            StackPane.setMargin(symbol, new Insets(0, 0, SHADOW_HEIGHT / 2, 0));
            stackPane.getChildren().add(name);
            StackPane.setMargin(name, new Insets(0, 0, 30, 0));
            stackPane.getChildren().add(type);
            stackPane.getChildren().add(description);
            StackPane.setMargin(description, new Insets(160, 0, 0, 0));
            stackPane.getChildren().add(glowLine);
            StackPane.setMargin(glowLine, new Insets(0, 0, 2, 0));
            if (collectionItem instanceof Unit) {
                Label unitClass = new Label(((Unit) collectionItem).getUnitType().toString());
                unitClass.setFont(Font.loadFont(new FileInputStream("./fonts/averta-black-webfont.ttf"), 15));
                unitClass.setTextFill(Color.ORANGE);
                stackPane.getChildren().add(unitClass);
                StackPane.setMargin(unitClass, new Insets(30, 0, 0, 0));
                Label hp = new Label(String.valueOf(((Unit) collectionItem).getHitPoint()));
                hp.setFont(Font.loadFont(new FileInputStream("./fonts/averta-black-webfont.ttf"), 15));
                hp.setTextFill(Color.CRIMSON);
                stackPane.getChildren().add(hp);
                StackPane.setMargin(hp, new Insets(55, 0, 0, 105));
                Label ap = new Label(String.valueOf(((Unit) collectionItem).getAttackPoint()));
                ap.setFont(Font.loadFont(new FileInputStream("./fonts/averta-black-webfont.ttf"), 15));
                ap.setTextFill(Color.GOLD);
                stackPane.getChildren().add(ap);
                StackPane.setMargin(ap, new Insets(55, 105, 0, 0));
            }
            if (collectionItem instanceof Minion || collectionItem instanceof SpellCard || (collectionItem instanceof Hero && ((Hero) collectionItem).getSpecialPowerTypes().stream().anyMatch(o -> o == SpecialPowerType.ON_USE))) {
                ImageView mana = new ImageView(new Image(new FileInputStream("images/cards/icon_mana@2x.png")));
                fixComponent(mana, 40, 40);
                stackPane.getChildren().add(mana);
                StackPane.setMargin(mana, new Insets(0, 170, 250, 0));
                Label mp = new Label(String.valueOf(((Card) collectionItem).getManaCost()));
                mp.setFont(Font.loadFont(new FileInputStream("./fonts/averta-black-webfont.ttf"), 15));
                mp.setTextFill(Color.BLACK);
                stackPane.getChildren().add(mp);
                StackPane.setMargin(mp, new Insets(0, 170, 250, 0));
            }
            if (collectionItem instanceof Hero && ((Hero) collectionItem).getSpecialPowerTypes().stream().anyMatch(o -> o == SpecialPowerType.ON_USE)) {
                ImageView cooldown = new ImageView(new Image(new FileInputStream("images/cards/icon_cooldown_counter@2x.png")));
                fixComponent(cooldown, 60, 40);
                stackPane.getChildren().add(cooldown);
                StackPane.setMargin(cooldown, new Insets(0, 0, 250, 170));
                Label cd = new Label(String.valueOf(((Hero) collectionItem).getOriginalCooldown()));
                cd.setFont(Font.loadFont(new FileInputStream("./fonts/averta-black-webfont.ttf"), 15));
                cd.setTextFill(Color.BLACK);
                stackPane.getChildren().add(cd);
                StackPane.setMargin(cd, new Insets(0, 0, 250, 170));
            }
        } catch (FileNotFoundException ignored) {
        }
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    private String fixCase(String name) {
        String string = name.replaceAll("([A-Za-z][a-z]*)", "$1 ").trim();
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private void setCard(CollectionItem collectionItem, ImageView background, ImageView symbol) throws FileNotFoundException {
        String collectionItemName = collectionItem.getName();
        String directory = "images/gameIcons/gifs/" + NamesAndTypes.getType(collectionItemName) + "/" + collectionItemName;
        if (collectionItem instanceof Unit) {
            background.setImage(new Image(new FileInputStream("images/cards/neutral_prismatic_unit@2x.png")));
            symbol.setImage(new Image(new FileInputStream(directory + "/breathing.gif")));

        } else if (collectionItem instanceof SpellCard) {
            background.setImage(new Image(new FileInputStream("images/cards/neutral_prismatic_spell@2x.png")));
            symbol.setImage(new Image(new FileInputStream(directory + "/actionbar.gif")));
        } else {
            background.setImage(new Image(new FileInputStream("images/cards/neutral_prismatic_artifact@2x.png")));
            symbol.setImage(new Image(new FileInputStream(directory + "/actionbar.gif")));
        }
    }

    private void fixComponent(ImageView imageView, double width, double height) {
        components.add(imageView);
        setDimensions(imageView, width, height);
    }

    private void setDimensions(ImageView glowLine, double width, double height) {
        glowLine.setFitWidth(width);
        glowLine.setFitHeight(height);
    }

    public CardView relocate(double x, double y) {
        stackPane.relocate(x, y);
        return this;
    }

    public void addInGroup(Group group) {
        group.getChildren().add(stackPane);
    }

    public void removeFromGroup(Group group) {
        group.getChildren().remove(stackPane);
    }
}
