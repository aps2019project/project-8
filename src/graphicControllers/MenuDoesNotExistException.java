package graphicControllers;

public class MenuDoesNotExistException extends Exception {
    public MenuDoesNotExistException() {super("Menu with this ID has not been created yet!");}
}
