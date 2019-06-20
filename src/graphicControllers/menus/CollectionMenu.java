package graphicControllers.menus;

import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import menus.UI;
import model.*;
import view.GUIButton;
import view.NodeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class CollectionMenu extends Menu {
    private static final String DASH = " - ";
    private GUIButton show;
    private GUIButton search;
    private GUIButton importButton;
    private GUIButton save;
    private GUIButton exportButton;
    private GUIButton createDeck;
    private GUIButton deleteDeck;
    private GUIButton addCard;
    private GUIButton removeCard;
    private GUIButton validateDeck;
    private GUIButton select;
    private GUIButton showAllDecks;
    private GUIButton showDeck;

    public CollectionMenu() {
        super(Id.COLLECTION_MENU, "Collection Menu", windowDefaultWidth, windowDefaultHeight);
        new Thread(() -> {
            try {
                ImageView imageView = new ImageView(new Image(new FileInputStream("images/logos/brand_duelyst@2x.png")));
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(windowWidth / 3);
                imageView.relocate(windowWidth * 2 / 3 - 10, 10);
                addComponent(new NodeWrapper(imageView));
            } catch (FileNotFoundException ignored) {
            }

            try {
                getView().setBackground(new Image(new FileInputStream("images/backgrounds/background@2x.jpg")));
                getView().setCursor(new Image(new FileInputStream("./images/cursors/mouse_auto.png")));
            } catch (FileNotFoundException ignored) {
            }

            show = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                    / 2 - 50.0 / 2 - 2 * 10 - 2 * 50, 170, 50);
            try {
                show.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                show.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                show.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            show.setText("Show");
            addComponent(show);

            search = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                    / 2 - 50.0 / 2 - 10 - 50, 170, 50);
            try {
                search.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                search.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                search.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            search.setText("Search");
            addComponent(search);

            save = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                    / 2 - 50.0 / 2, 170, 50);
            try {
                save.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                save.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                save.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            save.setText("Save");
            addComponent(save);

            importButton = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                    / 2 - 50.0 / 2 + 10 + 50, 170, 50);
            try {
                importButton.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                importButton.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                importButton.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            importButton.setText("Import");
            addComponent(importButton);

            exportButton = new GUIButton(windowWidth / 2 - 170.0 / 2, windowHeight
                    / 2 - 50.0 / 2 + 2 * 10 + 2 * 50, 170, 50);
            try {
                exportButton.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                exportButton.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                exportButton.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            exportButton.setText("Export");
            addComponent(exportButton);

            createDeck = new GUIButton(windowWidth / 2 - 170.0 / 2 + windowWidth / 3, windowHeight
                    / 2 - 50.0 / 2 - 2 * 10 - 2 * 50, 170, 50);
            try {
                createDeck.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                createDeck.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                createDeck.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            createDeck.setText("Create");
            addComponent(createDeck);

            deleteDeck = new GUIButton(windowWidth / 2 - 170.0 / 2 + windowWidth / 3, windowHeight
                    / 2 - 50.0 / 2 - 10 - 50, 170, 50);
            try {
                deleteDeck.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                deleteDeck.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                deleteDeck.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            deleteDeck.setText("Delete");
            addComponent(deleteDeck);

            addCard = new GUIButton(windowWidth / 2 - 170.0 / 2 + windowWidth / 3, windowHeight
                    / 2 - 50.0 / 2, 170, 50);
            try {
                addCard.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                addCard.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                addCard.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            addCard.setText("Add");
            addComponent(addCard);

            removeCard = new GUIButton(windowWidth / 2 - 170.0 / 2 + windowWidth / 3, windowHeight
                    / 2 - 50.0 / 2 + 10 + 50, 170, 50);
            try {
                removeCard.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                removeCard.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                removeCard.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            removeCard.setText("Remove");
            addComponent(removeCard);

            validateDeck = new GUIButton(windowWidth / 2 - 170.0 / 2 - windowWidth / 3, windowHeight
                    / 2 - 50.0 / 2 - 10 - 50, 170, 50);
            try {
                validateDeck.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                validateDeck.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                validateDeck.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            validateDeck.setText("Validate");
            addComponent(validateDeck);

            select = new GUIButton(windowWidth / 2 - 170.0 / 2 - windowWidth / 3, windowHeight
                    / 2 - 50.0 / 2, 170, 50);
            try {
                select.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                select.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                select.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            select.setText("Select");
            addComponent(select);

            showAllDecks = new GUIButton(windowWidth / 2 - 170.0 / 2 - windowWidth / 3, windowHeight
                    / 2 - 50.0 / 2 + 10 + 50, 170, 50);
            try {
                showAllDecks.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                showAllDecks.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                showAllDecks.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            showAllDecks.setText("Show All");
            addComponent(showAllDecks);

            showDeck = new GUIButton(windowWidth / 2 - 170.0 / 2 - windowWidth / 3, windowHeight
                    / 2 - 50.0 / 2 + 2 * 10 + 2 * 50, 170, 50);
            try {
                showDeck.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                showDeck.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                showDeck.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            showDeck.setText("Show Deck");
            addComponent(showDeck);

            GUIButton back = new GUIButton(windowWidth / 2 - 100.0 / 2, windowHeight - 50,
                    100, 50);
            try {
                back.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                back.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                back.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            back.setText("Back");
            back.setOnMouseClicked(e -> {
                UI.decide("exit");
                MenuManager.getInstance().setCurrentMenu(Id.MAIN_MENU);
            });
            addComponent(back);
        }).start();
    }

    private int showIndexedCollectionItemWithPrice(int index, CollectionItem collectionItem, String tradeKind, StringBuilder stringBuilder) {
        stringBuilder.append(index++ + " : " + collectionItem + DASH + tradeKind + " Cost : " + collectionItem.getPrice() + "\n");
        return index;
    }

    @Override
    public void refresh() {
        new Thread(() -> {
            show.setOnMouseClicked(e -> {
                UI.decide("show");
                Collection<CollectionItem> collectionItems = UI.getAccount().getCollectionItems().values();
                if (collectionItems.isEmpty()) {
                    showPopUp("Your collection is empty!");
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Heroes :\n");
                int index = 1;
                for (CollectionItem collectionItem : collectionItems) {
                    if (collectionItem instanceof Hero) {
                        index = showIndexedCollectionItemWithPrice(index, collectionItem, "Sell", stringBuilder);
                    }
                }
                stringBuilder.append("Items :\n");
                index = 1;
                for (CollectionItem collectionItem : collectionItems) {
                    if (collectionItem instanceof Item)
                        index = showIndexedCollectionItemWithPrice(index, collectionItem, "Sell", stringBuilder);
                }
                stringBuilder.append("Cards :\n");
                index = 1;
                for (CollectionItem collectionItem : collectionItems) {
                    if (collectionItem instanceof Card && !(collectionItem instanceof Hero))
                        index = showIndexedCollectionItemWithPrice(index, collectionItem, "Sell", stringBuilder);
                }
                showWidePopUp(stringBuilder.toString());
            });

            search.setOnMouseClicked(e -> {
                Optional<String> name = popUpGetList(UI.getAccount().getCollectionItems().values().stream().map(CollectionItem::getName).distinct().sorted().collect(Collectors.toList()), "Search", "Card name | item name");
                if (name.isPresent()) {
                    UI.decide("search " + name.get());
                    StringBuilder stringBuilder = new StringBuilder();
                    UI.getAccount().getCollection().getCollectionItemIDs(name.get()).forEach(o -> stringBuilder.append(o + "\n"));
                    showPopUp(stringBuilder.toString());
                }
            });

            importButton.setOnMouseClicked(e -> {
                ArrayList<Object> fileNames = new ArrayList<>();
                for (File file : new File("./export").listFiles())
                    fileNames.add(file.getName().split("\\.")[0]);
                Optional<String> getList = popUpGetList(fileNames, "Import", "Deck name");
                getList.ifPresent(s -> {
                    UI.decide("import " + s);
                    showPopUp("Deck successfully imported.");
                });
            });

            save.setOnMouseClicked(e -> {
                showPopUp("Successfully saved account.");
                UI.decide("save");
            });

            exportButton.setOnMouseClicked(e -> {
                if (UI.getAccount().getMainDeck() == null) {
                    showPopUp("You have no main deck.");
                } else if (!UI.getAccount().getMainDeck().isValid()) {
                    showPopUp("Your main deck is not valid");
                } else {
                    ArrayList<Object> fileNames = new ArrayList<>();
                    for (File file : new File("./export").listFiles())
                        fileNames.add(file.getName().split("\\.")[0]);
                    UI.decide("export");
                    for (File file : new File("./export").listFiles())
                        if (!fileNames.contains(file.getName().split("\\.")[0]))
                            showPopUp("Deck successfully exported to " + file.getName().split("\\.")[0]);
                }
            });

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

            deleteDeck.setOnMouseClicked(e -> {
                Optional<String> getList = popUpGetList(UI.getAccount().getDecks().stream().map(Deck::getDeckName).sorted().collect(Collectors.toList()), "Delete", "Deck name");
                getList.ifPresent(s -> {
                    UI.decide("delete deck " + getList.get());
                    showPopUp("Deck successfully deleted.");
                });
            });

            addCard.setOnMouseClicked(e -> {
                popUpGetList(UI.getAccount().getCollection().getCollectionItems().keySet().stream().mapToInt(Integer::parseInt).sorted().mapToObj(String::valueOf).collect(Collectors.toList()), "Select Card", "Card ID | Hero ID").ifPresent(s ->
                        popUpGetList(UI.getAccount().getDecks().stream().filter(d -> {
                            CollectionItem collectionItem = UI.getAccount().getCollectionItems().get(s);
                            if (d.hasCollectionItem(s))
                                return false;
                            if (collectionItem instanceof Hero)
                                return !d.hasHero();
                            if (collectionItem instanceof Usable)
                                return !d.hasItem();
                            return !d.isFull();
                        }).map(Deck::getDeckName).sorted().collect(Collectors.toList()), "Select Deck", "Deck name").ifPresent(d -> {
                            UI.decide("add " + s + " to deck " + d);
                            showPopUp("Collection item successfully added to deck");
                        }));
            });

            removeCard.setOnMouseClicked(e -> {
                popUpGetList(UI.getAccount().getCollection().getCollectionItems().keySet().stream().mapToInt(Integer::parseInt).sorted().mapToObj(String::valueOf).collect(Collectors.toList()), "Select Card", "Card ID | Hero ID").ifPresent(s -> {
                    popUpGetList(UI.getAccount().getDecks().stream().filter(d -> d.hasCollectionItem(s)).map(Deck::getDeckName).sorted().collect(Collectors.toList()), "Select Deck", "Deck name").ifPresent(d -> {
                        UI.decide("remove " + s + " from deck " + d);
                        showPopUp("Collection item successfully removed to deck");
                    });
                });
            });

            validateDeck.setOnMouseClicked(e -> {
                popUpGetList(UI.getAccount().getDecks().stream().map(Deck::getDeckName).collect(Collectors.toList()), "Validate", "Deck name").ifPresent(s -> {
                    if (UI.getAccount().getDeck(s).isValid())
                        showPopUp("The deck is valid.");
                    else
                        showPopUp("The deck is invalid.");
                    UI.decide("validate deck " + s);
                });
            });

            select.setOnMouseClicked(e -> popUpGetList(UI.getAccount().getDecks().stream().map(Deck::getDeckName).collect(Collectors.toList()), "Select", "Deck name").ifPresent(s -> {
                if (UI.getAccount().getDeck(s).isValid()) {
                    showPopUp("Main deck successfully changed.");
                } else
                    showPopUp("The deck is invalid.");
                UI.decide("select deck " + s);
            }));

            showAllDecks.setOnMouseClicked(e -> {
                ArrayList<Deck> decks = UI.getAccount().getDecks();
                if (decks.isEmpty()) {
                    showPopUp("No deck.");
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < decks.size(); i++) {
                    Deck deck = decks.get(i);
                    stringBuilder.append((i + 1) + " : " + deck.getDeckName() + "\n");
                    stringBuilder.append(deck + "\n");
                }
                showWidePopUp(stringBuilder.toString());
                UI.decide("show all decks");
            });

            showDeck.setOnMouseClicked(e -> {
                popUpGetList(UI.getAccount().getDecks().stream().map(Deck::getDeckName).collect(Collectors.toList()), "Select", "Deck name").ifPresent(s -> {
                    showWidePopUp(UI.getAccount().getDeck(s).toString());
                    UI.decide("show deck " + s);
                });
            });
        }).start();
    }
}
