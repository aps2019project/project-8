package view;

import javafx.scene.Node;

public class NodeWrapper implements MenuComponent {
    private Node value;

    public NodeWrapper(Node node) {this.value = node;}

    public Node getValue() {return this.value;}

    @Override
    public boolean equals(Object object) {
        if (object instanceof NodeWrapper)
            return value.equals(((NodeWrapper) object).getValue());
        if (object instanceof Node)
            return value.equals(object);
        return false;
    }
}
