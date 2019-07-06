package graphicControllers.menus;

import graphicControllers.Menu;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import menus.UI;
import view.GUIChangeMenuButton;
import view.NodeWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ChatMenu extends Menu {
    private static final double BORDER_OF_CHATBOX = 10.0;
    private double chatBoxHeight, chatBoxWidth;

    VBox vBox, messageVBox;

    HBox bottom;
    TextField textField;

    public ChatMenu() {
        super(Id.CHAT_MENU, "Chat Menu", 800, 600);
        setAutoRefresh();
        vBox = new VBox();
        vBox.setMinHeight(600 - 50);
        vBox.setMaxHeight(600 - 50);
        vBox.setMinWidth(800);
        vBox.setMaxWidth(800);
        vBox.getStylesheets().add("css/vBox.css");

        chatBoxHeight = windowHeight - 100;
        chatBoxWidth = windowWidth;

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMinHeight(chatBoxHeight);
        scrollPane.setPrefHeight(chatBoxHeight);
        scrollPane.setMaxHeight(chatBoxHeight);
        scrollPane.setMinWidth(chatBoxWidth);
        scrollPane.setPrefWidth(chatBoxWidth);
        scrollPane.setMaxWidth(chatBoxWidth);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHmax(0);
        getView().getScene().getStylesheets().add("css/scrollPane.css");
        messageVBox = new VBox();
        messageVBox.setSpacing(10);
        scrollPane.setContent(messageVBox);

        bottom = new HBox();
        bottom.setMinHeight(50);
        bottom.setMaxHeight(50);
        bottom.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        vBox.getChildren().add(scrollPane);
        vBox.getChildren().add(bottom);
        //vBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        addComponent(new NodeWrapper(vBox));

        try {
            setBackGround(new Image(new FileInputStream("images/backgrounds/bg@2x.jpg")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // set back button
        GUIChangeMenuButton back = new GUIChangeMenuButton(windowWidth / 2 - 100.0 / 2, windowHeight - 50,
                100, 50);
        try {
            back.setImage(new Image(new FileInputStream("./images/buttons/button_secondary@2x.png")));
            back.setActiveImage(new Image(new FileInputStream("./images/buttons/button_secondary_glow@2x.png")));
            back.setSound(new Media(new File("sfx/sfx_ui_menu_hover.m4a").toURI().toString()));
        } catch (FileNotFoundException ignored) {
        }
        back.setText("Back");
        back.setGoalMenuID(Id.MAIN_MENU);
        back.setOnMouseClicked(e -> UI.decide("exit"));
        addComponent(back);

        setTextBox();
    }

    private void setTextBox() {
        textField = new TextField();
        textField.setMinHeight(50);
        textField.setMaxHeight(50);
        textField.setMinWidth(windowWidth - 20);
        bottom.getChildren().add(textField);
        textField.setBackground(Background.EMPTY);
        try {
            textField.setFont(Font.loadFont(new FileInputStream("fonts/chatmenu_font2_bold.otf"), 25));
            textField.setStyle("-fx-text-inner-color: #ff4326;");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        getView().getScene().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                handleSendMessage();
            }
        });


        Button button = new Button("refresh");
        button.setOnMouseClicked(e -> handleGetMessages());
        bottom.getChildren().add(button);
    }

    private void handleGetMessages() {
        String[] newMessages = null;
        try {
            newMessages = getUIOutputAsString("get messages").split("\\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (newMessages == null || newMessages.length == 0) {
            newMessages = new String[]{"empty!", "emty!"};
        }
        for (String message: newMessages) {
            Label messageLabel = new Label(message);
            messageLabel.setStyle("-fx-background-color:POWDERBLUE");
            try {
                messageLabel.setFont(Font.loadFont(new FileInputStream("fonts/chatmenu_font2_bold.otf"), 25));
                messageLabel.setTextFill(Color.rgb(44, 74, 255));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            messageVBox.getChildren().add(messageLabel);
            //top.getChildren().add(messageLabel);
        }
    }

    private void handleSendMessage() {
        String message = textField.getText();
        UI.decide("send message: " + message);
        textField.clear();
    }

    @Override
    public void refresh() {
        handleGetMessages();
    }
}