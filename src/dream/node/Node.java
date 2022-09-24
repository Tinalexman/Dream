package dream.node;


import java.util.ArrayList;
import java.util.List;

public class Node
{
    protected String name;
    protected final int ID;
    protected final List<Node> children;

    public Node(String name, int ID)
    {
        this.name = name;
        this.ID = ID;
        this.children = new ArrayList<>();
    }

    public Node(int ID)
    {
        this("Node", ID);
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
