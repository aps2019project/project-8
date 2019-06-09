package graphicControllers;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import view.GraphicsView;
import view.MenuChangeComponent;
import view.MenuComponent;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    public static double windowDefaultWidth = 800;
    public static double windowDefaultHeight = 600;
    protected double windowHeight = windowDefaultHeight, windowWidth = windowDefaultWidth;
    private int id;
    private String title = "No Title!";
    private transient Menu parentMenu = null;
    private GraphicsView view = new GraphicsView(windowWidth, windowHeight, title);
    private Image backgroundImage = null;

    private ArrayList<MenuChangeComponent> menuChangeComponents = new ArrayList<>();
    private List<MenuComponent> menuComponents = new ArrayList<>();

    public Menu() {
        this(-1);
    }

    public Menu(int id) {
        this.id = id;
    }

    public Menu(int id, String title) {
        this.id = id;
        this.title = title;
        this.view = new GraphicsView(windowWidth, windowHeight, title);
    }

    public Menu(int id, String title, double width, double height) {
        this.id = id;
        this.title = title;
        this.windowWidth = width;
        this.windowHeight = height;
        this.view = new GraphicsView(width, height, title);
    }

    int getId() {
        return this.id;
    }

    protected void addComponent(MenuComponent component) {
        view.addComponent(component);
        menuComponents.add(component);
        if (component instanceof MenuChangeComponent)
            menuChangeComponents.add((MenuChangeComponent) component);
    }

    protected void removeComponent(MenuComponent component) {
        view.removeComponent(component);
        menuComponents.remove(component);
        if (component instanceof MenuChangeComponent)
            menuChangeComponents.remove(component);
    }

    protected void setKeyPressEvent(EventHandler<KeyEvent> eventHandler) {
        getView().getScene().setOnKeyPressed(eventHandler);
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
    }

    public void refresh() {}
}
