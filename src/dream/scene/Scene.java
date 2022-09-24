package dream.scene;

import dream.node.Node;

public class Scene
{
    private final Node root;

    public Scene(Node root)
    {
        this.root = root;
    }

    public Scene()
    {
        this(null);
    }

    public Node getRoot()
    {
        return this.root;
    }
}
