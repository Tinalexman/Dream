package dream.scene;

import dream.light.Light;
import dream.node.Node;

import java.util.ArrayList;
import java.util.List;

public class Scene
{
    private final Node root;
    private final List<Light> lights;
    public static final int maxLights = 5;

    public Scene(Node root)
    {
        this.root = root;
        this.lights = new ArrayList<>();
        root.start();
    }

    public Scene()
    {
        this(new Node());
    }

    public Node getRoot()
    {
        return this.root;
    }

   public List<Light> getLights()
   {
       return this.lights;
   }

   public void addLight(Light light)
   {
       if(this.lights.size() == maxLights)
           return;

       this.lights.add(light);
   }

   public void removeLight(Light light)
   {
       this.lights.remove(light);
   }

    public void addChild(Node child)
    {
        if (!this.root.getChildren().contains(child))
            this.root.addChild(child);
    }
}
