package view;

import javafx.event.EventHandler;

public interface MenuChangeComponent extends MenuComponent {
    int getGoalMenuID();
    void setGoalMenuID(int menuID);
    boolean isReady();
    void setReady(boolean ready);
    void setOnAction(EventHandler eventHandler);
}
