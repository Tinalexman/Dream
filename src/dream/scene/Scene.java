package dream.scene;

import dream.node.Node;

public class Scene
{
    private Node root;

    public Scene(Node root)
    {
        this.root = root;
        root.start();
    }

    public Scene()
    {
        this(null);
    }

    public Node getRoot()
    {
        return this.root;
    }

    public void setRoot(Node node)
    {
        this.root = node;
    }

    public void addChild(Node parent, Node child)
    {
        if(parent != null)
        {
            if (this.root != null && !this.root.getChildren().contains(child))
                parent.addChild(child);
        }
    }
}
