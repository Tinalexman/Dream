package dream.scene;

import dream.light.Light;
import dream.node.Node;

import java.util.HashMap;
import java.util.Map;

public class Scene
{
    private final Map<String, Node> nodeMap;

    public Scene(Node root)
    {
        this.nodeMap = new HashMap<>();
        this.nodeMap.put("Lights", new Node());
        this.nodeMap.put("Nodes", root);

        root.start();
    }

    public Scene()
    {
        this(new Node());
    }

    public Node getRoot()
    {
        return this.nodeMap.get("Nodes");
    }

   public Node getLights()
   {
       return this.nodeMap.get("Lights");
   }

    public void addChild(Node child)
    {
        Node root = this.nodeMap.get((child instanceof Light) ? "Lights" : "Nodes");
        if (!root.getChildren().contains(child))
            root.addChild(child);
    }
}
