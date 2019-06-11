package view;

import javafx.scene.Group;
import javafx.scene.Node;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ComponentSet implements MenuComponent {
    double x, y;

    private transient ArrayList<MenuComponent> components = new ArrayList<>();

    public ArrayList<MenuComponent> getChildren() {
        return components;
    }

    public void addInGroup(Group group) {
        for (MenuComponent component : components) {
            if (component instanceof NodeWrapper)
                group.getChildren().add(((NodeWrapper) component).getValue());
            if (component instanceof GUIButton)
                ((GUIButton) component).addInGroup(group);
            if (component instanceof ComponentSet)
                ((ComponentSet) component).addInGroup(group);
        }
    }

    public void removeFromGroup(Group group) {
        for (MenuComponent component : components) {
            if (component instanceof NodeWrapper)
                group.getChildren().remove(((NodeWrapper) component).getValue());
            if (component instanceof GUIButton)
                ((GUIButton) component).removeFromGroup(group);
            if (component instanceof ComponentSet)
                ((ComponentSet) component).removeFromGroup(group);
        }
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
    public void relocate(double x, double y) {
        for (MenuComponent component : components) {
            if (component instanceof NodeWrapper) {
                Node node = ((NodeWrapper) component).getValue();
                node.relocate((x - this.x) + node.getLayoutX(), (y - this.y) + node.getLayoutY());
            }
            if (component instanceof GUIButton) {
                GUIButton guiButton = (GUIButton) component;
                guiButton.setX(x - this.x + guiButton.getX());
                guiButton.setY(y - this.y + guiButton.getY());
            }
            if (component instanceof ComponentSet) {
                ComponentSet componentSet = (ComponentSet) component;
                componentSet.relocate(x - this.x + getX(), y - this.x + getY());
            }
        }
        this.x = x;
        this.y = y;
    }

    public void resize(double widthResizeFactor, double heightResizeFactor) {
        for (MenuComponent component : components) {
            if (component instanceof NodeWrapper) {
                Node node = ((NodeWrapper) component).getValue();
                node.relocate((node.getLayoutX() - x) * widthResizeFactor + x, (node.getLayoutY() - y) * heightResizeFactor + y);

                Method getMinHeight = null, getMinWidth = null, setMinHeight = null, setMinWidth = null;
                Method setMaxHeight = null, setMaxWidth = null;
                Method setFitHeight = null, setFitWidth = null, getFitHeight = null, getFitWidth = null;
                Method setHeight = null, getHeight = null, setWidth = null, getWidth = null;

                try {
                    getMinHeight = node.getClass().getMethod("getMinHeight");
                } catch (NoSuchMethodException e) {

                }
                try {
                    getMinWidth = node.getClass().getMethod("getMinWidth");
                } catch (NoSuchMethodException e) {

                }
                try {
                    setMinHeight = node.getClass().getMethod("setMinHeight", double.class);
                } catch (NoSuchMethodException e) {

                }
                try {
                    setMinWidth = node.getClass().getMethod("setMinWidth", double.class);
                } catch (NoSuchMethodException e) {

                }
                try {
                    setMaxHeight = node.getClass().getMethod("setMaxHeight", double.class);
                } catch (NoSuchMethodException e) {

                }
                try {
                    setMaxWidth = node.getClass().getMethod("setMaxWidth", double.class);
                } catch (NoSuchMethodException e) {

                }
                try {
                    setFitHeight = node.getClass().getMethod("setFitHeight", double.class);
                } catch (NoSuchMethodException e) {

                }
                try {
                    setFitWidth = node.getClass().getMethod("setFitWidth", double.class);
                } catch (NoSuchMethodException e) {

                }
                try {
                    getFitHeight = node.getClass().getMethod("getFitHeight");
                } catch (NoSuchMethodException e) {

                }
                try {
                    getFitWidth = node.getClass().getMethod("getFitWidth");
                } catch (NoSuchMethodException e) {

                }
                try {
                    setHeight = node.getClass().getMethod("setHeight", double.class);
                } catch (NoSuchMethodException e) {

                }
                try {
                    setWidth = node.getClass().getMethod("setWidth", double.class);
                } catch (NoSuchMethodException e) {

                }
                try {
                    getHeight = node.getClass().getMethod("getHeight");
                } catch (NoSuchMethodException e) {
                }
                try {
                    getWidth = node.getClass().getMethod("getWidth");
                } catch (NoSuchMethodException e) {
                }


                try {
                    if (getMinHeight != null && getMinWidth != null && setMinHeight != null &&
                            setMaxHeight != null && setMaxWidth != null) {
                        double previousHeight = (Double) getMinHeight.invoke(node);
                        double previousWidth = (Double) getMinWidth.invoke(node);
                        setMinHeight.invoke(node, previousHeight * heightResizeFactor);
                        setMaxHeight.invoke(node, previousHeight * heightResizeFactor);
                        setMinWidth.invoke(node, previousWidth * widthResizeFactor);
                        setMaxWidth.invoke(node, previousWidth * widthResizeFactor);
                    }
                    if (setFitHeight != null && setFitWidth != null && getFitHeight != null && getFitWidth != null) {
                        double previousFitHeight = (Double) getFitHeight.invoke(node);
                        double previousFitWidth = (Double) getFitWidth.invoke(node);
                        setFitHeight.invoke(node, previousFitHeight * heightResizeFactor);
                        setFitWidth.invoke(node, previousFitWidth * widthResizeFactor);
                    }
                    if (setWidth != null && setHeight != null && getWidth != null && getHeight != null) {
                        System.err.println("YOYO");
                        double previousHeight = (Double) getHeight.invoke(node);
                        double previousWidth = (Double) getWidth.invoke(node);
                        System.err.println(previousHeight + " ---- " + previousWidth);
                        setHeight.invoke(node, previousHeight * heightResizeFactor);
                        setWidth.invoke(node, previousWidth * widthResizeFactor);
                        System.err.println((Double) getHeight.invoke(node) + " ==== " + (Double) getWidth.invoke(node));
                    }
                } catch (IllegalAccessException e) {

                } catch (InvocationTargetException e) {

                }
            }
            if (component instanceof GUIButton) {
                GUIButton guiButton = (GUIButton) component;
                guiButton.setX((guiButton.getX() - x) * widthResizeFactor + x);
                guiButton.setY((guiButton.getY() - y) * heightResizeFactor + y);
                guiButton.setWidth(guiButton.getWidth() * widthResizeFactor);
                guiButton.setHeight(guiButton.getHeight() * heightResizeFactor);
            }
            if (component instanceof ComponentSet) {
                ComponentSet componentSet = (ComponentSet) component;
                componentSet.resize(widthResizeFactor, heightResizeFactor);
            }
        }
    }


    public void addMenuComponent(MenuComponent component) {
        components.add(component);
    }

    public void removeMenuComponent(MenuComponent component) {
        components.remove(component);
    }
}
