package dream.node;

import dream.components.Component;

import java.util.ArrayList;
import java.util.List;

public class Node
{
    private static int counter = 0;

    protected String name;
    protected int ID;
    protected final List<Node> children;
    protected final List<Component> components;

    public Node(String name)
    {
        this.name = name;
        this.ID = Node.counter++;
        this.children = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    public Node()
    {
        this("Node");
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getID()
    {
        return this.ID;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public List<Component> getComponents()
    {
        return this.components;
    }

    public <T extends Component> T getComponent(Class<T> componentClass)
    {
        for(Component component : this.components)
        {
            if(componentClass.isAssignableFrom(component.getClass()))
            {
                try
                {
                    return componentClass.cast(component);
                }
                catch (ClassCastException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public void addComponent(Component component)
    {
        for(Component c : this.components)
        {
            if(c.getClass().isAssignableFrom(component.getClass()))
                return;
        }

        this.components.add(component);
    }

    public void start()
    {

    }

    public void stop()
    {

    }

    public void removeComponent(Component component)
    {
        this.components.remove(component);
    }

    public List<Node> getChildren()
    {
        return this.children;
    }

    public void addChild(Node child)
    {
        this.children.add(child);
        child.start();
    }

    public void removeChild(Node child)
    {
        this.children.remove(child);
    }

    public boolean hasChildren()
    {
        return this.children.size() > 0;
    }
}
