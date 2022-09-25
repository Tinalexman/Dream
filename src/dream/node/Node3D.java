package dream.node;

import dream.components.Component;

import java.util.ArrayList;
import java.util.List;

public class Node3D implements Node
{
    protected String name;
    protected int ID;
    protected List<Node> children;
    protected List<Component> components;

    public Node3D(String name)
    {
        this.name = name;
        this.ID = 0;
        this.children = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    public Node3D()
    {
        this("Node3D");
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
        return this.components;
    }

    @Override
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

    @Override
    public void addComponent(Component component)
    {
        this.components.add(component);
    }

    @Override
    public void removeComponent(Component component)
    {
        this.components.remove(component);
    }
}
