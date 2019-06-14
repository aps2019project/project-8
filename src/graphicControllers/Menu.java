package graphicControllers;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Menu {
    private static final int POP_UP_BUTTON_HEIGHT = 40;
    public static double windowDefaultWidth = 1240;
    public static double windowDefaultHeight = 720;
    protected double windowHeight = windowDefaultHeight, windowWidth = windowDefaultWidth;
    private int id;
    private String title = "No Title!";
    private transient Menu parentMenu = null;
    private GraphicsView view = new GraphicsView(windowWidth, windowHeight, title);
    private Image backgroundImage = null;

    private ArrayList<MenuChangeComponent> menuChangeComponents = new ArrayList<>();
    private List<MenuComponent> menuComponents = new ArrayList<>();

    private Stage getText;
    private Optional<String> text = Optional.empty();
    private TextField textField;
    private Label button;

    private Stage popUp;
    private Stage getGameMode;
    private VBox popUpContent = new VBox();
    // bere tu guiButton
    private Media sound = new Media(new File("./sfx/sfx_ui_menu_hover.m4a").toURI().toString());

    private Optional[] gameMode = new Optional[]{Optional.empty(), Optional.empty()};
    private MediaPlayer backGroundMedia = new MediaPlayer(new Media(new File("music/mainmenu_v2c_looping.m4a").toURI().toString()));
    private ComboBox<String> choiceBox;
    private Stage getListItem;
    private Label getListButton;
    private Optional<String> listItem = Optional.empty();
    private ScrollPane scrollPane;
    private Label showPopUpButton;
    private ImageView popUpButtonImage;

    public void playMedia() {
        backGroundMedia.play();
        backGroundMedia.setOnEndOfMedia(() -> backGroundMedia.seek(Duration.ZERO));
    }

    public Menu() {
        this(-1);
    }

    public Menu(int id) {
        this.id = id;
        playMedia();
    }

    public Menu(int id, String title) {
        this(id);
        this.title = title;
        this.view = new GraphicsView(windowWidth, windowHeight, title);
    }

    public Menu(int id, String title, double width, double height) {
        this(id);
        this.title = title;
        this.windowWidth = width;
        this.windowHeight = height;
        this.view = new GraphicsView(width, height, title);
    }

    protected void setSound(String musicAddress) {
        backGroundMedia.stop();
        backGroundMedia = new MediaPlayer(new Media(new File(musicAddress).toURI().toString()));
        playMedia();
    }

    int getId() {
        return this.id;
    }

    protected void setBackGround(Image image) {
        getView().setBackground(image);
    }

    protected void addComponent(MenuComponent component) {
        if (component instanceof ComponentSet) {
            for (MenuComponent c : ((ComponentSet) component).getComponents()) {
                addComponent(c);
            }
            return;
        }
        view.addComponent(component);
        menuComponents.add(component);
        if (component instanceof MenuChangeComponent) {
            menuChangeComponents.add((MenuChangeComponent) component);
        }
    }

    protected void removeComponent(MenuComponent component) {
        if (component instanceof ComponentSet) {
            for (MenuComponent c : ((ComponentSet) component).getComponents()) {
                removeComponent(c);
            }
            return;
        }
        view.removeComponent(component);
        menuComponents.remove(component);
        if (component instanceof MenuChangeComponent)
            menuChangeComponents.remove(component);
    }

    protected void setKeyPressEvent(EventHandler<KeyEvent> eventHandler) {
        getView().getScene().setOnKeyPressed(eventHandler);
    }

    protected void setMouseClickEvent(EventHandler<MouseEvent> eventHandler) {
        getView().getScene().setOnMouseClicked(eventHandler);
    }

    protected void setMouseReleaseEvent(EventHandler<MouseEvent> eventHandler) {
        getView().getScene().setOnMouseReleased(eventHandler);
    }

    protected double getCursorX() {
        return getView().getScene().getX();
    }

    protected double getCursorY() {
        return getView().getScene().getY();
    }
    Menu getParentMenu() {
        return parentMenu;
    }

    ArrayList<MenuChangeComponent> getMenuChangeComponents() {
        return menuChangeComponents;
    }

    public GraphicsView getView() {
        return view;
    }

    public void stopMedia() {
        backGroundMedia.stop();
    }

    public static class Id {
        public static final int ACCOUNT_MENU = 1;
        public static final int MAIN_MENU = 434;
        public static final int COLLECTION_MENU = 898;
        public static final int SHOP_MENU = 351;
        public static final int CHOOSE_BATTLE_MENU = 428;
        public static final int START_CUSTOM_GAME_MENU = 457;
        public static final int SINGLEPLAYER_MENU = 681;
        public static final int MULTIPLAYER_MENU = 448;
        public static final int IN_GAME_MENU = 66;
        public static final int GRAVE_YARD_MENU = 23;
        public static final int LOGIN_MENU = 788;
        public static final int SIGN_IN_MENU = 768;
        public static final int LEADERBOARD = 9999;
        public static final int STORY_MODE = 8080;
    }

    public void refresh() {}

    private void setUpGetText(String prompt) {
        textField = new TextField();
        getText = new Stage();
        getText.setTitle(prompt);
        Group group = new Group();
        VBox vBox = new VBox();
        vBox.setFillWidth(true);
        vBox.setMinWidth(320);
        vBox.setMaxWidth(320);
        try {
            Image inactive = new Image(new FileInputStream("images/buttons/button_secondary@2x.png"));
            Image active = new Image(new FileInputStream("images/buttons/button_secondary_glow@2x.png"));
            ImageView imageView = new ImageView(inactive);
            group.getChildren().add(imageView);
            imageView.setFitWidth(170);
            imageView.setFitHeight(POP_UP_BUTTON_HEIGHT);
            imageView.relocate(320 / 2 - 170 / 2, 180 - POP_UP_BUTTON_HEIGHT);
            button = new Label();
            button.setOnMouseEntered(e -> {
                imageView.setImage(active);
                new MediaPlayer(sound).play();
            });
            button.setOnMouseClicked(e -> {
                if (!textField.getText().isEmpty()) {
                    text = Optional.of(textField.getText());
                }
                textField.clear();
                getText.close();
            });
            fixPopUpButton(group, inactive, imageView, button);
        } catch (FileNotFoundException ignored) {
        }
        setPopUpContent(group, vBox, getText);
        textField.setPromptText("");
        textField.setMaxWidth(320 / 2);
        textField.setMinWidth(320 / 2);
        vBox.getChildren().add(textField);
    }

    private void setUpGetGameMode() {
        ComboBox<String> choiceBox = new ComboBox<>();
        TextField textField = new TextField();
        getGameMode = new Stage();
        Group group = new Group();
        VBox getGameModeContent = new VBox();
        getGameModeContent.setFillWidth(true);
        getGameModeContent.setMinWidth(320);
        getGameModeContent.setMaxWidth(320);
        try {
            Image inactive = new Image(new FileInputStream("images/buttons/button_secondary@2x.png"));
            Image active = new Image(new FileInputStream("images/buttons/button_secondary_glow@2x.png"));
            ImageView imageView = new ImageView(inactive);
            group.getChildren().add(imageView);
            imageView.setFitWidth(90);
            imageView.setFitHeight(POP_UP_BUTTON_HEIGHT);
            imageView.relocate(320 / 2 - 90 / 2, 180 - POP_UP_BUTTON_HEIGHT);
            Label button = new Label("Play");
            button.setOnMouseEntered(e -> {
                imageView.setImage(active);
                new MediaPlayer(sound).play();
            });
            button.setOnMouseClicked(e -> {
                if (choiceBox.getSelectionModel().getSelectedIndex() == -1) {
                    showPopUp("Please select a game mode.");
                } else if (choiceBox.getSelectionModel().getSelectedIndex() == 2 && !textField.getText().matches("\\d+"))
                    showPopUp("Please enter the number of flags.");
                else {
                    gameMode[0] = Optional.of(choiceBox.getSelectionModel().getSelectedIndex() + 1);
                    if (textField.getText().isEmpty())
                        gameMode[1] = Optional.empty();
                    else
                        gameMode[1] = Optional.of(Integer.parseInt(textField.getText()));
                    getGameMode.close();
                }
            });
            fixPopUpButton(group, inactive, imageView, button);
        } catch (FileNotFoundException ignored) {
        }
        setPopUpContent(group, getGameModeContent, getGameMode);
        choiceBox.setMinWidth(320 / 2);
        choiceBox.setMaxWidth(320 / 2);
        choiceBox.setItems(FXCollections.observableArrayList("Elimination", "Hold the Flag", "Collect the Flags"));
        getGameModeContent.getChildren().add(choiceBox);
        choiceBox.setPromptText("Game Mode");
        textField.setPromptText("Number of Flags");
        textField.setMaxWidth(320 / 2);
        textField.setMinWidth(320 / 2);
        textField.setDisable(true);
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(2))
                textField.setDisable(false);
            else {
                textField.setDisable(true);
                textField.clear();
            }
        });
        getGameModeContent.getChildren().add(textField);
    }

    private void setUpGetListItem() {
        choiceBox = new ComboBox<>();
        choiceBox.setMinWidth(320 / 2);
        choiceBox.setMaxWidth(320 / 2);
        getListItem = new Stage();
        Group group = new Group();
        VBox vBox = new VBox();
        vBox.setFillWidth(true);
        vBox.setMinWidth(320);
        vBox.setMaxWidth(320);
        try {
            Image inactive = new Image(new FileInputStream("images/buttons/button_secondary@2x.png"));
            Image active = new Image(new FileInputStream("images/buttons/button_secondary_glow@2x.png"));
            ImageView imageView = new ImageView(inactive);
            group.getChildren().add(imageView);
            imageView.setFitWidth(170);
            imageView.setFitHeight(POP_UP_BUTTON_HEIGHT);
            imageView.relocate(320 / 2 - 170 / 2, 180 - POP_UP_BUTTON_HEIGHT);
            getListButton = new Label();
            getListButton.setOnMouseEntered(e -> {
                imageView.setImage(active);
                new MediaPlayer(sound).play();
            });
            getListButton.setOnMouseClicked(e -> {
                if (choiceBox.getSelectionModel().getSelectedIndex() != -1)
                    listItem = Optional.of(choiceBox.getSelectionModel().getSelectedItem());
                getListItem.close();
            });
            fixPopUpButton(group, inactive, imageView, getListButton);
        } catch (FileNotFoundException ignored) {
        }
        setPopUpContent(group, vBox, getListItem);
        vBox.getChildren().add(choiceBox);
    }

    private void setPopUpContent(Group group, VBox getGameModeContent, Stage getGameMode) {
        fixPopUpContent(group, getGameModeContent, getGameMode);
        try {
            getGameMode.getScene().setFill(new ImagePattern(new Image(new FileInputStream("images/backgrounds/color-plate-bg-orange@2x.png"))));
            getGameMode.getScene().setCursor(new ImageCursor(new Image(new FileInputStream("images/cursors/mouse_auto.png"))));
        } catch (FileNotFoundException ignored) {
        }
        getGameModeContent.getChildren().clear();
        getGameModeContent.setSpacing((180 - POP_UP_BUTTON_HEIGHT) / 4);
    }

    private void fixPopUpContent(Group group, VBox getGameModeContent, Stage getGameMode) {
        getGameMode.setScene(new Scene(group, 320, 180));
        group.getChildren().add(getGameModeContent);
        getGameMode.setResizable(false);
        getGameModeContent.setAlignment(Pos.CENTER);
        getGameModeContent.getStylesheets().add("css/vBox.css");
        getGameModeContent.setMinHeight(180 - POP_UP_BUTTON_HEIGHT);
        getGameModeContent.setMaxHeight(180 - POP_UP_BUTTON_HEIGHT);
        getGameMode.initModality(Modality.APPLICATION_MODAL);
        getGameMode.setAlwaysOnTop(true);
    }

    private void fixPopUpButton(Group group, Image inactive, ImageView imageView, Label button) throws FileNotFoundException {
        button.setOnMouseExited(e -> imageView.setImage(inactive));
        button.setFont(Font.loadFont(new FileInputStream("./fonts/averta-extrathin-webfont.ttf"), 17));
        button.setTextFill(Color.CORAL);
        button.setMinSize(90, POP_UP_BUTTON_HEIGHT);
        button.setMaxSize(90, POP_UP_BUTTON_HEIGHT);
        button.setAlignment(Pos.CENTER);
        button.setTextAlignment(TextAlignment.CENTER);
        button.relocate(320 / 2 - 90 / 2, 180 - POP_UP_BUTTON_HEIGHT);
        group.getChildren().add(button);
    }

    private void setUpPopUp() {
        popUp = new Stage();
        Group group = new Group();
        popUpContent.setFillWidth(true);
        popUpContent.setMinWidth(320);
        popUpContent.setMaxWidth(320);
        try {
            Image inactive = new Image(new FileInputStream("images/buttons/button_secondary@2x.png"));
            Image active = new Image(new FileInputStream("images/buttons/button_secondary_glow@2x.png"));
            popUpButtonImage = new ImageView(inactive);
            group.getChildren().add(popUpButtonImage);
            popUpButtonImage.setFitWidth(90);
            popUpButtonImage.setFitHeight(POP_UP_BUTTON_HEIGHT);
            popUpButtonImage.relocate(popUpContent.getMinWidth() / 2 - 90 / 2, 180 - POP_UP_BUTTON_HEIGHT);
            showPopUpButton = new Label("Okay");
            showPopUpButton.setMinHeight(POP_UP_BUTTON_HEIGHT);
            showPopUpButton.setMaxHeight(POP_UP_BUTTON_HEIGHT);
            showPopUpButton.setOnMouseEntered(e -> {
                popUpButtonImage.setImage(active);
                new MediaPlayer(sound).play();
            });
            showPopUpButton.setOnMouseClicked(e -> popUp.close());
            fixPopUpButton(group, inactive, popUpButtonImage, showPopUpButton);
        } catch (FileNotFoundException ignored) {
        }
        popUp.setScene(new Scene(group, 320, 180));
        scrollPane = new ScrollPane(popUpContent);
        scrollPane.setMinWidth(320);
        scrollPane.setMaxWidth(320);
        scrollPane.setMaxHeight(180 - POP_UP_BUTTON_HEIGHT);
        scrollPane.setFitToWidth(true);
        popUpContent.setFillWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHmax(0);
        scrollPane.getStylesheets().add("css/scrollPane.css");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        group.getChildren().add(scrollPane);
        popUp.setResizable(false);
        popUpContent.setAlignment(Pos.CENTER);
        popUpContent.getStylesheets().add("css/vBox.css");
        popUpContent.setMinHeight(180 - POP_UP_BUTTON_HEIGHT - 2);
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setAlwaysOnTop(true);
        try {
            popUp.getScene().setFill(new ImagePattern(new Image(new FileInputStream("images/backgrounds/color-plate-bg-purple@2x.png"))));
            popUp.getScene().setCursor(new ImageCursor(new Image(new FileInputStream("images/cursors/mouse_auto.png"))));
        } catch (FileNotFoundException ignored) {
        }
    }

    protected void showPopUp(String text) {
        setUpPopUp();
        popUpContent.getChildren().clear();
        Label label = new Label(text);
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        try {
            label.setFont(Font.loadFont(new FileInputStream("./fonts/averta-black-webfont.ttf"), 17));
        } catch (FileNotFoundException ignored) {
        }
        label.setTextFill(Color.CORAL);
        label.setMinHeight(180 - POP_UP_BUTTON_HEIGHT - 2);
        popUpContent.getChildren().add(label);
        popUp.showAndWait();
    }

    protected void showWidePopUp(String text) {
        setUpPopUp();
        popUpContent.getChildren().clear();
        Label label = new Label(text);
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        try {
            label.setFont(Font.loadFont(new FileInputStream("./fonts/averta-black-webfont.ttf"), 17));
        } catch (FileNotFoundException ignored) {
        }
        label.setTextFill(Color.CORAL);
        label.setMinHeight(180 - POP_UP_BUTTON_HEIGHT - 2);
        popUpContent.getChildren().add(label);
        scrollPane.setMinWidth(1240);
        Scene scene = new Scene(new Group(popUp.getScene().getRoot()), 1240, 720);
        popUp.setScene(scene);
        scrollPane.setMaxWidth(1230);
        popUpContent.setMinWidth(1230);
        popUpContent.setMaxWidth(1230);
        scrollPane.setMinHeight(720 - POP_UP_BUTTON_HEIGHT);
        scrollPane.setMaxHeight(720 - POP_UP_BUTTON_HEIGHT);
        popUpContent.setMinHeight(720 - POP_UP_BUTTON_HEIGHT - 2);
        showPopUpButton.relocate(1240 / 2 - 90 / 2, 720 - POP_UP_BUTTON_HEIGHT);
        popUpButtonImage.relocate(1240 / 2 - 90 / 2, 720 - POP_UP_BUTTON_HEIGHT);
        try {
            popUp.getScene().setFill(new ImagePattern(new Image(new FileInputStream("images/backgrounds/color-plate-bg-purple@2x.png"))));
            popUp.getScene().setCursor(new ImageCursor(new Image(new FileInputStream("images/cursors/mouse_auto.png"))));
        } catch (FileNotFoundException ignored) {
        }
        popUp.showAndWait();
    }

    protected Optional<Integer>[] popUpGetGameType() {
        setUpGetGameMode();
        gameMode = new Optional[]{Optional.empty(), Optional.empty()};
        getGameMode.showAndWait();
        return gameMode;
    }

    protected Optional<String> popUpGetText(String prompt, String buttonText) {
        setUpGetText(prompt);
        text = Optional.empty();
        textField.setPromptText(prompt);
        button.setText(buttonText);
        getText.showAndWait();
        return text;
    }

    protected Optional<String> popUpGetList(List list, String buttonText, String prompt) {
        setUpGetListItem();
        listItem = Optional.empty();
        getListButton.setText(buttonText);
        choiceBox.setPromptText(prompt);
        choiceBox.setItems(FXCollections.observableArrayList(list));
        getListItem.showAndWait();
        return listItem;
    }
}
