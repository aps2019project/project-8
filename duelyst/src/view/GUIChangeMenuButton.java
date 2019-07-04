package view;


import javafx.event.EventHandler;

public class GUIChangeMenuButton extends GUIButton implements MenuChangeComponent {

    private int goalMenuID;
    private boolean ready = true;

    public GUIChangeMenuButton(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public int getGoalMenuID() {
        return goalMenuID;
    }

    @Override
    public void setGoalMenuID(int menuID) {
        this.goalMenuID = menuID;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public void setOnAction(EventHandler eventHandler) {
        button.setOnAction(eventHandler);
    }
}
