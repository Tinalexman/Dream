package dream.node;

import dream.components.Component;

import java.util.List;

public interface Node
{
    String getName();
    void setName(String name);

    int getID();
    void setID(int ID);

    List<Node> getChildren();
    void addChild(Node child);
    void removeChild(Node child);

    List<Component> getComponents();
    <T extends Component> T getComponent(Class<T> componentClass);
    void addComponent(Component component);
    void removeComponent(Component component);
}
