package graphicControllers.menus;

import gen.JsonMaker;
import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import menus.Shop;
import menus.UI;
import model.SpecialPowerType;
import model.Spell;
import model.UnitType;
import view.GUIButton;
import view.NodeWrapper;

import java.io.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainMenu extends Menu {

    private static final String MINION = "Minion";

    public MainMenu() {
        super(Id.MAIN_MENU, "Main Menu", windowDefaultWidth, windowDefaultHeight);
        new Thread(() -> {
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
                    StringBuilder stringBuilder = new StringBuilder(name).append("\n");
                    stringBuilder.append(popUpGetText("Description", "Next").orElse("none")).append("\n");
                    stringBuilder.append(name).append("\n");
                    Optional<Integer> price = getNumber("Price");
                    if (!price.isPresent()) return;
                    stringBuilder.append(price.get()).append("\n").append("Card").append("\n");
                    Optional<String> type = popUpGetList(Arrays.asList(MINION, "Spell", "Hero"), "Next", "Type");
                    if (!type.isPresent())
                        return;
                    if (!type.get().equals("Spell")) {
                        if (type.get().equals(MINION)) {
                            Optional<Integer> mana = getNumber("Mana");
                            if (!mana.isPresent()) return;
                            stringBuilder.append(mana.get()).append("\n");
                        }
                        StringBuilder unit = new StringBuilder();
                        unit.append("Unit").append("\n");
                        Optional<Integer> hp = getNumber("HP");
                        if (!hp.isPresent()) return;
                        unit.append(hp.get()).append("\n");
                        Optional<Integer> ap = getNumber("AP");
                        if (!ap.isPresent()) return;
                        unit.append(ap.get()).append("\n");
                        Optional<String> attackType = popUpGetList(Arrays.stream(UnitType.values()).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()), "Next", "Attack type");
                        if (!attackType.isPresent())
                            return;
                        int attackTypeIndex = Arrays.stream(UnitType.values()).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()).indexOf(attackType.get()) + 1;
                        unit.append(attackTypeIndex).append("\n");
                        Optional<String> sp = popUpGetList(Arrays.asList("Yes", "No"), "Next", "Special Power");
                        if (!sp.isPresent())
                            return;
                        unit.append(sp.get().toLowerCase()).append("\n");
                        Optional<Integer> coolDown = Optional.empty();
                        if (sp.get().equals("Yes")) {
                            int spActivationIndex;
                            if (type.get().equals(MINION)) {
                                Optional<String> spActivation = popUpGetList(Arrays.stream(Arrays.copyOfRange(SpecialPowerType.values(), 0, SpecialPowerType.values().length - 1)).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()), "Next", "Special power activation");
                                if (!spActivation.isPresent())
                                    return;
                                spActivationIndex = Arrays.stream(Arrays.copyOfRange(SpecialPowerType.values(), 0, SpecialPowerType.values().length - 1)).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()).indexOf(spActivation.get()) + 1;
                                unit.append(spActivationIndex).append("\n");
                            } else {
                                Optional<Integer> mana = getNumber("Mana");
                                if (!mana.isPresent()) return;
                                stringBuilder.append(mana.get()).append("\n");
                                Optional<String> spActivation = popUpGetList(Arrays.stream(SpecialPowerType.values()).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()), "Next", "Special power activation");
                                if (!spActivation.isPresent())
                                    return;
                                spActivationIndex = Arrays.stream(SpecialPowerType.values()).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()).indexOf(spActivation.get()) + 1;
                                unit.append(spActivationIndex).append("\n");
                            }
                            getSpell(unit);
                            if (type.get().equals("Hero") && spActivationIndex == 8) {
                                coolDown = getNumber("Cooldown");
                                if (!coolDown.isPresent()) return;
                            }
                        } else if (type.get().equals("Hero")) {
                            stringBuilder.append(0).append("\n");
                        }
                        Optional<String> canFly = popUpGetList(Arrays.asList("Yes", "No"), "Next", "Can fly");
                        if (!canFly.isPresent())
                            return;
                        unit.append(canFly.get().toLowerCase()).append("\n");
                        Optional<Integer> range = getNumber("Range");
                        if (!range.isPresent()) return;
                        unit.append(range.get()).append("\n");
                        unit.append(type.get()).append("\n");
                        if (sp.get().equals("No") && !type.get().equals(MINION)) {
                            unit.append(0).append("\n");
                        } else if (sp.get().equals("Yes") && !type.get().equals(MINION)) {
                            unit.append(coolDown.orElse(0)).append("\n");
                        }
                        stringBuilder.append(unit);
                    } else {
                        Optional<Integer> mana = getNumber("Mana");
                        if (!mana.isPresent()) return;
                        stringBuilder.append(mana.get()).append("\n").append("SpellCard").append("\n");
                        getSpell(stringBuilder);
                    }
                    try {
                        FileWriter fileWriter = new FileWriter(new File("gameData/ManualFeatureInputLogs/" + name + ".txt"), false);
                        fileWriter.append(stringBuilder);
                        fileWriter.flush();
                        fileWriter.close();
                        new Thread(() -> {
                            JsonMaker.main(new String[]{"java", "JsonMaker"});
                            Shop.load();
                            UI.getAccount().getCollection().addCollectionItem(Shop.getCollectionItemByName(name));
                        }).start();
                    } catch (IOException ignored) {
                    }
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
        }).start();
    }

    private Optional<Integer> getNumber(String prompt) {
        Optional<String> numberText = popUpGetText(prompt, "Next");
        if (!numberText.isPresent())
            return Optional.empty();
        if (!numberText.get().matches("[-]?\\d+")) {
            showPopUp("Please enter a number.");
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(numberText.get()));
    }

    private void getSpell(StringBuilder stringBuilder) {
        Optional<String> targetType = popUpGetList(Arrays.stream(Spell.TargetType.values()).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()), "Next", "Target type");
        if (!targetType.isPresent())
            return;
        int targetTypeIndex = Arrays.stream(Spell.TargetType.values()).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()).indexOf(targetType.get()) + 1;
        stringBuilder.append(targetTypeIndex).append("\n");
        if (targetTypeIndex == 2) {
            Optional<String> targetUnit = popUpGetList(Arrays.stream(Arrays.copyOfRange(Spell.TargetUnit.values(), 0, Spell.TargetUnit.values().length - 1)).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()), "Next", "Target type");
            if (!targetUnit.isPresent())
                return;
            int targetUnitIndex = Arrays.stream(Spell.TargetType.values()).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()).indexOf(targetType.get()) + 1;
            stringBuilder.append(targetUnitIndex).append("\n");
            Optional<String> targetUnitType = popUpGetList(Arrays.stream(Spell.TargetUnitType.values()).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()), "Next", "Target unit type");
            if (!targetUnitType.isPresent())
                return;
            int targetUnitTypeIndex = Arrays.stream(Spell.TargetUnitType.values()).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()).indexOf(targetUnitType.get()) + 1;
            stringBuilder.append(targetUnitTypeIndex).append("\n");
        }
        Optional<String> targetArea = popUpGetList(Arrays.stream(Spell.TargetArea.values()).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()), "Next", "Target area");
        if (!targetArea.isPresent())
            return;
        int targetAreaIndex = Arrays.stream(Spell.TargetArea.values()).map(Enum::toString).map(this::fixCase).collect(Collectors.toList()).indexOf(targetArea.get()) + 1;
        stringBuilder.append(targetAreaIndex).append("\n");
        if (targetAreaIndex == 2) {
            Optional<Integer> numberOfRows = getNumber("Number of rows");
            if (!numberOfRows.isPresent())
                return;
            stringBuilder.append(numberOfRows.get()).append("\n");
            Optional<Integer> numberOfColumns = getNumber("Number of columns");
            if (!numberOfColumns.isPresent())
                return;
            stringBuilder.append(numberOfColumns.get()).append("\n");
        }
        Optional<String> random = popUpGetList(Arrays.asList("Yes", "No"), "Next", "Random spell");
        if (!random.isPresent())
            return;
        stringBuilder.append(random.get().toLowerCase()).append("\n");
        if (random.get().equals("Yes")) {
            Optional<Integer> randomNum = getNumber("Number of random picks");
            if (!randomNum.isPresent()) return;
            stringBuilder.append(randomNum.get()).append("\n");
        }
        Optional<Integer> buffNumber = getNumber("Number of buffs");
        if (!buffNumber.isPresent()) return;
        stringBuilder.append(buffNumber.get()).append("\n");
        for (int i = 0; i < buffNumber.get(); i++)
            getBuff(stringBuilder);
        Optional<String> dispel = popUpGetList(Arrays.asList("Yes", "No"), "Next", "Dispel");
        if (!dispel.isPresent())
            return;
        stringBuilder.append(dispel.get().toLowerCase()).append("\n");
        stringBuilder.append("none").append("\n");
    }

    private void getBuff(StringBuilder stringBuilder) {
        popUpGetText("Buff name", "Next");
        int duration = getNumber("Duration").orElse(0);
        int delay = getNumber("Delay").orElse(0);
        int holy = getNumber("Holy (Unholy)").orElse(0);
        int poison = getNumber("Poison").orElse(0);
        int hp = getNumber("HP").orElse(0);
        int ap = getNumber("AP").orElse(0);
        String stun = popUpGetList(Arrays.asList("Yes", "No"), "Next", "Stun").orElse("No").toLowerCase();
        String disarm = popUpGetList(Arrays.asList("Yes", "No"), "Next", "Disarm").orElse("No").toLowerCase();
        String dispellable = popUpGetList(Arrays.asList("Yes", "No"), "Next", "Dispellable").orElse("Yes").toLowerCase();
        String allegiance = popUpGetList(Arrays.asList("Friendly", "Enemy", "Neither"), "Next", "Allegiance").orElse("Neither").toLowerCase();
        stringBuilder.append(duration).append("\n").append(holy).append("\n").append(poison).append("\n").append(hp).append("\n").append(ap).append("\n").append(stun).append("\n").append(disarm).append("\n").append(dispellable).append("\n").append(allegiance).append("\n").append(delay).append("\n").append("none\n");
    }

    private String fixCase(String enumConst) {
        enumConst = enumConst.replaceAll("_", " ").toLowerCase();
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : enumConst.split(" "))
            stringBuilder.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
        return stringBuilder.toString();
    }
}
