package graphicControllers;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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

public class MenuManager {

    private static final int BUTTON_HEIGHT = 40;
    /**
     * Stage should be removed!
     */
    private Stage stage;
    private Stage popUp;
    private VBox popUpContent = new VBox();
    private Button back = new Button("Okay");
    private Media sound = new Media(new File("./sfx/sfx_unit_onclick.m4a").toURI().toString());
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private static MenuManager instance = null;

    public static MenuManager getInstance() {
        return instance;
    }

    private Menu currentMenu;

    private Map<Integer, Menu> menusIDs = new HashMap<>();

    public void setCurrentMenu(Menu currentMenu) {
        currentMenu.refresh();
        this.currentMenu = currentMenu;
    }


    private void listenForMenuChange() {
        stage.setScene(currentMenu.getView().getScene());
        for (MenuChangeComponent changeComponent: currentMenu.getMenuChangeComponents()) {
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

    Label okay = new Label("Okay");

    public void startProcessOnStage() {
        instance = this;
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
            Label okay = new Label("Okay");
            okay.setOnMouseEntered(e -> {
                imageView.setImage(active);
                new MediaPlayer(sound).play();
            });
            okay.setOnMouseClicked(e -> popUp.close());
            okay.setOnMouseExited(e -> imageView.setImage(inactive));
            okay.setFont(Font.loadFont(new FileInputStream("./fonts/averta-extrathin-webfont.ttf"), 17));
            okay.setTextFill(Color.CORAL);
            okay.setMinSize(90, BUTTON_HEIGHT);
            okay.setAlignment(Pos.CENTER);
            okay.setTextAlignment(TextAlignment.CENTER);
            okay.relocate(320 / 2 - 90 / 2, 180 - BUTTON_HEIGHT);
            group.getChildren().add(okay);
        } catch (FileNotFoundException e) {
        }
        group.getChildren().add(popUpContent);
        popUp.setScene(new Scene(group, 320, 180));
        popUp.setResizable(false);
        popUpContent.setAlignment(Pos.CENTER);
        popUpContent.getStylesheets().add("css/vBox.css");
        popUpContent.setMinHeight(180 - BUTTON_HEIGHT);
        popUpContent.setMaxHeight(180 - BUTTON_HEIGHT);
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setAlwaysOnTop(true);
        try {
            popUp.getScene().setFill(new ImagePattern(new Image(new FileInputStream("images/backgrounds/color-plate-bg-purple@2x.png"))));
            popUp.getScene().setCursor(new ImageCursor(new Image(new FileInputStream("images/cursors/mouse_auto.png"))));
        } catch (FileNotFoundException ignored) {
        }
        listenForMenuChange();
        stage.setResizable(false);
        stage.show();
    }

    public void setCurrentMenu(int menuID) {
        currentMenu = menusIDs.get(menuID);
        currentMenu.refresh();
        listenForMenuChange();
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
}
