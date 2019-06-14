package graphicControllers.menus;

import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import menus.Shop;
import menus.UI;
import model.Card;
import model.CollectionItem;
import model.Hero;
import model.Item;
import view.GUIButton;
import view.NodeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShopMenu extends Menu {
    private static final String DASH = " - ";

    public ShopMenu() {
        super(Id.SHOP_MENU, "Shop Menu", 1240, 720);
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

            GUIButton showCollection = new GUIButton(windowWidth / 3 - 180.0 / 2, windowHeight
                    / 2 - 50.0 / 2 - 10 - 50, 180, 50);
            try {
                showCollection.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                showCollection.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                showCollection.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            showCollection.setText("Show Collection");
            showCollection.setOnMouseClicked(e -> {
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
                UI.decide("show collection");
            });
            addComponent(showCollection);

            GUIButton search = new GUIButton(windowWidth / 3 - 180.0 / 2, windowHeight
                    / 2 - 50.0 / 2, 180, 50);
            try {
                search.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                search.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                search.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            search.setText("Search");
            search.setOnMouseClicked(e -> {
                popUpGetList(Shop.getCollectionItems().stream().map(CollectionItem::getName).sorted().collect(Collectors.toList()), "Search", "Item name | Card name").ifPresent(s -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(s + "\n");
                    stringBuilder.append(Shop.getCollectionItemByName(s).showInfo());
                    showWidePopUp(stringBuilder.toString());
                    UI.decide("search " + s);
                });
            });
            addComponent(search);

            GUIButton searchCollection = new GUIButton(windowWidth / 3 - 180.0 / 2, windowHeight
                    / 2 - 50.0 / 2 + 10 + 50, 180, 50);
            try {
                searchCollection.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                searchCollection.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                searchCollection.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            searchCollection.setText("Search Collection");
            searchCollection.setOnMouseClicked(e -> {
                Optional<String> name = popUpGetList(UI.getAccount().getCollectionItems().values().stream().map(CollectionItem::getName).distinct().sorted().collect(Collectors.toList()), "Search", "Card name | item name");
                if (name.isPresent()) {
                    UI.decide("search collection " + name.get());
                    StringBuilder stringBuilder = new StringBuilder();
                    UI.getAccount().getCollection().getCollectionItemIDs(name.get()).forEach(o -> stringBuilder.append(o + "\n"));
                    showPopUp(stringBuilder.toString());
                }
            });
            addComponent(searchCollection);

            GUIButton buy = new GUIButton(2 * windowWidth / 3 - 180.0 / 2, windowHeight
                    / 2 - 50.0 / 2 - 10 - 50, 180, 50);
            try {
                buy.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                buy.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                buy.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            buy.setText("Buy");
            buy.setOnMouseClicked(e -> {
                popUpGetList(Shop.getCollectionItems().stream().map(CollectionItem::getName).distinct().sorted().collect(Collectors.toList()), "Buy", "Card name | item name").ifPresent(s -> {
                    if (UI.getAccount().getMoney() < Shop.getPrice(s)) {
                        showPopUp("Not enough money.");
                        return;
                    }
                    if (UI.getAccount().hasThreeItems() && Shop.isItem(s)) {
                        showPopUp("Cannot have 4 items in collection.");
                        return;
                    }
                    CollectionItem collectionItem = Shop.getCollectionItemByName(s);
                    UI.getAccount().payMoney(collectionItem.getPrice());
                    UI.getAccount().getCollection().addCollectionItem(Shop.getCopy(collectionItem));
                    showPopUp("Successfully bought the collection item.");
                    UI.decide("buy " + s);
                });
            });
            addComponent(buy);

            GUIButton sell = new GUIButton(2 * windowWidth / 3 - 180.0 / 2, windowHeight
                    / 2 - 50.0 / 2, 180, 50);
            try {
                sell.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                sell.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                sell.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            sell.setText("Sell");
            sell.setOnMouseClicked(e -> popUpGetList(UI.getAccount().getCollectionItems().keySet().stream().mapToInt(Integer::parseInt).sorted().mapToObj(String::valueOf).collect(Collectors.toList()), "Sell", "Card ID").ifPresent(s -> {
                CollectionItem collectionItem = UI.getAccount().getCollection().getCollectionItemByID(s);
                UI.getAccount().receiveMoney(collectionItem.getPrice());
                UI.getAccount().getCollection().removeCollectionItem(s);
                UI.decide("sell " + s);
                showPopUp("Successfully sold the collection item.");
            }));
            addComponent(sell);

            GUIButton show = new GUIButton(2 * windowWidth / 3 - 180.0 / 2, windowHeight
                    / 2 - 50.0 / 2 + 10 + 50, 180, 50);
            try {
                show.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
                show.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
                show.setSound(new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString()));
            } catch (FileNotFoundException ignored) {
            }
            show.setText("Show");
            show.setOnMouseClicked(e -> {
                UI.decide("show");
                StringBuilder stringBuilder = new StringBuilder();
                showCollectionItemsWithPrice(Shop.getCollectionItems(), "Buy", stringBuilder);
                showWidePopUp(stringBuilder.toString());
            });
            addComponent(show);

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
        }).start();
    }

    private int showIndexedCollectionItemWithPrice(int index, CollectionItem collectionItem, String tradeKind, StringBuilder stringBuilder) {
        stringBuilder.append(index++ + " : " + collectionItem + DASH + tradeKind + " Cost : " + collectionItem.getPrice() + "\n");
        return index;
    }

    private void showCollectionItemsWithPrice(ArrayList<CollectionItem> collectionItems, String tradeKind, StringBuilder stringBuilder) {
        stringBuilder.append("Heroes :\n");
        int index = 1;
        for (CollectionItem collectionItem : collectionItems) {
            if (collectionItem instanceof Hero) {
                index = showIndexedCollectionItemWithPrice(index, collectionItem, tradeKind, stringBuilder);
            }
        }
        stringBuilder.append("Items :\n");
        index = 1;
        for (CollectionItem collectionItem : collectionItems) {
            if (collectionItem instanceof Item)
                index = showIndexedCollectionItemWithPrice(index, collectionItem, tradeKind, stringBuilder);
        }
        stringBuilder.append("Cards :\n");
        index = 1;
        for (CollectionItem collectionItem : collectionItems) {
            if (collectionItem instanceof Card && !(collectionItem instanceof Hero))
                index = showIndexedCollectionItemWithPrice(index, collectionItem, tradeKind, stringBuilder);
        }
    }
}
