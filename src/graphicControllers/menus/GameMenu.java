package graphicControllers.menus;

import com.gilecode.yagson.com.google.gson.JsonObject;
import gen.NamesAndTypes;
import graphicControllers.Menu;
import graphicControllers.MenuManager;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import menus.UI;
import model.Map;
import model.Unit;
import view.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameMenu extends Menu {
    private static final double CELL_CONTENT_WIDTH = 100;
    private static final double CELL_CONTENT_HEIGHT = 70;
    private static final double TILE_WITDH = 50;
    private static final double TILE_HEIGHT = 30;
    private static final Color textColor = Color.rgb(176, 42, 226);
    private static final double BUFF_WIDTH = 10;
    private static final double BUFF_HEIGHT = 10;
    private static final int TIME_LIMIT = 20;



    private class Refresher extends Thread {
        @Override
        public void run() {
//            while (!interrupted()) {
//                if (!UI.getGame().getCurrentPlayer().getName().equals(UI.getAccount().getName()))
//                    Platform.runLater(GameMenu.this::refresh);
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    interrupt();
//                    return;
//                }
//            }
//        }

            while (!interrupted()) {
                if (anotherGame || !UI.getGame().getCurrentPlayer().getName().equals(UI.getAccount().getName()))
                    Platform.runLater(GameMenu.this::refresh);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    interrupt();
                    return;
                }
            }
        }
    }

    private Refresher refresher;

    private boolean hasThread = false;
    private boolean turnChecker = false;

    static class Assets {
        private static HashMap<String, Image> imageMap = new HashMap<>();
        private static HashMap<String, Media> mediaMap = new HashMap<>();

        public static Image getImage(String path) {
            if (!imageMap.containsKey(path)) {
                try {
                    imageMap.put(path, new Image(new FileInputStream(path)));
                } catch (FileNotFoundException e) {
                    try {
                        return new Image(new FileInputStream("images/gameIcons/gifs/test_idle.gif"));
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return imageMap.get(path);
        }

        public static Media getMedia(String path) {
            if (!mediaMap.containsKey(path)) {
                mediaMap.put(path, new Media(new File(path).toURI().toString()));
            }
            return mediaMap.get(path);
        }
    }

    private static final Media attackSound = new Media(new File("sfx/attack.m4a").toURI().toString());
    private static final Media deathSound = new Media(new File("sfx/death.m4a").toURI().toString());
    private static final Media insertSound = new Media(new File("sfx/insert_card.m4a").toURI().toString());
    private static final Media runSound = new Media(new File("sfx/runn.m4a").toURI().toString());
    private static transient GameMenu instance = null;
    public ArrayList<ArrayList<Node>> history = new ArrayList<>();
    Rectangle disableEventRectangle;
    ArrayList<AnimationTimer> animations = new ArrayList<>();
    boolean isRecording;
    private double speedCoefficient = 1.0;
    private boolean hasPopup = true;
    private ComponentSet firstPlayerBar, secondPlayerBar;
    private ComponentSet gridCells;
    private ComponentSet cardBar;
    private ComponentSet menuButtons;
    private ComponentSet everyThing;
    private ComponentSet collectibleComponents;
    private CardView selectedCardInfo;
    private String draggedCardName;
    private MenuComponent draggedComponenet;
    private ArrayList<String> selectedComboCardIds = new ArrayList<>();
    private boolean clickedOnShowNextCard = false;
    private String selectedCollectibleID;
    private String[][] gridStrings;
    private boolean showCutSceneMode = false;
    private String selectedCardID;
    private int numberOfDisable = 0;
    private boolean isInGame;


    public boolean anotherGame = false;
    public String otherGame = "";

    public GameMenu() {
        super(Id.IN_GAME_MENU, "Game Menu", windowDefaultWidth, windowDefaultHeight);
        getView().getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                speedCoefficient *= 2;
            } else if (event.getCode() == KeyCode.DOWN) {
                speedCoefficient /= 2;
            }
        });
        setUpEndTurnTimer();
        setUpBackGround();
        setUpMenuButtons();
        instance = this;
        startRecording();
    }

    public static GameMenu getInstance() {
        return instance;
    }

    public void startRecording() {
        isRecording = true;
    }

    public void stopRecording() {
        ReplayMenu.addGame(this);
        isRecording = false;
    }

    class TurnTimer extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep((long) (1000 / speedCoefficient));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isInGame && turnChecker) {
                    Platform.runLater(() -> {
                        Label timer = (Label) ((NodeWrapper) menuButtons.getComponentByID("timer")).getValue();
                        timer.setText(Integer.toString(Integer.parseInt(timer.getText()) - 1));
                        if (timer.getText().equals("0")) {
                            handleEndTurn();
                        }
                    });
                }
            }
        }
    }

    TurnTimer turnTimer = new TurnTimer();

    private void setUpEndTurnTimer() {
        turnTimer.start();
    }

    private void setUpBackGround() {
        // setting up the back ground
        setBackGround(Assets.getImage("images/backgrounds/battlemap0_background@2x.png"));


        ImageView middleGround = new ImageView(Assets.getImage("images/gameIcons/middleGround/battlemap0_middleground.png"));
        middleGround.setFitHeight(windowHeight);
        middleGround.setFitWidth(windowWidth);
        addComponent(new NodeWrapper(middleGround));

        ImageView foreGround = new ImageView(Assets.getImage("images/foregrounds/battlemap0_foreground_002@2x.png"));
        foreGround.setPreserveRatio(true);
        foreGround.setFitWidth(windowHeight / 3);
        foreGround.relocate(windowWidth - foreGround.getFitWidth(), windowHeight - foreGround.getFitWidth() * foreGround.getImage().getHeight() / foreGround.getImage().getWidth());
        addComponent(new NodeWrapper(foreGround));
    }

    private synchronized void disableEvents() {
        numberOfDisable++;
        if (numberOfDisable == 1) {
            showCutSceneMode = true;
            disableEventRectangle = new Rectangle(0, 0, windowWidth, windowHeight);
            disableEventRectangle.setOpacity(0);
            addComponent(new NodeWrapper(disableEventRectangle));
        } else {
            moveComponentToFront(new NodeWrapper(disableEventRectangle));
        }

    }

    private synchronized void enableEvents() {
        numberOfDisable = Math.max(0, numberOfDisable - 1);
        if (numberOfDisable == 0) {
            showCutSceneMode = false;
            removeComponent(new NodeWrapper(disableEventRectangle));
            disableEventRectangle = null;
            notify();
        }
    }

    private synchronized void handleDeathAnimation() {
        disableEvents();
        String out = getUIOutputAsString("get dead");
        if (!gameEnded(out)) {
            new Thread(() -> {

                ArrayList<NodeWrapper> effects = new ArrayList<>();
                String[] output = out.split("\\n");
                ArrayList<String> deathIDs = new ArrayList<>();
                for (String s : output)
                    if (s.contains("death")) {
                        deathIDs.add(s.split(" ")[1]);
                    }
                ImageView castSpellEffect = new ImageView(Assets.getImage("images/gameIcons/Cells/onDeath.gif"));
                Platform.runLater(() -> {
                    if (!deathIDs.isEmpty())
                        new MediaPlayer(deathSound).play();
                    try {
                        for (String s : output) {
                            if (s.split(" ")[0].equalsIgnoreCase("death")) {
                                int row = Integer.parseInt(s.split(" ")[2]);
                                int column = Integer.parseInt(s.split(" ")[3]);
                                ImageView imageView = getCellContent(row, column);
                                imageView.setImage(getImageByCardName(s.split(" ")[1], "death", "gif"));
                                ImageView tile = getTile(row, column);
                                if (s.contains("onDeath")) {
                                    castSpellEffect.relocate(tile.getLayoutX(), tile.getLayoutY() - 20);
                                    castSpellEffect.setFitHeight(tile.getFitHeight() + 20);
                                    castSpellEffect.setFitWidth(tile.getFitWidth());
                                    addComponent(new NodeWrapper(castSpellEffect));
                                    effects.add(new NodeWrapper(castSpellEffect));
                                }
                            }
                        }
                    } catch (Throwable ex) {
                        forceRefresh();
                        System.out.println("GOOOD!");
                    }
                });
                try {
                    if (!deathIDs.isEmpty())
                        Thread.sleep((long) (1000 / speedCoefficient));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    enableEvents();
                    for (NodeWrapper n : effects)
                        removeComponent(n);
                    effects.clear();
                });
//                }

            }).start();
        } else {
            enableEvents();
        }

    }

    private synchronized void handleSelectCollectible(String s) {
        selectedCollectibleID = s;
        String out = getUIOutputAsString("select " + s);
        if (!gameEnded(out)) {
            if (hasPopup)
                showPopUp(out);
            setGridOnCollectibleSelected();
        }
    }

    private synchronized void handleEndTurn() {
        if (!UI.getGame().getCurrentPlayer().getName().equals(UI.getAccount().getName()))
            return;


        String out = getUIOutputAsString("end turn");
        out = out.trim();
        if (!gameEnded(out)) {
            turnChecker = false;
            refresh();
            String[] commands = out.split("\\n");
            for (String s : commands) {
                s = s.trim();
                Pattern pattern = Pattern.compile("(\\w+) moved from (\\d+) (\\d+) to (\\d+) (\\d+)");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    selectedCardID = matcher.group(1);
                    int sx = Integer.parseInt(matcher.group(2)) - 1;
                    int sy = Integer.parseInt(matcher.group(3)) - 1;
                    int dx = Integer.parseInt(matcher.group(4)) - 1;
                    int dy = Integer.parseInt(matcher.group(5)) - 1;

                    handleGraphicallMove(sx, sy, dx, dy, true);
                }

                pattern = Pattern.compile("a new card inserted to (\\d+) (\\d+)");
                matcher = pattern.matcher(s);
                if (matcher.find()) {
                    int x = Integer.parseInt(matcher.group(1)) - 1;
                    int y = Integer.parseInt(matcher.group(2)) - 1;
                    handleInsertCard(x, y, true);
                }

                pattern = Pattern.compile("attack from (\\w+) to (\\w+)");
                matcher = pattern.matcher(s);
                if (matcher.find()) {
                    selectedCardID = (matcher.group(1));
                }

                pattern = Pattern.compile("hero power on (\\d+) (\\d+)");
                matcher = pattern.matcher(s);
                if (matcher.find()) {
                    int x = Integer.parseInt(matcher.group(1)) - 1;
                    int y = Integer.parseInt(matcher.group(2)) - 1;
                }

                pattern = Pattern.compile("apply collectible (\\d+) to (\\d+)");
                matcher = pattern.matcher(s);
                if (matcher.find()) {
                    int x = Integer.parseInt(matcher.group(1)) - 1;
                    int y = Integer.parseInt(matcher.group(2)) - 1;
                }
            }
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 5; j++) {
                    if (getDescriptionByLocation(j, i) != null && getDescriptionByLocation(j, i).contains("passive")) {
                        int finalJ = j;
                        int finalI = i;
                        new Thread(() -> {
                            ImageView tile = getTile(finalJ, finalI);
                            ImageView passive = new ImageView(Assets.getImage("images/gameIcons/Cells/passive.gif"));
                            Platform.runLater(() -> {
                                passive.setFitWidth(tile.getFitWidth());
                                passive.setFitHeight(tile.getFitHeight());
                                passive.relocate(tile.getLayoutX(), tile.getLayoutY());
                                addComponent(new NodeWrapper(passive));
                            });
                            try {
                                Thread.sleep((long) (1000 / speedCoefficient));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Platform.runLater(() -> {
                                removeComponent(new NodeWrapper(passive));
                                enableEvents();
                                refresh();
                            });
                        }).start();
                    }
                }
            }
        }
    }

    private synchronized void handleUseCollectible(int row, int column, boolean ai) {
        disableEvents();
        String out = "";
        if (!ai)
            out = getUIOutputAsString("use (" + (row + 1) + ", " + (column + 1) + ")");
        if (ai || !gameEnded(out)) {
            if (!ai && !out.equals("Successful!")) {
                if (hasPopup)
                    showPopUp(out);
                enableEvents();
                refresh();
            } else {
                new Thread(() -> {
                    try {
                        ImageView tile = getTile(row, column);
                        ImageView splash = new ImageView(Assets.getImage("images/gameIcons/collectible_splash.gif"));
                        splash.relocate(tile.getLayoutX() - 75, tile.getLayoutY() - 75);
                        splash.setFitHeight(tile.getFitHeight() + 150);
                        splash.setFitWidth(tile.getFitWidth() + 150);
                        Platform.runLater(() -> addComponent(new NodeWrapper(splash)));
                        Thread.sleep((long) (1000 / speedCoefficient));
                        Platform.runLater(() -> {
                            removeComponent(new NodeWrapper(splash));
                            enableEvents();
                            refresh();
                        });
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        } else {
            enableEvents();
            refresh();

        }
    }

    private synchronized void handleShowCollectibleinfo() {
        String out = getUIOutputAsString("show info");
        if (hasPopup)
            showPopUp(out);
    }

    private synchronized void handleInsertCard(int row, int column, boolean ai) {
        disableEvents();
        String cordinate = "(" + (row + 1) + ", " + (column + 1) + ")";
        if (draggedCardName != null) {
            String output = "";
            if (!ai) {
                output = getUIOutputAsString("insert " + draggedCardName + " in " + cordinate);
            }
            if (ai || !gameEnded(output)) {
                if (output.contains("inserted")) {
                    new Thread(() -> {
                        ImageView onSpawnEffect = new ImageView(Assets.getImage("images/gameIcons/Cells/onSpawn.gif"));
                        ImageView tile = getTile(row, column);
                        onSpawnEffect.relocate(tile.getLayoutX(), tile.getLayoutY() - 20);
                        onSpawnEffect.setFitWidth(tile.getFitWidth());
                        onSpawnEffect.setFitHeight(tile.getFitHeight() + 20);

                        ImageView strike = new ImageView(Assets.getImage("images/gameIcons/lightning_strike.gif"));
                        strike.setFitHeight(tile.getFitHeight());
                        strike.setFitWidth(tile.getFitWidth());
                        strike.relocate(tile.getLayoutX(), tile.getLayoutY());

                        Platform.runLater(() -> {
                            new MediaPlayer(insertSound).play();
                            addComponent(new NodeWrapper(strike));
                        });
                        try {
                            Thread.sleep((long) (1000 / speedCoefficient));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Platform.runLater(() -> {
                            removeComponent(new NodeWrapper(strike));
                            if (getDescriptionByLocation(row, column) != null && getDescriptionByLocation(row, column).contains("onSpawn")) {
                                new Thread(() -> {
                                    Platform.runLater(() -> addComponent(new NodeWrapper(onSpawnEffect)));
                                    try {
                                        Thread.sleep((long) (1000 / speedCoefficient));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Platform.runLater(() -> {
                                        removeComponent(new NodeWrapper(onSpawnEffect));
                                        enableEvents();
                                        refresh();
                                    });
                                }).start();
                            } else {
                                enableEvents();
                                refresh();
                            }
                        });


                    }).start();
                } else {
                    if (hasPopup)
                        showPopUp(output);
                    enableEvents();
                    refresh();
                }
            }
        } else {
            enableEvents();
        }
    }

    private synchronized void handleSelectCard(String cardID) {
        selectedCardID = cardID;
        String output = getUIOutputAsString("select " + cardID);
        if (hasPopup)
            showPopUp(output);
        cardSelectedGridChangePrimary();
    }

    private synchronized void handleUseSpecialPower(int row, int column, boolean ai) {
        if (ai)
            return;
        String out = getUIOutputAsString("use special power (" + row + ", " + column + ")");
        if (!gameEnded(out)) {
            if (hasPopup)
                showPopUp(out);
            refresh();
        }
    }

    private synchronized void handleAttackUnit(String enemyCardID, boolean ai) {
        disableEvents();
        if (enemyCardID != null) {
            int sourceRow = getRowFromUnitID(selectedCardID);
            int sourceColumn = getColumnFromUnitID(selectedCardID);
            int row = getRowFromUnitID(enemyCardID);
            int column = getColumnFromUnitID(enemyCardID);
            Unit selectedUnit;
            Unit enemyUnit;
            if (!ai) {
                selectedUnit = UI.getGame().getCurrentPlayer().getUnit(selectedCardID);
                enemyUnit = UI.getGame().getOtherPlayer().getUnit(enemyCardID);
            } else {
                selectedUnit = UI.getGame().getOtherPlayer().getUnit(selectedCardID);
                enemyUnit = UI.getGame().getCurrentPlayer().getUnit(enemyCardID);
            }

            String out = "";
            if (!ai) {
                out = getUIOutputAsString("attack " + enemyCardID);
            }
            if (ai || !gameEnded(out)) {
                if (!ai && !out.equals("Successful!")) {
                    if (hasPopup)
                        showPopUp(out);
                    enableEvents();
                    refresh();
                } else {

                    new MediaPlayer(attackSound).play();
                    new Thread(() -> {
                        try {
                            try {
                                ImageView imageView = getCellContent(sourceRow, sourceColumn);
                                ImageView tile = getTile(row, column);
                                ImageView hit;
                                boolean counterAttack = enemyUnit.canCounterAttack() && UI.getGame().canAttack(enemyUnit, selectedUnit, Math.abs(row - sourceRow) + Math.abs(column - sourceColumn));
                                if (counterAttack)
                                    hit = getCellContent(row, column);
                                else
                                    hit = new ImageView(Assets.getImage("images/gameIcons/hit.gif"));
                                ImageView onAttack = new ImageView(Assets.getImage("images/gameIcons/Cells/onAttack.gif"));
                                ImageView onDefend = new ImageView(Assets.getImage("images/gameIcons/Cells/onDefend.gif"));
                                Platform.runLater(() -> {
                                    try {
                                        imageView.setImage(getImageByCardName(getNameFromID(selectedCardID), "attack", "gif"));
                                        if (!counterAttack) {
                                            hit.setFitWidth(tile.getFitWidth());
                                            hit.setFitHeight(tile.getFitHeight());
                                            hit.relocate(tile.getLayoutX(), tile.getLayoutY());
                                            addComponent(new NodeWrapper(hit));
                                        } else
                                            hit.setImage(getImageByCardName(getNameFromID(enemyCardID), "attack", "gif"));
                                        if (getDescriptionByLocation(sourceRow, sourceColumn).contains("onAttack")) {
                                            onAttack.setFitWidth(tile.getFitWidth());
                                            onAttack.setFitHeight(tile.getFitHeight());
                                            onAttack.relocate(tile.getLayoutX(), tile.getLayoutY());
                                            addComponent(new NodeWrapper(onAttack));
                                        }
                                        if (getDescriptionByLocation(row, column).contains("onDefend")) {
                                            onDefend.setFitWidth(tile.getFitWidth());
                                            onDefend.setFitHeight(tile.getFitHeight());
                                            onDefend.relocate(tile.getLayoutX(), tile.getLayoutY());
                                            addComponent(new NodeWrapper(onAttack));
                                        }
                                    } catch (Throwable ex) {
                                        forceRefresh();
                                    }
//                                try {
//                                    getRowFromUnitID(enemyCardID);
//                                } catch (NullPointerException ex) {
//                                    // died:
//                                    hit.setOpacity(0);
//                                    getCellContent(row, column).setImage(getImageByCardName(getNameFromID(enemyCardID), "death", "gif"));
//                                }
                                });
                                try {
                                    Thread.sleep((long) (1000 / speedCoefficient));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Platform.runLater(() -> {
                                    try {
                                        if (!counterAttack)
                                            removeComponent(new NodeWrapper(hit));
                                        if (getDescriptionByLocation(sourceRow, sourceColumn).contains("onAttack"))
                                            removeComponent(new NodeWrapper(onAttack));
                                        if (getDescriptionByLocation(row, column).contains("onDefend"))
                                            removeComponent(new NodeWrapper(onDefend));
                                        enableEvents();
                                        refresh();
                                    } catch (Throwable ex) {
                                        System.out.println("GOOOD");
                                    }
                                });
                            } catch (Exception ex) {
                                enableEvents();
                                ex.printStackTrace();
                            }
                        } catch (Throwable ex) {
                            forceRefresh();
                            System.out.println("GOOOD");
                        }
                    }).start();
                }
            } else {
                enableEvents();
                refresh();
            }
        } else {
            enableEvents();
            refresh();
        }

    }

    private void toggleCellActive(int row, int column, boolean active, boolean attack) {
        ComponentSet cell = (ComponentSet) gridCells.getComponentByID(row + "," + column);
        double opacity = active ? 1 : 0;
        MenuComponent temp;
        temp = cell.getComponentByID("friendliness");
        if (temp != null) {
            ImageView friendliness = (ImageView) ((NodeWrapper) temp).getValue();
            friendliness.setOpacity(opacity);
        }
        temp = cell.getComponentByID("card_content");
        if (temp != null) {
            ImageView cardContent = (ImageView) ((NodeWrapper) temp).getValue();
            cardContent.setOpacity(opacity);
            if (attack)
                cardContent = null;
        }
    }

    //
    //
    //
    //

    private void moveUnit(String cardID, int dRow, int dColumn) {
        int sRow = getRowFromUnitID(cardID);
        int sColumn = getColumnFromUnitID(cardID);

        ImageView imageView = getImageViewByCardName(getNameFromID(cardID), "run", "gif");
        if (sColumn > dColumn)
            imageView.setScaleY(-1);

        int CellWidth = 50;
        int CellHeight = 50;

        int width = CellWidth;
        int height = CellHeight;
        int lx = (int) gridCells.getX();
        int ly = (int) gridCells.getY();
        int sx = (int) (lx + sColumn * width + width / 2.0);
        int sy = (int) (ly + sRow * height + height / 2.0);
        int dx = (int) (lx + dColumn * width + width / 2.0);
        int dy = (int) (ly + dRow * height + height / 2.0);
        Line orthogonal = new Line(sx, sy, dx, dy);
        imageView.setFitWidth(CellWidth);
        imageView.setFitHeight(CellHeight);
        orthogonal.setFill(Color.BLACK);

        PathTransition pathTransition = new PathTransition();
        pathTransition.setNode(imageView);
        pathTransition.setDuration(Duration.millis(500 / speedCoefficient));
        pathTransition.setPath(orthogonal);
        pathTransition.setCycleCount(1);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        toggleCellActive(dRow, dColumn, false, false);


        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageView.setOpacity(0);
                toggleCellActive(dRow, dColumn, true, false);
            }
        });
        addComponent(new NodeWrapper(imageView));
        addComponent(new NodeWrapper(orthogonal));
        pathTransition.play();
    }

    private void handleCellSpawn(int x, int y) {
        disableEvents();
        ComponentSet cell = (ComponentSet) gridCells.getComponentByID(x + "," + y);
        cell = makeCellFromString(gridStrings, x, y);
        ImageView source = (ImageView) ((NodeWrapper) cell.getComponentByID("card_content")).getValue();
        new Thread(() -> {
            Platform.runLater(() -> {
                source.setImage(getImageByCardName(getNameFromID(selectedCardID), "breathing", "gif"));
                KeyValue xValue = new KeyValue(source.xProperty(), 0);
                KeyValue yValue = new KeyValue(source.yProperty(), 0);
                KeyValue rValue = new KeyValue(source.rotateProperty(), 0);
                KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), xValue, yValue, rValue);
                Timeline timeline = new Timeline(keyFrame);
                timeline.play();
            });
            try {
                Thread.sleep((long) (1000 / speedCoefficient));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                enableEvents();
                refresh();
            });
        }).start();
    }


    //
    //
    //
    //
    //

    private void handleGraphicallMove(int sourceRow, int sourceColumn, int row, int column, boolean ai) {


        disableEvents();
        ImageView source;
        if (!ai)
            source = getImageViewInGrid(selectedCardID);
        else source = getCellContent(sourceRow, sourceColumn);

        String out = "";
        if (!ai)
            out = getUIOutputAsString("move to (" + (row + 1) + ", " + (column + 1) + ")");
        if (ai || !gameEnded(out)) {
            if (!ai && !out.contains("moved")) {
                if (hasPopup)
                    showPopUp(out);
                selectedCardID = null;
                forceRefresh();
            } else {
                //MediaPlayer mediaPlayer = new MediaPlayer(runSound);

                new Thread(() -> {
                    Platform.runLater(() -> {
                        new MediaPlayer(runSound).play();
                        //      mediaPlayer.play();
                        source.setImage(getImageByCardName(getNameFromID(selectedCardID), "run", "gif"));
                        KeyValue xValue = new KeyValue(source.xProperty(), (column - sourceColumn) * CELL_CONTENT_WIDTH);
                        KeyValue yValue = new KeyValue(source.yProperty(), (row - sourceRow) * CELL_CONTENT_HEIGHT);
                        KeyValue rValue = new KeyValue(source.rotateProperty(), 0);
                        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000 / speedCoefficient), xValue, yValue, rValue);
                        Timeline timeline = new Timeline(keyFrame);
                        timeline.play();
                    });
                    try {
                        Thread.sleep((long) (1000 / speedCoefficient));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        //    mediaPlayer.stop();
                        enableEvents();
                        selectedCardID = null;
                        refresh();
                    });
                }).start();
            }

        } else {
            forceRefresh();
        }
    }

    private void handleMoveCard(int row, int column, boolean ai) {
        int sourceRow = getRowFromUnitID(selectedCardID);
        int sourceColumn = getColumnFromUnitID(selectedCardID);
        handleGraphicallMove(sourceRow, sourceColumn, row, column, ai);
    }

    private void forceRefresh() {
        enableEvents();
        refresh();
    }

    private void handleAttackCombo(String enemyCardID) {
        StringBuilder stringBuilder = new StringBuilder("attack combo ");
        stringBuilder.append(enemyCardID);
        for (String s : selectedComboCardIds) {
            stringBuilder.append(" ");
            stringBuilder.append(s);
        }
        selectedComboCardIds.clear();


        String out = getUIOutputAsString(stringBuilder.toString());
        if (!gameEnded(out)) {
            if (hasPopup)
                showPopUp(out);
            refresh();
        }
    }

    private boolean gameEnded(String output) {
        if (output.contains("winner is:") || output.contains("HAHA!")) {

            stopRecording();
            String[] parts = output.split("\\n");
            output = "";
            for (int i = 0; i < parts.length; i++)
                if (parts[i].contains("winner is:") || parts[i].contains("HAHA!") || parts[i].contains("$$$")) {
                    output += parts[i] + "\n";
                }
            try {
                UI.decide("end game");
                Rectangle rectangle = new Rectangle(0, 0, windowWidth, windowHeight);
                rectangle.setFill(Color.RED);
                rectangle.setOpacity(0.1);
                addComponent(new NodeWrapper(rectangle));
                ImageView imageView = new ImageView(Assets.getImage("images/gameIcons/notification.png"));
                imageView.setFitWidth(windowWidth);
                imageView.setFitHeight(windowHeight - 300);
                imageView.relocate(0, 150);
                addComponent(new NodeWrapper(imageView));
                Label label = new Label(output);
                label.relocate(0, windowHeight / 2 - 50);
                label.setFont(Font.loadFont(new FileInputStream("fonts/averta-black-webfont.ttf"), 20));
                label.setAlignment(Pos.CENTER);
                label.setMinSize(windowWidth, 100);
                label.setMaxSize(windowWidth, 100);
                label.setTextFill(Color.rgb(158, 79, 124));
                addComponent(new NodeWrapper(label));
                MenuComponent component = menuButtons.getComponentByID("main_menu");
                moveComponentToFront(component);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private ImageView getImageViewInGrid(String cardID) {
        int row = getRowFromUnitID(cardID);
        int column = getColumnFromUnitID(cardID);
        return getCellContent(row, column);
    }

    private ImageView getCellContent(int row, int column) {
        ComponentSet cell = (ComponentSet) gridCells.getComponentByID(row + "," + column);
        if (cell == null) {
            System.err.println("NUll cell");
        }
        if (cell.getComponentByID("card_content") == null) {
            System.err.println(row + " " + column);
            System.err.println("content null");
        }
        if (((NodeWrapper) cell.getComponentByID("card_content")).getValue() == null) {
            System.err.println("value null");
        }
        return (ImageView) ((NodeWrapper) cell.getComponentByID("card_content")).getValue();
    }

    private int getRowFromUnitID(String cardID) {
        String description = getDescriptionByName(cardID);
        Pattern pattern = Pattern.compile(".*location: \\((\\d+), (\\d+)\\).*");
        Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1)) - 1;
        } else {
            throw new NullPointerException();
        }
    }

    private int getColumnFromUnitID(String cardID) {
        String description = getDescriptionByName(cardID);
        Pattern pattern = Pattern.compile(".*location: \\((\\d+), (\\d+)\\).*");
        Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(2)) - 1;
        } else {
            throw new NullPointerException();
        }
    }

    private void setOnEnterAndExitEffect(GUIButton guiButton, String text, String enterImagePath, String exitImagePath) {
        guiButton.setImage(Assets.getImage(exitImagePath));

        guiButton.setText(text);
        guiButton.setActiveImage(Assets.getImage(enterImagePath));

        guiButton.setSound(new Media(new File("sfx/sfx_unit_onclick.m4a").toURI().toString()));
    }

    private void setUpMenuButtons() {
        menuButtons = new ComponentSet();

        ImageView collectibleBackground = new ImageView(Assets.getImage("images/gameIcons/menuButtons/collectible_background.png"));
        collectibleBackground.setFitHeight(268);
        collectibleBackground.setFitWidth(201);
        collectibleBackground.relocate(30 + 5, -300 + 27);
        menuButtons.addMenuComponent(new NodeWrapper(collectibleBackground));

        GUIButton refresh = new GUIButton(-925, -120, 100, 100);
        setOnEnterAndExitEffect(refresh, "", "images/gameIcons/menuButtons/cancel.png",
                "images/gameIcons/menuButtons/cancel.png");
        refresh.setOnMouseClicked(e -> refresh());
        menuButtons.addMenuComponent(refresh, "refresh_button");

        GUIButton endTurnButton = new GUIButton(-50, 0, 300, 100);
        setOnEnterAndExitEffect(endTurnButton, "END TURN", "images/gameIcons/menuButtons/end_turn_glow.png"
                , "images/gameIcons/menuButtons/end_turn_normal.png");
        endTurnButton.setOnMouseClicked(e -> handleEndTurn());
        menuButtons.addMenuComponent(endTurnButton);

        GUIButton mainMenu = new GUIButton(-50 + 20, 100 - 20, 150, 70);
        setOnEnterAndExitEffect(mainMenu, "Main Menu", "images/gameIcons/menuButtons/ui_left_glowing.png",
                "images/gameIcons/menuButtons/ui_left_normal.png");
        mainMenu.setOnMouseClicked(e -> {
            UI.decide("exit");
            isInGame = false;
            if (refresher != null && !refresher.isInterrupted()) {
                hasThread = false;
                refresher.interrupt();
            }            MenuManager.getInstance().setCurrentMenu(Id.MAIN_MENU);
        });
        menuButtons.addMenuComponent(mainMenu, "main_menu");

        GUIChangeMenuButton graveyard = new GUIChangeMenuButton(-50 + 150 - 20, 100 - 20, 150, 70);
        setOnEnterAndExitEffect(graveyard, "Graveyard", "images/gameIcons/menuButtons/ui_right_glowing.png",
                "images/gameIcons/menuButtons/ui_right_normal.png");
        graveyard.setGoalMenuID(Id.GRAVE_YARD_MENU);
        graveyard.setOnMouseClicked(e -> {
            isInGame = false;
            if (refresher != null && !refresher.isInterrupted()) {
                hasThread = false;
                refresher.interrupt();
            }
        });
        menuButtons.addMenuComponent(graveyard);

        GUIButton collectibles = new GUIButton(140 - 10 + 20, -100 + 10 - 5, 100, 100);
        setOnEnterAndExitEffect(collectibles, "", "images/gameIcons/menuButtons/select_collectible_hovered.png",
                "images/gameIcons/menuButtons/select_collectible.png");
        collectibles.setOnMouseClicked(e -> {
            HashMap<String, String> map = new HashMap<>();
            List<String> list = getCollectibles();
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);
                map.put(getNameFromID(s), s);
                list.set(i, getNameFromID(s));
            }
            Optional<String> s = popUpGetList(list, "Select", "Select collectible");
            s.ifPresent(s1 -> handleSelectCollectible(map.get(s1)));
        });
        menuButtons.addMenuComponent(collectibles);

        Label timer = new Label(TIME_LIMIT + "");
        timer.setTextFill(textColor);
        try {
            timer.setFont(Font.loadFont(new FileInputStream("fonts/chatmenu_font2.ttf"), 80));
        } catch (FileNotFoundException ignored) {
        }
        timer.relocate(-895, -220);
        menuButtons.addMenuComponent(new NodeWrapper(timer), "timer");

        menuButtons.relocate(windowWidth - 250, windowHeight - 200);
        addComponent(menuButtons);

    }

    private String getNameFromID(String s) {
        if (s == null)
            return null;
        Pattern pattern = Pattern.compile("([^_]*)_([^_]*)_([^_]*)");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }

    // the grid setups when collectible is chosen
    private void setGridOnCollectibleSelected() {
        showCardInfo(getNameFromID(selectedCollectibleID));
        setGridColor(Color.GOLD);
        for (int row = 0; row < Map.NUMBER_OF_ROWS; row++) {
            for (int column = 0; column < Map.NUMBER_OF_COLUMNS; column++) {
                ImageView interactor = getInteractor(row, column);
                int finalRow = row, finalColumn = column;
                interactor.setOnMouseClicked(e -> handleUseCollectible(finalRow, finalColumn, false));

            }
        }
    }

    private List<String> getCollectibles() {
        ArrayList<String> list = new ArrayList<>();
        String[] collectibles = getUIOutputAsString("show collectibles").split("\\n");
        for (String s : collectibles) {
            if (s.matches(".+: Name :.*")) {
                list.add(s.replaceAll(": Name.*", ""));
            }
        }
        return list;
    }

    private ComponentSet setHandOptions(String deckCapacityNumber) {
        ComponentSet handOptions = new ComponentSet();

        ImageView backGround = new ImageView(Assets.getImage("images/gameIcons/hand_options_background.png"));
        backGround.setFitWidth(70);
        backGround.setFitHeight(70);
        backGround.setOpacity(0.5);
        handOptions.addMenuComponent(new NodeWrapper(backGround));
        ImageView textBackground = new ImageView(Assets.getImage("images/gameIcons/simple_text_background.png"));
        textBackground.setFitHeight(10);
        textBackground.setFitWidth(20);
        textBackground.relocate(35 - 10, 35 - 5);
        handOptions.addMenuComponent(new NodeWrapper(textBackground), "text_background");
        Label deckNameLabel = new Label("Deck");
        deckNameLabel.relocate(35 - 10 + 4, 35 - 5 + 2);
        deckNameLabel.setTextFill(Color.AZURE);
        deckNameLabel.setFont(new Font(5));
        handOptions.addMenuComponent(new NodeWrapper(deckNameLabel), "deck_name_label");
        Label deckCapacity = new Label(deckCapacityNumber + "/" + 20);
        deckCapacity.relocate(35 - 10, 35 - 5 + 2 + 15);
        deckCapacity.setTextFill(Color.AZURE);
        deckCapacity.setFont(new Font(7));
        handOptions.addMenuComponent(new NodeWrapper(deckCapacity));
        ImageView interactor = new ImageView(Assets.getImage("images/gameIcons/hand_options_background.png"));
        interactor.setFitWidth(70);
        interactor.setFitWidth(70);
        interactor.setOpacity(0);
        interactor.setOnMouseEntered(e -> backGround.setOpacity(0.5));
        interactor.setOnMouseExited(e -> backGround.setOpacity(1));

        interactor.setOnMouseClicked(e -> {
            if (clickedOnShowNextCard)
                return;
            clickedOnShowNextCard = true;
            String s = getUIOutputAsString("show next card");
            s = s.replaceFirst(".*Name : ", "");
            s = s.replaceAll(" -.*", "").trim();
            String type = NamesAndTypes.getType(s);
            String state = "idle";
            if (type.equals("spellCard") || type.equals("usable") || type.equals("collectible")) {
                state = "actionbar";
            }
            ImageView imageView = getImageViewByCardName(s, state, "gif");
            imageView.setFitWidth(150);
            imageView.setFitHeight(170);
            imageView.relocate(50 - 17, windowHeight - 200 - 10);

            addComponent(new NodeWrapper(imageView));
            double prevLabelOpacity = deckNameLabel.getOpacity();
            double prevTextBackGroundOpacity = textBackground.getOpacity();
            double prevDeckCapacityOpacity = deckCapacity.getOpacity();
            deckNameLabel.setOpacity(-100);
            textBackground.setOpacity(-100);
            deckCapacity.setOpacity(-100);
            backGround.setOpacity(1);
            interactor.setOnMouseEntered(event -> {
            });
            interactor.setOnMouseExited(event -> {
            });

            new Thread(() -> {
                try {
                    Thread.sleep((long) (2000 / speedCoefficient));
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                Platform.runLater(() -> {
                    removeComponent(new NodeWrapper(imageView));
                    deckNameLabel.setOpacity(prevLabelOpacity);
                    textBackground.setOpacity(prevTextBackGroundOpacity);
                    deckCapacity.setOpacity(prevDeckCapacityOpacity);
                    interactor.setOnMouseEntered(event -> backGround.setOpacity(0.5));
                    interactor.setOnMouseExited(event -> backGround.setOpacity(1));

                });
                clickedOnShowNextCard = false;
            }).start();


        });
        handOptions.addMenuComponent(new NodeWrapper(interactor));

        return handOptions;
    }

    private ComponentSet makeManaBar(int mana, int capacity) {
        ComponentSet manaBar = new ComponentSet();
        for (int i = 0; i < capacity; i++) {
            ImageView imageView = new ImageView(Assets.getImage("images/gameIcons/MenuIcons/icon_mana_inactive.png"));
            if (i < mana) {
                imageView.setImage(Assets.getImage("images/gameIcons/MenuIcons/icon_mana.png"));
            }
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            imageView.relocate(i * 20 + i * 5, 0);
            manaBar.addMenuComponent(new NodeWrapper(imageView));

            Label label = new Label(mana + "/" + capacity);
            label.relocate(capacity * 25 + 5, 2.5);
            label.setTextFill(textColor);
            manaBar.addMenuComponent(new NodeWrapper(label));
        }
        return manaBar;
    }

    @Override
    public void refresh() {

//        if (!UI.getConnection().getGameInfo().get("currentPlayer").getAsString().equals(UI.getAccount().getName()))
//            disableEvents();
//        else
//            enableEvents();

        if (UI.getConnection().inGame().equals("no") && UI.getGame() == null)
            return;

        if (MenuManager.getInstance().getCurrentMenu() instanceof GameMenu) {
            isInGame = true;
        }

        if (!hasThread && (anotherGame || UI.getConnection().inGame().equals("yes"))) {
            refresher = new Refresher();
            refresher.start();
        }
        hasThread = true;

        if (!turnChecker && UI.getGame().getCurrentPlayer().getName().equals(UI.getAccount().getName())) {
            Label timer = (Label) ((NodeWrapper) menuButtons.getComponentByID("timer")).getValue();
            timer.setText(TIME_LIMIT + "");
            turnChecker = true;
        }

        new Thread(() -> {
            synchronized (this) {
                if (showCutSceneMode) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            Platform.runLater(() -> {
                try {
                    stopAnimations();
                    removeComponent(selectedCardInfo);
                    if (collectibleComponents != null) {
                        removeComponent(collectibleComponents);
                        collectibleComponents = null;
                    }

                    draggedComponenet = null;
                    draggedCardName = null;
                    String firstPlayerName = null, secondPlayerName = null, firstPlayerMana = null, secondPlayerMana = null;
                    String firstPlayerDeckCapacity = null;
                    ArrayList<String> handCardNames = new ArrayList<>();
                    ArrayList<String> handCardManaCosts = new ArrayList<>();
                    String playerOneUsableItemName = null, playerTwoUsableItemName = null;
                    gridStrings = new String[Map.NUMBER_OF_ROWS][Map.NUMBER_OF_COLUMNS];
                    int turnNumber = 0;

                    String[] shengdeShow;
                    if (!anotherGame)
                        shengdeShow = getUIOutputAsString("shengdebao").split("\\n");
                    else shengdeShow = UI.getConnection().showGame(otherGame).split("\\n");

                    for (int i = 0; i < shengdeShow.length; i++) {
                        shengdeShow[i] = shengdeShow[i].trim();
                        if (i == 0) {
                            turnNumber = Integer.parseInt(shengdeShow[i].replaceFirst("Turn number: ", ""));
                        } else if (i == 1) {
                            Pattern pattern = Pattern.compile("(.+) Mana\\((\\d+)\\) deck\\((\\d+)\\) hand:.*");
                            Matcher matcher = pattern.matcher(shengdeShow[i]);
                            if (matcher.find()) {
                                firstPlayerName = matcher.group(1);
                                firstPlayerMana = matcher.group(2);
                                firstPlayerDeckCapacity = matcher.group(3);
                            }
                        } else if (i == 2) {
                            Pattern pattern = Pattern.compile("([a-zA-Z0-9]+)\\((\\d+)\\)");
                            Matcher matcher = pattern.matcher(shengdeShow[i]);
                            while (matcher.find()) {
                                handCardNames.add(matcher.group(1));
                                handCardManaCosts.add(matcher.group(2));
                            }
                        } else if (i == 3) {
                            Pattern pattern = Pattern.compile("Player 1 usable item is: (.+)");
                            Matcher matcher = pattern.matcher(shengdeShow[i]);
                            if (matcher.find()) {
                                String s = matcher.group(1);
                                if (!s.equals("No Items selected"))
                                    playerOneUsableItemName = s;
                            }
                        } else if (i == shengdeShow.length - 3) {
                            Pattern pattern = Pattern.compile("(.+) Mana\\((\\d+)\\) deck\\(\\d+\\) hand:.*");
                            Matcher matcher = pattern.matcher(shengdeShow[i]);
                            if (matcher.find()) {
                                secondPlayerName = matcher.group(1);
                                secondPlayerMana = matcher.group(2);
                            }
                        } else if (i == shengdeShow.length - 1) {
                            Pattern pattern = Pattern.compile("Player 2 usable item is: (.+)");
                            Matcher matcher = pattern.matcher(shengdeShow[i]);
                            if (matcher.find()) {
                                playerTwoUsableItemName = matcher.group(1);
                                if (playerTwoUsableItemName.equals("No Items selected"))
                                    playerTwoUsableItemName = null;
                            }
                        } else if (i < 4 + Map.NUMBER_OF_ROWS) {
                            String[] strings = shengdeShow[i].split(" +");
                            gridStrings[i - 4] = Arrays.copyOf(strings, gridStrings[i - 4].length);
                        }
                    }

                    if (everyThing != null)
                        removeComponent(everyThing);
                    everyThing = new ComponentSet();

                    String friendlyHeroName = getFriendlyHeroName();
                    String opponentHeroName = getOpponentHeroName();
                    if (UI.getConnection().inGame().equals("yes")) {
                        JsonObject jsonObject = UI.getConnection().getGameInfo();
                        if (jsonObject.get("currentPlayer").getAsString().equals(firstPlayerName)) {
                            firstPlayerBar = makePlayerStat(firstPlayerName, friendlyHeroName, playerOneUsableItemName, firstPlayerMana, Math.max(Integer.parseInt(firstPlayerMana), Math.min(9, (turnNumber + 1) / 2 + 2)));
                            secondPlayerBar = makeReverseEmptyPlayerStat(secondPlayerName, opponentHeroName, playerTwoUsableItemName);
                        } else {
                            firstPlayerBar = makeEmptyPlayerStat(firstPlayerName, friendlyHeroName, playerOneUsableItemName);
                            secondPlayerBar = makeReversePlayerStat(secondPlayerName, opponentHeroName, playerTwoUsableItemName, secondPlayerMana, Math.max(Integer.parseInt(secondPlayerMana), Math.min(9, (turnNumber + 1) / 2 + 1)));
                        }
                    } else {
                        if (turnNumber % 2 == 0) {
                            firstPlayerBar = makePlayerStat(firstPlayerName, friendlyHeroName, playerOneUsableItemName, firstPlayerMana, Math.max(Integer.parseInt(firstPlayerMana), Math.min(9, (turnNumber + 1) / 2 + 2)));
                            secondPlayerBar = makeReverseEmptyPlayerStat(secondPlayerName, opponentHeroName, playerTwoUsableItemName);
                        } else {
                            firstPlayerBar = makeEmptyPlayerStat(firstPlayerName, friendlyHeroName, playerOneUsableItemName);
                            secondPlayerBar = makeReversePlayerStat(secondPlayerName, opponentHeroName, playerTwoUsableItemName, secondPlayerMana, Math.max(Integer.parseInt(secondPlayerMana), Math.min(9, (turnNumber + 1) / 2 + 1)));
                        }
                    }
                    cardBar = makeCardBar(firstPlayerDeckCapacity, handCardNames, handCardManaCosts);
                    gridCells = makeGridCells(gridStrings);
                    everyThing.addMenuComponent(firstPlayerBar, "first_player_bar");
                    everyThing.addMenuComponent(secondPlayerBar, "second_player_bar");
                    everyThing.addMenuComponent(cardBar, "card_bar");
                    everyThing.addMenuComponent(gridCells, "grid_cells");

                    try {
                        everyThing.setLabelFromLocalDir("fonts/averta-black-webfont.ttf");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    addComponent(everyThing);
                    if (isRecording) {
                        ArrayList<Node> nodes = new ArrayList<>(getView().getGroup().getChildrenUnmodifiable());
                        if (history.isEmpty() || !nodes.equals(history.get(history.size() - 1))) {
                            history.add(new ArrayList<>(getView().getGroup().getChildrenUnmodifiable()));
                        }
                    }
                } catch (Throwable ignored) {

                }
            });

        }).start();


        handleDeathAnimation();
    }

    private void stopAnimations() {
        for (AnimationTimer animation : animations) {
            animation.stop();
        }
        animations.clear();
    }

    private String getFriendlyHeroName() {
        for (int i = 0; i < Map.NUMBER_OF_ROWS; i++)
            for (int j = 0; j < Map.NUMBER_OF_COLUMNS; j++) {
                String s = getFriendlyCardID(i, j);
                if (s != null) {
                    s = getNameFromID(s);
                    if (NamesAndTypes.getType(s).equals("hero"))
                        return s;
                }
            }
        return null;
    }

    private String getOpponentHeroName() {
        for (int i = 0; i < Map.NUMBER_OF_ROWS; i++)
            for (int j = 0; j < Map.NUMBER_OF_COLUMNS; j++) {
                String s = getEnemyCardID(i, j);
                if (s != null) {
                    s = getNameFromID(s);
                    if (NamesAndTypes.getType(s).equals("hero"))
                        return s;
                }
            }
        return null;
    }

    private String getDescriptionByName(String cardName, String command) {
        String[] output = getUIOutputAsString(command).split("\\n");
        for (int i = 0; i < output.length; i++) {
            if (output[i].contains(cardName)) {
                return output[i];
            }
        }
        return null;
    }

    private String getDescriptionByName(String cardName) {
        String s = getDescriptionByName(cardName, "show my minions");
        if (s == null)
            s = getDescriptionByName(cardName, "show opponent minions");
        return s;
    }

    private String getDescriptionByLocation(int row, int column, String command) {
        String[] output = getUIOutputAsString(command).split("\\n");
        for (int i = 0; i < output.length; i++) {
            if (output[i].contains("location: (" + (row + 1) + ", " + (column + 1) + ")")) {
                return output[i];
            }
        }
        return null;
    }

    private String getDescriptionByLocation(int row, int column) {
        String s = getDescriptionByLocation(row, column, "show my minions");
        if (s == null)
            s = getDescriptionByLocation(row, column, "show opponent minions");
        return s;
    }

    private String getCardIDByLocation(int row, int column, String command) {
        String s = getDescriptionByLocation(row, column, command);
        if (s == null)
            return null;
        return s.replaceAll(":.*", "").trim();
    }

    private String getCardIDByLocation(int row, int column) {
        String s = getCardIDByLocation(row, column, "show my minions");
        if (s == null)
            s = getCardIDByLocation(row, column, "show opponent minions");
        return s;
    }

    private boolean hasFriendly(int row, int column) {
        return getFriendlyCardID(row, column) != null;
    }

    private boolean hasEnemy(int row, int column) {
        return getEnemyCardID(row, column) != null;
    }

    private String getFriendlyCardID(int row, int column) {
        return getCardIDByLocation(row, column, "show my minions");
    }

    private String getEnemyCardID(int row, int column) {
        return getCardIDByLocation(row, column, "show opponent minions");
    }

    private ComponentSet makeReverseEmptyPlayerStat(String playerName, String heroName, String usableItemName) {
        ComponentSet statBar = makeEmptyPlayerStat(playerName, heroName, usableItemName);
        statBar.reflectVertically();
        statBar.relocateUpRight(windowWidth - 290, 30);
        return statBar;
    }

    private ComponentSet makeReversePlayerStat(String playerName, String heroName, String usableItemName, String mana, int manaCapacity) {
        ComponentSet statBar = makePlayerStat(playerName, heroName, usableItemName, mana, manaCapacity);
        statBar.reflectVertically();
        statBar.relocateUpRight(windowWidth - 290, 30);
        return statBar;
    }

    private ComponentSet makeEmptyPlayerStat(String playerName, String heroName, String usableItemName) {
        ComponentSet statBar = makePlayerStat(playerName, heroName, usableItemName, "1", 1);
        statBar.turnBlackAndWhite();
        return statBar;
    }

    private ComponentSet makePlayerStat(String playerName, String heroName, String usableItemName, String mana, int manaCapacity) {
        ComponentSet statBar = new ComponentSet();

        ImageView backGround = new ImageView(Assets.getImage("images/gameIcons/Hero/hero_background.png"));
        backGround.setFitHeight(55);
        backGround.setFitWidth(55);
        statBar.addMenuComponent(new NodeWrapper(backGround));
        ImageView heroIcon = getImageViewByCardName(heroName, "idle", "gif");
        heroIcon.setFitWidth(55);
        heroIcon.setFitHeight(70);
        heroIcon.relocate(heroIcon.getLayoutX(), heroIcon.getLayoutY() - 20);
        statBar.addMenuComponent(new NodeWrapper(heroIcon));


        Label playerNameLabel = new Label(playerName);
        playerNameLabel.relocate(50 + 10, 5);
        playerNameLabel.setTextFill(textColor);
        statBar.addMenuComponent(new NodeWrapper(playerNameLabel));


        ImageView usableBackground = new ImageView(Assets.getImage("images/gameIcons/Hero/usable_background.png"));
        usableBackground.setFitHeight(35);
        usableBackground.setFitWidth(35);
        usableBackground.relocate(45, 57);
        statBar.addMenuComponent(new NodeWrapper(usableBackground));

        if (usableItemName != null) {
            ImageView usable = getImageViewByCardName(usableItemName, "actionbar", "gif");
            usable.setOnMouseClicked(e -> showCardInfo(usableItemName));
            usable.setFitHeight(35);
            usable.setFitWidth(35);
            usable.relocate(45, 57);
            statBar.addMenuComponent(new NodeWrapper(usable));
        }


        {
//            Label label = new Label("N");
//            label.relocate(0, 57);
//            statBar.addMenuComponent(new NodeWrapper(label));
        }
        ComponentSet manaBar = makeManaBar(Integer.parseInt(mana), manaCapacity);
        manaBar.resize(0.85, 0.85);
        statBar.addMenuComponent(manaBar, "ManaBar");
        ((ComponentSet) statBar.getComponentByID("ManaBar")).relocate(50 + 10, 30);
        statBar.relocate(30, 30);
        statBar.resize(2.2, 2.2);
        return statBar;
    }

    private Image getImageByCardName(String cardName, String state, String format) {
        Image image = null;
        image = Assets.getImage("images/gameIcons/gifs/" + NamesAndTypes.getType(cardName)
                + "/" + cardName + "/" + state + "." + format);

        return image;
    }

    private ImageView getImageViewByCardName(String cardName, String state, String format) {
        ImageView imageView = null;
        imageView = new ImageView(Assets.getImage("images/gameIcons/gifs/" + NamesAndTypes.getType(cardName)
                + "/" + cardName + "/" + state + "." + format));

        return imageView;
    }

    private ComponentSet makeCellFromString(String[][] gridStrings, int i, int j) {
        Pattern pattern = Pattern.compile("(\\[?)((\\+|-|\\()?)(.*)((\\+|-|\\))?)(]?):(-?\\d+)\\!(-?\\d+)\\?(-?\\d+)");
        Matcher matcher = pattern.matcher(gridStrings[i][j]);

        boolean isFriendly = false, isEnemy = false;
        String contentCardName = ".";
        int numberOfFlags = 0, poisonEffect = 0, hpEffect = 0;
        if (matcher.find()) {
            isFriendly = matcher.group(2).equals("+");
            isEnemy = matcher.group(2).equals("-");
            contentCardName = matcher.group(4);
            numberOfFlags = Integer.parseInt(matcher.group(8));
            poisonEffect = Integer.parseInt(matcher.group(9));
            hpEffect = Integer.parseInt(matcher.group(10));
        }
        contentCardName = contentCardName.replaceAll("(\\[|]|-|\\+|\\(|\\))", "");
        return makeCellContent(i, j, isFriendly, isEnemy, contentCardName, numberOfFlags, poisonEffect, hpEffect);
    }

    private ComponentSet makeGridCells(String[][] gridStrings) {
        ComponentSet grid = new ComponentSet();
        for (int i = 0; i < gridStrings.length; i++)
            for (int j = 0; j < gridStrings[i].length; j++) {
                ComponentSet cell_content = makeCellFromString(gridStrings, i, j);
                grid.addMenuComponent(cell_content, i + "," + j);
            }


        for (int i = 0; i < Map.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < Map.NUMBER_OF_COLUMNS; j++) {
                addCellInteractor(i, j, (ComponentSet) grid.getComponentByID(i + "," + j));
            }
        }

        grid.relocate(210, 200);
        grid.resize(1.8, 2);

        getView().getScene().setOnDragDetected(event -> {
            getView().getScene().startFullDrag();
        });

        getView().getScene().setOnMouseDragReleased(event -> {
        });

        for (int i = 0; i < gridStrings.length; i++)
            for (int j = 0; j < gridStrings[i].length; j++) {
                ComponentSet cell = (ComponentSet) grid.getComponentByID(i + "," + j);
                ImageView interactor = (ImageView) ((NodeWrapper) cell.getComponentByID("interactor")).getValue();
                ImageView tile = (ImageView) ((NodeWrapper) cell.getComponentByID("tile")).getValue();
                interactor.setOnMouseDragEntered(e -> tile.setOpacity(1));
                interactor.setOnMouseDragExited(e -> tile.setOpacity(0.1));
                int finalRow = i, finalColumn = j;
                interactor.setOnMouseDragReleased(e -> handleInsertCard(finalRow, finalColumn, false));

            }
        return grid;
    }

    private ComponentSet makeCellContent(int i, int j, boolean isFriendly, boolean isEnemy, String contentCardName, int numberOfFlags, int poisonEffect, int hpEffect) {
        ComponentSet cell = new ComponentSet();
        ImageView tile = null;
        tile = new ImageView(Assets.getImage("images/gameIcons/Cells/tile_normal.png"));
        tile.setFitWidth(TILE_WITDH);
        tile.setFitHeight(TILE_HEIGHT);
        tile.relocate(j * 50, i * 30);
        tile.setOpacity(0.1);
        cell.addMenuComponent(new NodeWrapper(tile), "tile");


        if (poisonEffect != 0 && tile != null) {

            ImageView effect = new ImageView(Assets.getImage("images/gameIcons/Cells/poison_cell.gif"));
            effect.setFitWidth(50);
            effect.setFitHeight(30);
            effect.setOpacity(0.5);
            effect.relocate(j * 50, i * 30);
            cell.addMenuComponent(new NodeWrapper(effect));


            Label label = new Label(poisonEffect + "");
            label.setTextFill(Color.GREENYELLOW);
            label.relocate(j * 50, i * 30);
            cell.addMenuComponent(new NodeWrapper(label));
            startEffectCellAnimation(label);


        }

        if (hpEffect != 0 && tile != null) {

            ImageView effect = new ImageView(Assets.getImage("images/gameIcons/Cells/hp_cell.gif"));
            effect.setFitWidth(50);
            effect.setFitHeight(30);
            effect.relocate(j * 50, i * 30);
            cell.addMenuComponent(new NodeWrapper(effect));


            Label label = new Label(hpEffect + "");
            label.setTextFill(Color.RED);
            label.relocate(j * 50 + 30, i * 30);
            cell.addMenuComponent(new NodeWrapper(label));
            startEffectCellAnimation(label);

        }

        if (numberOfFlags != 0) {

            ImageView flag = new ImageView(Assets.getImage("images/gameIcons/Cells/flag.gif"));
            flag.setFitHeight(30);
            flag.setFitWidth(50);
            flag.relocate(j * 50, i * 30);
            cell.addMenuComponent(new NodeWrapper(flag));

            Label label = new Label(numberOfFlags + "");
            label.relocate(j * 50 + 20, i * 30 + 5);
            label.setFont(new Font(12));
            cell.addMenuComponent(new NodeWrapper(label));
        }
        ImageView background = null;
        if (isFriendly) {
            background = new ImageView(Assets.getImage("images/gameIcons/Cells/friendly_background.png"));
        }

        if (isEnemy) {
            background = new ImageView(Assets.getImage("images/gameIcons/Cells/opponent_background.png"));
        }

        if (background != null) {
            background.setFitWidth(50);
            background.setFitHeight(30);
            background.relocate(j * 50, i * 30);
            cell.addMenuComponent(new NodeWrapper(background), "friendliness");
        }


        if (contentCardName != null && !contentCardName.equals(".")) {
            String type = NamesAndTypes.getType(contentCardName);
            String state = "idle";
            if (type.equals("spellCard") || type.equals("usable") || type.equals("collectible")) {
                state = "actionbar";
            }
            ImageView card = getImageViewByCardName(contentCardName, state, "gif");
            double height = CELL_CONTENT_HEIGHT, width = CELL_CONTENT_WIDTH;
            double x = j * 50 + TILE_WITDH / 2 - width / 2, y = i * 30 + TILE_HEIGHT / 2 - height / 2 - height / 5;
            if (!(NamesAndTypes.getCollectionItem(contentCardName) instanceof Unit)) {
                height -= 40;
                width -= 40;
                y += 2 + TILE_HEIGHT;
                x += 17;
            }
            card.setFitHeight(height);
            card.setFitWidth(width);
            card.relocate(x, y);
            cell.addMenuComponent(new NodeWrapper(card), "card_content");
            addUnitBuffsToCell(i, j, cell);
        }

        return cell;
    }

    private void addUnitBuffsToCell(int row, int column, ComponentSet cell) {
        String[] buffNamesInDescription = {"holy", "unholy", "powerBuff", "weaknessBuff", "stun", "disarm"};
        double centreY = row * TILE_HEIGHT + TILE_HEIGHT / 2;
        double centreX = column * TILE_WITDH + TILE_WITDH / 2;
        double radius = 15;
        double angleRange = Math.PI;
        double angleForOne = Math.PI / buffNamesInDescription.length;

        String desc = getDescriptionByLocation(row, column);
        if (desc == null)
            return;
        int cnt = 0;
        for (String buffName : buffNamesInDescription) {
            if (desc.contains(buffName)) {
                ImageView buffView = new ImageView(Assets.getImage("images/gameIcons/Cells/" + buffName + "_buff.png"));
                buffView.setFitWidth(BUFF_WIDTH);
                buffView.setFitHeight(BUFF_HEIGHT);
                buffView.relocate(centreX + Math.cos(angleForOne * cnt) * radius,
                        centreY - Math.sin(angleForOne * cnt) * radius);
                cell.addMenuComponent(new NodeWrapper(buffView));
                cnt++;

            }
        }
    }

    private void startEffectCellAnimation(Node node) {
        AnimationTimer animationTimer = new AnimationTimer() {
            long maxAnimationTime = Long.MAX_VALUE - 1000;
            long first = -1;
            long previous;
            double goalUpY = 60.0;
            long maxGoUpTime = 1000L * 1000 * 1000 * 3;
            double movedUp = 0;

            @Override
            public void handle(long now) {
                if (first == -1)
                    first = now;
                long delta = now - previous;
                previous = now;
                now -= first;

                if (now > maxAnimationTime)
                    stop();

                if (now <= maxGoUpTime) {
                    node.relocate(node.getLayoutX(), node.getLayoutY() - goalUpY * delta / maxGoUpTime);
                    movedUp += goalUpY * delta / maxGoUpTime;
                    node.setOpacity(node.getOpacity() - 0.0005);
                } else {
                    first += now;
                    node.relocate(node.getLayoutX(), node.getLayoutY() + movedUp);
                    movedUp = 0;
                    node.setOpacity(1);
                }
            }
        };
        addAnimation(animationTimer);
    }

    private void addAnimation(AnimationTimer animationTimer) {
        animations.add(animationTimer);
        animationTimer.start();
    }

    private void addCellInteractor(int i, int j, ComponentSet cell) {

        ImageView interactor = new ImageView(Assets.getImage("images/gameIcons/Cells/active_cell.png"));

        interactor.setFitWidth(50);
        interactor.setFitHeight(30);
        interactor.relocate(j * 50, i * 30);
        interactor.setOpacity(0);

        interactor.setOnMouseEntered(e -> ((NodeWrapper) cell.getComponentByID("tile")).getValue().setOpacity(0.5));
        interactor.setOnMouseExited(e -> ((NodeWrapper) cell.getComponentByID("tile")).getValue().setOpacity(0.1));
        interactor.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.MIDDLE) {
                String cardName = getNameFromID(getCardIDByLocation(i, j));
                if (cardName != null) {
                    showCardInfo(cardName);
                }
                return;
            }
            String cardID = getFriendlyCardID(i, j);
            if (cardID != null) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    handleSelectCard(cardID);
                }
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    if (UI.getGame().getCurrentPlayer().getUnit(cardID).canCombo()) {
                        selectedComboCardIds.clear();
                        selectedComboCardIds.add(cardID);
                        (new NodeWrapper(getInteractor(i, j))).disableMouseEvents();
                        getTile(i, j).setOpacity(1);
                        comboGridChange();
                    }
                }

            }
        });

        cell.addMenuComponent(new NodeWrapper(interactor), "interactor");


    }

    private void showCardInfo(String cardName) {
        if (selectedCardInfo != null) {
            removeComponent(selectedCardInfo);
            selectedCardInfo = null;
        }
        CardView cardView = new CardView(NamesAndTypes.getCollectionItem(cardName));
        if (getDescriptionByName(cardName) != null) {
            Pattern pattern = Pattern.compile(".*health *: *(\\d+).*power *: *(\\d+).*");
            Matcher matcher = pattern.matcher(getDescriptionByName(cardName));
            if (matcher.find()) {
                cardView.setHp(Integer.parseInt(matcher.group(1)));
                cardView.setAp(Integer.parseInt(matcher.group(2)));
            }
        }
        cardView.relocate(windowWidth - 220 - 70, windowHeight - 500);
        selectedCardInfo = cardView;
        addComponent(cardView);
        moveComponentToFront(gridCells);
        moveComponentToFront(firstPlayerBar);
        moveComponentToFront(secondPlayerBar);
    }

    private String getInfoOfUnit(int row, int column) {
        String s;
        s = getDescriptionByLocation(row, column, "show my minions");
        if (s != null)
            return s;
        return getDescriptionByLocation(row, column, "show opponent minions");
    }

    private ComponentSet getCell(int row, int column) {
        return (ComponentSet) gridCells.getComponentByID(row + "," + column);
    }

    private void comboGridChange() {
        setGridColor(Color.ORANGE);
        for (int row = 0; row < Map.NUMBER_OF_ROWS; row++) {
            for (int column = 0; column < Map.NUMBER_OF_COLUMNS; column++) {
                ImageView interactor = getInteractor(row, column);

                int finalRow = row, finalColumn = column;
                interactor.setOnMouseClicked(e -> {
                    if (hasFriendly(finalRow, finalColumn)) {
                        (new NodeWrapper(getInteractor(finalRow, finalColumn))).disableMouseEvents();
                        getTile(finalRow, finalColumn).setOpacity(1);
                        selectedComboCardIds.add(getFriendlyCardID(finalRow, finalColumn));
                    }
                    if (hasEnemy(finalRow, finalColumn)) {
                        handleAttackCombo(getEnemyCardID(finalRow, finalColumn));
                    }
                });
            }
        }
    }

    ImageView getTile(int row, int column) {
        ComponentSet cell = (ComponentSet) gridCells.getComponentByID(row + "," + column);
        return (ImageView) ((NodeWrapper) cell.getComponentByID("tile")).getValue();
    }

    ImageView getInteractor(int row, int column) {
        ComponentSet cell = (ComponentSet) gridCells.getComponentByID(row + "," + column);
        return (ImageView) ((NodeWrapper) cell.getComponentByID("interactor")).getValue();
    }

    private void setGridColor(Color color) {
        for (int row = 0; row < Map.NUMBER_OF_ROWS; row++) {
            for (int column = 0; column < Map.NUMBER_OF_COLUMNS; column++) {
                ImageView tile = getTile(row, column);
                ImageView interactor = getInteractor(row, column);

                (new NodeWrapper(tile)).setColor(color);
                double opacity = tile.getOpacity();
                interactor.setOnMouseEntered(e -> tile.setOpacity(0.7));
                interactor.setOnMouseExited(e -> tile.setOpacity(opacity));
                interactor.setOnMouseDragEntered(e -> tile.setOpacity(0.7));
                interactor.setOnMouseDragReleased(e -> {
                });
                interactor.setOnMouseDragExited(e -> tile.setOpacity(opacity));
            }
        }
    }

    private void cardSelectedGridChangePrimary() {
        setGridColor(Color.BLUE);
        for (int row = 0; row < Map.NUMBER_OF_ROWS; row++) {
            for (int column = 0; column < Map.NUMBER_OF_COLUMNS; column++) {
                ImageView interactor = getInteractor(row, column);

                int finalRow = row, finalColumn = column;
                interactor.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        if (!hasFriendly(finalRow, finalColumn) && !hasEnemy(finalRow, finalColumn)) {

//                            if (Objects.equals(getCardIDByLocation(finalRow, finalColumn, "show my minions"), selectedCardID)) {
//                                refresh();
//                                moveUnit(selectedCardID, coordinations[0] - 1, coordinations[1] - 1, finalRow, finalColumn);
//                            } else {
//                                showPopUp(verdict);
//                            }
                            handleMoveCard(finalRow, finalColumn, false);
                        }
                        if (hasEnemy(finalRow, finalColumn)) {
                            handleAttackUnit(getEnemyCardID(finalRow, finalColumn), false);
                        }
                    }
                    if (e.getButton() == MouseButton.MIDDLE) {
                        if (hasEnemy(finalRow, finalColumn)) {
                            handleUseSpecialPower(finalRow, finalColumn, false);
                        }
                    }
                });
            }
        }
    }

    private ComponentSet makeCardBar(String firstPlayerDeckCapacity, ArrayList<String> handCardNames, ArrayList<String> handCardManaCosts) {

        int size = handCardNames.size();
        cardBar = new ComponentSet();
        for (int i = 0; i < 5; i++) {
            ComponentSet handCard = new ComponentSet();

            ImageView manaIcon = new ImageView(Assets.getImage("images/gameIcons/MenuIcons/icon_mana.png"));
            if (i >= size) {
                manaIcon.setImage(Assets.getImage("images/gameIcons/MenuIcons/icon_mana_inactive.png"));
            }
            manaIcon.setOpacity(0.7);
            manaIcon.setFitWidth(20);
            manaIcon.setFitHeight(20);
            manaIcon.relocate(i * 55 + 25 - 7, 50 + 1);
            handCard.addMenuComponent(new NodeWrapper(manaIcon));

            ImageView imageView = new ImageView(Assets.getImage("images/gameIcons/card_background.png"));
            imageView.setFitHeight(55);
            imageView.setFitWidth(55);
            imageView.relocate(i * 55, 0);


            ImageView innerGlow = new ImageView(Assets.getImage("images/gameIcons/card_background_inner_glow.png"));
            innerGlow.setFitWidth(55);
            innerGlow.setFitHeight(55);
            innerGlow.relocate(i * 55, 0);
            innerGlow.setOpacity(0.7);

            if (i < size) {


                String x = handCardManaCosts.get(i);
                Label label = new Label(x);
                label.relocate(i * 55 + 25 - 1, 50 + 3.8);
                label.setFont(new Font(12));
                label.setTextFill(Color.LIGHTCYAN);
                label.setOpacity(0.7);
                handCard.addMenuComponent(new NodeWrapper(label));

                String cardName = handCardNames.get(i);

                imageView.setOnMouseEntered(e -> {
                    imageView.setImage(Assets.getImage("images/gameIcons/cardbackground_highlight.png"));
                    innerGlow.setOpacity(1);
                    manaIcon.setOpacity(1);
                    label.setOpacity(1);

                });
                imageView.setOnMouseExited(e -> {
                    imageView.setImage(Assets.getImage("images/gameIcons/card_background.png"));
                    innerGlow.setOpacity(0.7);
                    manaIcon.setOpacity(0.7);
                    label.setOpacity(0.7);

                });

                handCard.addMenuComponent(new NodeWrapper(innerGlow));
                String type = NamesAndTypes.getType(handCardNames.get(i));
                String state = "idle";
                if (type.equals("spellCard") || type.equals("usable") || type.equals("collectible")) {
                    state = "actionbar";
                }
                ImageView cardContent = getImageViewByCardName(handCardNames.get(i), state, "gif");
                cardContent.setFitWidth(55);
                cardContent.setFitHeight(55);
                cardContent.relocate(i * 55, 0);
                handCard.addMenuComponent(new NodeWrapper(cardContent), "content");

            }
            handCard.addMenuComponent(new NodeWrapper(imageView), "background");

            ComponentSet handOptions = setHandOptions(firstPlayerDeckCapacity);
            handOptions.relocate(-80, -7);
            handOptions.resize(1.2, 1.2);
            cardBar.addMenuComponent(handOptions, "hand_options");

            cardBar.addMenuComponent(handCard, "card_" + i);
        }
        cardBar.resize(2.5, 2.5);
        cardBar.relocate(300 - 120 + 30, windowHeight - 200);


        // add card bar functionality:
        {
            for (int i = 0; i < size; i++) {
                String cardName = handCardNames.get(i);
                ComponentSet handCard = (ComponentSet) cardBar.getComponentByID("card_" + i);
                ImageView background = (ImageView) ((NodeWrapper) handCard.getComponentByID("background")).getValue();
                ImageView content = (ImageView) ((NodeWrapper) handCard.getComponentByID("content")).getValue();

                background.setOnMousePressed(mouseEvent -> {
                    removeComponent(new NodeWrapper(content));
                    String type = NamesAndTypes.getType(cardName);
                    String state = "idle";
                    if (type.equals("spellCard") || type.equals("usable") || type.equals("collectible")) {
                        state = "actionbar";
                    }
                    draggedComponenet = new NodeWrapper(getImageViewByCardName(cardName, state, "gif"));
                    ((NodeWrapper) draggedComponenet).getValue().relocate(mouseEvent.getSceneX() - 50,
                            mouseEvent.getSceneY() - 50);
                    ((ImageView) ((NodeWrapper) draggedComponenet).getValue()).setFitWidth(100);
                    ((ImageView) ((NodeWrapper) draggedComponenet).getValue()).setFitHeight(100);
                    draggedCardName = cardName;
                    addComponent(draggedComponenet);
                    everyThing.addMenuComponent(draggedComponenet);
                    for (int row = 0; row < Map.NUMBER_OF_ROWS; row++) {
                        for (int column = 0; column < Map.NUMBER_OF_COLUMNS; column++) {
                            ComponentSet cell = (ComponentSet) gridCells.getComponentByID(row + "," + column);
                            removeComponent(cell.getComponentByID("interactor"));
                            addComponent(cell.getComponentByID("interactor"));
                        }
                    }
                });

                background.setOnMouseDragged(mouseEvent -> {
                    ((NodeWrapper) draggedComponenet).getValue().relocate(mouseEvent.getSceneX() - 50, mouseEvent.getSceneY() - 50);
                });
                background.setOnMouseReleased(mouseEvent -> refresh());
            }
        }
        return cardBar;
    }
}
