package dream.shape;

import dream.components.material.Material;
import dream.components.mesh.Mesh;
import dream.components.mesh.MeshFactory;
import dream.components.transform.Transform;
import dream.constants.MaterialConstants;
import dream.managers.ResourcePool;
import dream.node.Node;
import dream.node.drawable.Drawable;
import dream.shader.Shader;


public class Shape extends Node implements Drawable
{
    private Shader shader;

    public Shape()
    {
        super("Shape");

        super.addComponent(new Transform());
        Material material = new Material();
        material.getPack().diffuse = ResourcePool.addAndGetTexture("crate diffuse.png");
        super.addComponent(material);
        Mesh mesh = new Mesh();
        MeshFactory.asPlane(mesh);
        super.addComponent(mesh);
    }

    @Override
    public Shader getShader()
    {
        return this.shader;
    }

    @Override
    public void setShader(Shader shader)
    {
        this.shader = shader;
    }
}
