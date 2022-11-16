package dream.scene;

import dream.node.Node;

public class Scene
{
    private String name;
    private final Node root;

    public Scene(Node root)
    {
        this("Scene", root);
    }

    public Scene(String name, Node root)
    {
        this.name = name;
        this.root = root;
        this.root.start();
    }

    public Scene()
    {
        this("Scene", new Node());
    }

    public String name()
    {
        return this.name;
    }

    public void name(String name)
    {
        this.name = name;
    }

    public Node root()
    {
        return this.root;
    }

    public void add(Node child)
    {
        if (!this.root.getChildren().contains(child))
        {
            String className = child.getClass().getSimpleName();
            if(child.name().equalsIgnoreCase(className))
            {
                int ordinal = nextOrdinalFor(child.getClass());
                if(ordinal > 1)
                    child.name(child.name() + " " + ordinal);
            }
            this.root.addChild(child);
        }
    }

    public void remove(Node node)
    {
        this.root.getChildren().remove(node);
    }

    private int nextOrdinalFor(Class<? extends Node> object)
    {
        int count = 0;
        for(Node node : this.root.getChildren())
        {
            if(node.getClass().isAssignableFrom(object))
                ++count;
        }
        return count + 1;
    }
}
