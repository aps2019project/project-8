package graphicControllers;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.MenuChangeComponent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MenuManager {

    private static final int BUTTON_HEIGHT = 40;
    private static MenuManager instance = null;
    /**
     * Stage should be removed!
     */
    private Stage stage;
    private Stage popUp;
    private Stage getGameMode;
    private Stage getText;
    private Optional<String> text = Optional.empty();
    private TextField textField;
    private Label button;
    private VBox popUpContent = new VBox();
    private Media sound = new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString());
    private Menu currentMenu;
    private Map<Integer, Menu> menusIDs = new HashMap<>();

    private Optional[] gameMode = new Optional[]{Optional.empty(), Optional.empty()};

    public static MenuManager getInstance() {
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void listenForMenuChange() {
        stage.setScene(currentMenu.getView().getScene());
        for (MenuChangeComponent changeComponent : currentMenu.getMenuChangeComponents()) {
            changeComponent.setOnAction(event -> {
                try {
                    int goalMenuID = changeComponent.getGoalMenuID();
                    boolean success = false;
                    for (Map.Entry<Integer, Menu> entry : menusIDs.entrySet()) {
                        if (entry.getKey().equals(goalMenuID)) {
                            System.err.println(entry.getValue());
                            setCurrentMenu(entry.getValue());
                            success = true;
                            break;
                        }
                    }
                    if (!success) {
                        throw new MenuDoesNotExistException();
                    }
                    listenForMenuChange();
                } catch (MenuDoesNotExistException e1) {
                    e1.printStackTrace();
                }
            });
        }
    }

    public void addMenu(Menu menu) throws MenuAlreadyCreatedException {
        int id = menu.getId();
        if (menusIDs.containsKey(id))
            throw new MenuAlreadyCreatedException();
        menusIDs.put(menu.getId(), menu);
    }

    public void back() {
        setCurrentMenu(this.currentMenu.getParentMenu());
    }


    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        currentMenu.refresh();
        this.currentMenu = currentMenu;
    }

    public void setCurrentMenu(int menuID) {
        currentMenu = menusIDs.get(menuID);
        currentMenu.refresh();
        listenForMenuChange();
    }

    public void startProcessOnStage() {
        instance = this;
        setUpPopUp();
        setUpGetGameMode();
        setUpGetText();
        listenForMenuChange();
        stage.setResizable(false);
        stage.show();
    }

    private void setUpGetText() {
        textField = new TextField();
        getText = new Stage();
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
            imageView.setFitHeight(BUTTON_HEIGHT);
            imageView.relocate(320 / 2 - 170 / 2, 180 - BUTTON_HEIGHT);
            button = new Label();
            button.setOnMouseEntered(e -> {
                imageView.setImage(active);
                new MediaPlayer(sound).play();
            });
            button.setOnMouseClicked(e -> {
                text = Optional.of(textField.getText());
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
            imageView.setFitHeight(BUTTON_HEIGHT);
            imageView.relocate(320 / 2 - 90 / 2, 180 - BUTTON_HEIGHT);
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

    private void setPopUpContent(Group group, VBox getGameModeContent, Stage getGameMode) {
        fixPopUpContent(group, getGameModeContent, getGameMode);
        try {
            getGameMode.getScene().setFill(new ImagePattern(new Image(new FileInputStream("images/backgrounds/color-plate-bg-orange@2x.png"))));
            getGameMode.getScene().setCursor(new ImageCursor(new Image(new FileInputStream("images/cursors/mouse_auto.png"))));
        } catch (FileNotFoundException ignored) {
        }
        getGameModeContent.getChildren().clear();
        getGameModeContent.setSpacing((180 - BUTTON_HEIGHT) / 4);
    }

    private void fixPopUpContent(Group group, VBox getGameModeContent, Stage getGameMode) {
        group.getChildren().add(getGameModeContent);
        getGameMode.setScene(new Scene(group, 320, 180));
        getGameMode.setResizable(false);
        getGameModeContent.setAlignment(Pos.CENTER);
        getGameModeContent.getStylesheets().add("css/vBox.css");
        getGameModeContent.setMinHeight(180 - BUTTON_HEIGHT);
        getGameModeContent.setMaxHeight(180 - BUTTON_HEIGHT);
        getGameMode.initModality(Modality.APPLICATION_MODAL);
        getGameMode.setAlwaysOnTop(true);
    }

    private void fixPopUpButton(Group group, Image inactive, ImageView imageView, Label button) throws FileNotFoundException {
        button.setOnMouseExited(e -> imageView.setImage(inactive));
        button.setFont(Font.loadFont(new FileInputStream("./fonts/averta-extrathin-webfont.ttf"), 17));
        button.setTextFill(Color.CORAL);
        button.setMinSize(90, BUTTON_HEIGHT);
        button.setAlignment(Pos.CENTER);
        button.setTextAlignment(TextAlignment.CENTER);
        button.relocate(320 / 2 - 90 / 2, 180 - BUTTON_HEIGHT);
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
            ImageView imageView = new ImageView(inactive);
            group.getChildren().add(imageView);
            imageView.setFitWidth(90);
            imageView.setFitHeight(BUTTON_HEIGHT);
            imageView.relocate(320 / 2 - 90 / 2, 180 - BUTTON_HEIGHT);
            Label button = new Label("Okay");
            button.setOnMouseEntered(e -> {
                imageView.setImage(active);
                new MediaPlayer(sound).play();
            });
            button.setOnMouseClicked(e -> popUp.close());
            fixPopUpButton(group, inactive, imageView, button);
        } catch (FileNotFoundException ignored) {
        }
        fixPopUpContent(group, popUpContent, popUp);
        try {
            popUp.getScene().setFill(new ImagePattern(new Image(new FileInputStream("images/backgrounds/color-plate-bg-purple@2x.png"))));
            popUp.getScene().setCursor(new ImageCursor(new Image(new FileInputStream("images/cursors/mouse_auto.png"))));
        } catch (FileNotFoundException ignored) {
        }
    }

    public void showPopUp(String text) {
        popUpContent.getChildren().clear();
        Label label = new Label(text);
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        try {
            label.setFont(Font.loadFont(new FileInputStream("./fonts/averta-black-webfont.ttf"), 17));
        } catch (FileNotFoundException ignored) {
        }
        label.setTextFill(Color.CORAL);
        label.setMinHeight(180 - BUTTON_HEIGHT);
        label.setMaxHeight(180 - BUTTON_HEIGHT);
        popUpContent.getChildren().add(label);
        popUp.showAndWait();
    }

    public Optional<Integer>[] getGameType() {
        gameMode = new Optional[]{Optional.empty(), Optional.empty()};
        getGameMode.showAndWait();
        return gameMode;
    }

    public Optional<String> getText(String prompt, String buttonText) {
        text = Optional.empty();
        textField.setPromptText(prompt);
        button.setText(buttonText);
        getText.showAndWait();
        return text;
    }
}
