package graphicControllers.menus;

import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import menus.Shop;
import menus.UI;
import model.Spell;
import model.UnitType;
import view.GUIButton;
import view.NodeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainMenu extends Menu {
    public MainMenu() {
        super(Id.MAIN_MENU, "Main Menu", windowDefaultWidth, windowDefaultHeight);

        try {
            ImageView imageView = new ImageView(new Image(new FileInputStream("images/logos/brand_duelyst@2x.png")));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(windowWidth / 3);
            imageView.relocate(windowWidth * 2 / 3 - 10, 10);
            addComponent(new NodeWrapper(imageView));
        } catch (FileNotFoundException e) {
        }

        try {
            getView().setBackground(new Image(new FileInputStream("images/backgrounds/background@2x.jpg")));
            getView().setCursor(new Image(new FileInputStream("./images/cursors/mouse_auto.png")));
        } catch (FileNotFoundException ignored) {
        }

        GUIButton gotoCollection = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2 - 2 * 10 - 2 * 50, 170, 50);
        try {
            gotoCollection.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            gotoCollection.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            gotoCollection.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        gotoCollection.setText("Collection");
        gotoCollection.setOnMouseClicked(e -> {
            UI.decide("collection");
            MenuManager.getInstance().setCurrentMenu(Id.COLLECTION_MENU);
        });
        addComponent(gotoCollection);

        GUIButton gotoShop = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2 - 10 - 50, 170, 50);
        try {
            gotoShop.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            gotoShop.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            gotoShop.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        gotoShop.setText("Shop");
        gotoShop.setOnMouseClicked(e -> {
            UI.decide("shop");
            MenuManager.getInstance().setCurrentMenu(Id.SHOP_MENU);
        });
        addComponent(gotoShop);

        GUIButton gotoBattle = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2, 170, 50);
        try {
            gotoBattle.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            gotoBattle.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            gotoBattle.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        gotoBattle.setText("Battle");
        gotoBattle.setOnMouseClicked(e -> {
            UI.decide("battle");
            MenuManager.getInstance().setCurrentMenu(Id.CHOOSE_BATTLE_MENU);
        });
        addComponent(gotoBattle);

        GUIButton logout = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2 + 10 + 50, 170, 50);
        try {
            logout.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            logout.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            logout.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        logout.setText("Logout");
        logout.setOnMouseClicked(e -> {
            UI.decide("logout");
            MenuManager.getInstance().setCurrentMenu(Id.ACCOUNT_MENU);
        });
        addComponent(logout);

        GUIButton save = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2 + 2 * 10 + 2 * 50, 170, 50);
        try {
            save.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            save.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            save.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        save.setText("Save");
        save.setOnMouseClicked(e -> {
            showPopUp("Successfully saved account.");
            UI.decide("save");
        });
        addComponent(save);

        GUIButton createCard = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2 + 3 * 10 + 3 * 50, 170, 50);
        try {
            createCard.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            createCard.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            createCard.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        createCard.setText("Create Card");
        createCard.setOnMouseClicked(e -> {
            popUpGetText("Name", "Next").ifPresent(name -> {
                if (Shop.getCollectionItemByName(name) != null) {
                    showPopUp("A card with this name already exists.");
                    return;
                }
                popUpGetText("Description", "Next").orElse("");
                Optional<String> price = popUpGetText("Price", "Next");
                if (!price.isPresent())
                    return;
                if (!price.get().matches("\\d+")) {
                    showPopUp("Please enter a number.");
                    return;
                }
                //Card
                popUpGetList(Arrays.asList("Spell", "Minion", "Hero"), "Next", "Type").ifPresent(type -> {
                    if (type.equals("Spell")) {
                        Optional<String> mana = popUpGetText("Price", "Next");
                        if (!mana.isPresent())
                            return;
                        if (!mana.get().matches("\\d+")) {
                            showPopUp("Please enter a number.");
                            return;
                        }
                        Optional<String> targetType = popUpGetList(Arrays.stream(Spell.TargetType.values()).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()), "Next", "Target type");
                        if (!targetType.isPresent())
                            return;
                        System.err.println("here");
                        System.err.println(Arrays.asList(Arrays.stream(Spell.TargetType.values()).map(Enum::toString)).indexOf(revertCase(targetType.get())));
                        System.err.println("aftre");
                        Spell.TargetArea.values();
                        Arrays.asList("Yes", "No");
                        popUpGetText("Number of random picks", "Next");
                        popUpGetText("Number of buffs", "Next");
                    } else {
                        if (type.equals("Minion")) {
                            Optional<String> mana = popUpGetText("Price", "Next");
                            if (!mana.isPresent())
                                return;
                            if (!mana.get().matches("\\d+")) {
                                showPopUp("Please enter a number.");
                                return;
                            }
                        }
                        Optional<String> ap = popUpGetText("AP", "Next");
                        if (!ap.isPresent())
                            return;
                        if (!ap.get().matches("\\d+")) {
                            showPopUp("Please enter a number.");
                            return;
                        }
                        Optional<String> hp = popUpGetText("HP", "Next");
                        if (!hp.isPresent())
                            return;
                        if (!hp.get().matches("\\d+")) {
                            showPopUp("Please enter a number.");
                            return;
                        }
                        Optional<String> attackType = popUpGetList(Arrays.asList(UnitType.values()), "Next", "Attack type");
                        if (!attackType.isPresent())
                            return;
                        Optional<String> range = popUpGetText("HP", "Next");
                        if (!range.isPresent())
                            return;
                        if (!range.get().matches("\\d+")) {
                            showPopUp("Please enter a number.");
                            return;
                        }
                    }
                });
            });
        });
        addComponent(createCard);

        GUIButton exit = new GUIButton(windowWidth - 100, windowHeight - 50, 100, 50);
        try {
            exit.setImage(new Image(new FileInputStream("./images/buttons/button_cancel@2x.png")));
            exit.setActiveImage(new Image(new FileInputStream("./images/buttons/button_cancel_glow@2x.png")));
            exit.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        exit.setText("Exit");
        addComponent(exit);
        exit.setOnMouseClicked(e -> System.exit(0));
    }

    private String revertCase(String s) {
        return s.replaceAll(" ", "_").toUpperCase();
    }

    private String fixCase(String enumConst) {
        enumConst = enumConst.replaceAll("_", " ").toLowerCase();
        enumConst = enumConst.substring(0, 1).toUpperCase() + enumConst.substring(1);
        return enumConst;
    }
}
