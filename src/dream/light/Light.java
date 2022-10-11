package dream.light;

import dream.node.Node;
import org.joml.Vector3f;

public class Light extends Node
{
    protected Vector3f position;
    protected Vector3f color;

    public Light()
    {
        super("Light");
        this.position = new Vector3f();
        this.color = new Vector3f(1.0f);
    }
}
