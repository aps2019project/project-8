package view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sun.awt.im.InputMethodAdapter;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class NodeSet implements MenuComponent {
    double x, y;

    private ArrayList<Node> components = new ArrayList<>();

    public ArrayList<Node> getChildren() {
        return components;
    }

    public void addInGroup(Group group) {
        for (Node node : components)
            group.getChildren().add(node);
    }

    public void removeFromGroup(Group group) {
        for (Node node : components) {
            group.getChildren().remove(node);
        }
    }

    public void relocate(double x, double y) {
        for (int i = 0; i < components.size(); i++) {
            Node node = components.get(i);
            node.relocate((x - this.x) + node.getLayoutX(), (y - this.y) + node.getLayoutY());
        }
        this.x = x;
        this.y = y;
    }

    public void resize(double widthResizeFactor, double heightResizeFactor) {
        for (int i = 0; i < components.size(); i++) {
            Node node = components.get(i);
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
    }


    public void addNode(Node node) {
        components.add(node);
    }

    public void removeNode(Node node) {
        components.remove(node);
    }
}


