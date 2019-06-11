package graphicControllers.menus;

import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import menus.UI;
import view.GUIButton;
import view.NodeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

public class CollectionMenu extends Menu {
    public CollectionMenu() {
        super(Id.COLLECTION_MENU, "Collection Menu", windowDefaultWidth, windowDefaultHeight);

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

        GUIButton show = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2 - 10 - 50, 170, 50);
        try {
            show.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            show.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            show.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        show.setText("Show");
        addComponent(show);

        GUIButton search = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2, 170, 50);
        try {
            search.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            search.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            search.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        search.setText("Search");
        search.setOnMouseClicked(e -> {
            Optional<String> name = popUpGetText("Card name | item name", "Search");
            if (name.isPresent()) {
                if (!UI.getAccount().getCollection().hasCollectionItem(name.get()))
                    showPopUp("No such item found.");
                else {
                    UI.decide("search " + name.get());
                    UI.getAccount().getCollection().getCollectionItemIDs(name.get());
                }
            }
        });
        addComponent(search);

        GUIButton save = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                / 2 - 50.0 / 2 + 10 + 50, 170, 50);
        try {
            save.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            save.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            save.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        save.setText("Save");
        save.setOnMouseClicked(e -> {
            showPopUp("Successfully saved account.");
            UI.decide("save");
        });
        addComponent(save);

        GUIButton createDeck = new GUIButton(windowWidth / 2 - 170.0 / 2 + windowWidth / 3, windowHeight
                / 2 - 50.0 / 2 - 2 * 10 - 2 * 50, 170, 50);
        try {
            createDeck.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            createDeck.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            createDeck.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        createDeck.setText("Create");
        createDeck.setOnMouseClicked(e -> {
            Optional<String> deckName = popUpGetText("Deck name", "Create");
            if (deckName.isPresent()) {
                if (UI.getAccount().getCollection().hasDeck(deckName.get()))
                    showPopUp("A deck with this name already exists.");
                else
                    showPopUp("Deck successfully created.");
                UI.decide("create deck " + deckName.orElse(""));
            }
        });
        addComponent(createDeck);

        GUIButton deleteDeck = new GUIButton(windowWidth / 2 - 170.0 / 2 + windowWidth / 3, windowHeight
                / 2 - 50.0 / 2 - 10 - 50, 170, 50);
        try {
            deleteDeck.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            deleteDeck.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            deleteDeck.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        deleteDeck.setText("Delete");
        deleteDeck.setOnMouseClicked(e -> {
            Optional<String> deckName = popUpGetText("Deck name", "Delete");
            UI.decide("delete deck " + deckName.orElse(""));
        });
        addComponent(deleteDeck);

        GUIButton addCard = new GUIButton(windowWidth / 2 - 170.0 / 2 + windowWidth / 3, windowHeight
                / 2 - 50.0 / 2, 170, 50);
        try {
            addCard.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            addCard.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            addCard.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        addCard.setText("Add");
        addComponent(addCard);

        GUIButton removeCard = new GUIButton(windowWidth / 2 - 170.0 / 2 + windowWidth / 3, windowHeight
                / 2 - 50.0 / 2 + 10 + 50, 170, 50);
        try {
            removeCard.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            removeCard.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            removeCard.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        removeCard.setText("Remove");
        addComponent(removeCard);

        GUIButton validateDeck = new GUIButton(windowWidth / 2 - 170.0 / 2 - windowWidth / 3, windowHeight
                / 2 - 50.0 / 2 - 10 - 50, 170, 50);
        try {
            validateDeck.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            validateDeck.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            validateDeck.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        validateDeck.setText("Validate");
        validateDeck.setOnMouseClicked(e -> {
            Optional<String> deckName = popUpGetText("Deck name", "Validate");
            UI.decide("validate deck " + deckName.orElse(""));
        });
        addComponent(validateDeck);

        GUIButton select = new GUIButton(windowWidth / 2 - 170.0 / 2 - windowWidth / 3, windowHeight
                / 2 - 50.0 / 2, 170, 50);
        try {
            select.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            select.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            select.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        select.setText("Select");
        select.setOnMouseClicked(e -> {
            Optional<String> deckName = popUpGetText("Deck name", "Select");
            UI.decide("select deck " + deckName.orElse(""));
        });
        addComponent(select);

        GUIButton showAllDecks = new GUIButton(windowWidth / 2 - 170.0 / 2 - windowWidth / 3, windowHeight
                / 2 - 50.0 / 2 + 10 + 50, 170, 50);
        try {
            showAllDecks.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            showAllDecks.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            showAllDecks.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        showAllDecks.setText("Show All");
        addComponent(showAllDecks);

        GUIButton showDeck = new GUIButton(windowWidth / 2 - 170.0 / 2 - windowWidth / 3, windowHeight
                / 2 - 50.0 / 2 + 2 * 10 + 2 * 50, 170, 50);
        try {
            showDeck.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            showDeck.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            showDeck.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        showDeck.setText("Show Deck");
        showDeck.setOnMouseClicked(e -> {
            Optional<String> deckName = popUpGetText("Deck name", "Show Deck");
            UI.decide("show deck " + deckName.orElse(""));
        });
        addComponent(showDeck);

        GUIButton back = new GUIButton(windowWidth / 2 - 100.0 / 2, windowHeight - 50,
                100, 50);
        try {
            back.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            back.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            back.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        back.setText("Back");
        back.setOnMouseClicked(e -> {
            UI.decide("exit");
            MenuManager.getInstance().setCurrentMenu(Id.MAIN_MENU);
        });
        addComponent(back);
    }
}
