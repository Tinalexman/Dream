package dream.node;

import dream.components.Component;

import java.util.ArrayList;
import java.util.List;

public class Node2D implements Node
{
    protected String name;
    protected int ID;
    protected List<Node> children;

    public Node2D()
    {
        this.name = "Node2D";
        this.ID = 0;
        this.children = new ArrayList<>();
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public int getID()
    {
        return this.ID;
    }

    @Override
    public void setID(int ID)
    {
        this.ID = ID;
    }

    @Override
    public List<Node> getChildren()
    {
        return this.children;
    }

    @Override
    public void addChild(Node child)
    {
        this.children.add(child);
    }

    @Override
    public void removeChild(Node child)
    {
        this.children.remove(child);
    }

    @Override
    public List<Component> getComponents()
    {
        return null;
    }

    @Override
    public <T extends Component> T getComponent(Class<T> componentClass)
    {
        return null;
    }

    @Override
    public void addComponent(Component component)
    {

    }

    @Override
    public void removeComponent(Component component)
    {

    }
}
