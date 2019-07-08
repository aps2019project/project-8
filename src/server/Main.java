package server;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        new Thread(() -> Server.main(new String[]{})).start();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setTitle("Server");
        final Scene rootScene = new Scene(root, 300, 300);
        primaryStage.setScene(rootScene);
        primaryStage.show();
        Button online = new Button("Online");
        online.relocate(150, 100);
        Group onlineUsers = new Group();
        final Scene onlineUsersScene = new Scene(onlineUsers, 300, 300);
        root.getChildren().add(online);
        Button onlineUsersBack = new Button("back");
        onlineUsersBack.setOnMouseClicked(e -> primaryStage.setScene(rootScene));
        onlineUsers.getChildren().add(onlineUsersBack);
        onlineUsersBack.relocate(150, 250);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMinWidth(300);
        scrollPane.setMaxWidth(300);
        scrollPane.setMinHeight(200);
        scrollPane.setMaxHeight(200);
        onlineUsers.getChildren().add(scrollPane);
        VBox list = new VBox();
        list.setFillWidth(true);
        list.setMinWidth(300);
        list.setMaxWidth(300);
        list.setMinHeight(150);
        scrollPane.setContent(list);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHmax(0);
        online.setOnMouseClicked(e -> {
            list.getChildren().clear();
            Server.getInstance().getPlayers().values().forEach(account -> list.getChildren().add(new Label(account.
                    getName())));
            primaryStage.setScene(onlineUsersScene);
        });
        Group cardsView = new Group();
        Scene cardsScene = new Scene(cardsView, 300, 300);
        Button cards = new Button("cards");
        root.getChildren().add(cards);
        cards.relocate(150, 200);
        ScrollPane cardsPane = new ScrollPane();
        cardsPane.setMinWidth(300);
        cardsPane.setMaxWidth(300);
        cardsPane.setMinHeight(200);
        cardsPane.setMaxHeight(200);
        cardsPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        cardsPane.setHmax(0);
        cardsView.getChildren().add(cardsPane);
        VBox cardsList = new VBox();
        cardsList.setFillWidth(true);
        cardsList.setMinWidth(300);
        cardsList.setMaxWidth(300);
        cardsList.setMinHeight(150);
        cardsPane.setContent(cardsList);
        cards.setOnMouseClicked(e -> {
            cardsList.getChildren().clear();
            Server.getInstance().getShopInterface().getCollectionItems().stream().map(ci -> ci.getName() + " " + ci.
                    getCount()).sorted().forEach(st -> cardsList.getChildren().add(new Label(st)));
            primaryStage.setScene(cardsScene);
        });
        Button cardsBack = new Button("back");
        cardsView.getChildren().add(cardsBack);
        cardsBack.relocate(150, 250);
        cardsBack.setOnMouseClicked(e -> primaryStage.setScene(rootScene));
    }
}