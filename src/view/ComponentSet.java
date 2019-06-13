package view;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComponentSet implements MenuComponent {
    private double x, y;
    private double lazyX, lazyY;

    private transient ArrayList<MenuComponent> components = new ArrayList<>();

    private Map<String, Integer> map = new HashMap<>();

    public ArrayList<MenuComponent> getComponents() {
        return components;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    private double getMinWidth() {
        ArrayList<Double> widths = getComponentWidths();
        double minWidth = Double.MAX_VALUE;
        for (Double w : widths) {
            minWidth = Math.min(minWidth, w);
        }
        return minWidth;
    }

    private double getMaxWidth() {
        ArrayList<Double> widths = getComponentWidths();
        double maxWidth = Double.MIN_VALUE;
        for (Double w : widths) {
            maxWidth = Math.max(maxWidth, w);
        }
        return maxWidth;
    }

    public void relocateUpRight(double x, double y) {
        relocate(x - (getMaxWidth() - getMinWidth()), y);
    }

    public void setLabelFontStyle(String family) {
        for (MenuComponent component : components) {
            if (component instanceof NodeWrapper) {
                Node node = ((NodeWrapper) component).getValue();
                if (node instanceof Label) {
                    ((Label) node).setFont(Font.font(family, ((Label) node).getFont().getSize()));
                }
            }
            if (component instanceof ComponentSet) {
                ((ComponentSet) component).setLabelFontStyle(family);
            }
        }
    }

    public void relocate(double x, double y) {
        x += lazyX;
        y += lazyY;
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
                componentSet.relocate(x - this.x + componentSet.getX(), y - this.y + componentSet.getY());
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

                Method getFont = null, setFont = null;
                Method setFitHeight = null, setFitWidth = null, getFitHeight = null, getFitWidth = null;
                Method setHeight = null, getHeight = null, setWidth = null, getWidth = null;

                try {
                    getFont = node.getClass().getMethod("getFont");
                } catch (NoSuchMethodException e) {
                }

                try {
                    setFont = node.getClass().getMethod("setFont", Font.class);
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


                if (node instanceof Label) {

                }
                try {
                    if (getFont != null && setFont != null) {
                        Font font = (Font) getFont.invoke(node);
                        double size = font.getSize();
                        setFont.invoke(node, new Font(size * heightResizeFactor));
                    }
                    if (setFitHeight != null && setFitWidth != null && getFitHeight != null && getFitWidth != null) {
                        double previousFitHeight = (Double) getFitHeight.invoke(node);
                        double previousFitWidth = (Double) getFitWidth.invoke(node);
                        setFitHeight.invoke(node, previousFitHeight * heightResizeFactor);
                        setFitWidth.invoke(node, previousFitWidth * widthResizeFactor);
                    }
                    if (setWidth != null && setHeight != null && getWidth != null && getHeight != null) {
                        double previousHeight = (Double) getHeight.invoke(node);
                        double previousWidth = (Double) getWidth.invoke(node);
                        setHeight.invoke(node, previousHeight * heightResizeFactor);
                        setWidth.invoke(node, previousWidth * widthResizeFactor);
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
                componentSet.relocate((componentSet.getX() - x) * widthResizeFactor + x,
                        (componentSet.getY() - y) * heightResizeFactor + y);
                componentSet.resize(widthResizeFactor, heightResizeFactor);
            }
        }
    }

    private ArrayList<Double> getComponentWidths() {
        ArrayList<Double> widths = new ArrayList<>();
        for (MenuComponent component : components) {
            if (component instanceof NodeWrapper) {
                widths.add(((NodeWrapper) component).getValue().getLayoutX());
            }
            if (component instanceof GUIButton) {
                widths.add(((GUIButton) component).getX());
            }
            if (component instanceof ComponentSet) {
                widths.addAll(((ComponentSet) component).getComponentWidths());
            }
        }
        return widths;
    }

    public void reflectVertically() {
        double minWidth = getMinWidth(), maxWidth = getMaxWidth();
        double line = (maxWidth + minWidth) / 2 + 100;
        reflectVertically(line);
    }

    private void reflectVertically(double line) {
        for (MenuComponent component : components) {
            if (component instanceof NodeWrapper) {
                Node node = ((NodeWrapper) component).getValue();
                if (node instanceof ImageView) {
                    node.relocate(2 * line - (node.getLayoutX() + ((ImageView) node).getFitWidth()), node.getLayoutY());
                } else if (node instanceof Label) {
                    FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
                    double width = fontLoader.computeStringWidth(((Label) node).getText(), ((Label) node).getFont());
                    node.relocate(2 * line - (node.getLayoutX() + width), node.getLayoutY());
                } else {
                    node.relocate(line + (line - node.getLayoutX()), node.getLayoutY());
                }
            }
            if (component instanceof GUIButton) {
                GUIButton guiButton = (GUIButton) component;
                guiButton.setCentreX(2 * line - (guiButton.getX() + guiButton.getWidth() / 2));
            }
            if (component instanceof ComponentSet) {
                ((ComponentSet) component).reflectVertically(line);
            }
        }
    }

    public MenuComponent getComponentByID(String id) {
        if (!map.containsKey(id))
            return null;
        return components.get(map.get(id));
    }

    public void addMenuComponent(MenuComponent component, String id) {
        map.put(id, components.size());
        components.add(component);
    }

    public void addMenuComponent(MenuComponent component) {
        Integer t = components.size();
        while (map.containsKey(t.toString()))
            t++;
        addMenuComponent(component, t.toString());
    }

    public void removeMenuComponent(MenuComponent component) {
        components.remove(component);
    }

    public void setEffect(Effect effect) {
        for (MenuComponent component : components) {
            if (component instanceof NodeWrapper) {
                Node node = ((NodeWrapper) component).getValue();
                node.setEffect(effect);
            }
            if (component instanceof ComponentSet) {
                ((ComponentSet) component).setEffect(effect);
            }
        }
    }

    public void turnBlackAndWhite() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.3);
        setEffect(colorAdjust);
    }
}
