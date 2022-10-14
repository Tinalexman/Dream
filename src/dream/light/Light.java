package dream.light;

import org.joml.Vector3f;

public class Light
{
    public final Vector3f position;
    public final Vector3f ambient;
    public final Vector3f diffuse;
    public final Vector3f specular;
    public boolean active;

    public Light()
    {
        this.position = new Vector3f();
        this.ambient = new Vector3f(0.2f);
        this.diffuse = new Vector3f(0.5f);
        this.specular = new Vector3f(1.0f);
        this.active = true;
    }
}
