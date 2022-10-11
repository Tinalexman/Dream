package dream.shape;

import dream.components.material.Material;
import dream.components.mesh.Mesh;
import dream.components.mesh.MeshFactory;
import dream.components.mesh.MeshRenderer;
import dream.components.transform.Transform;
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
    }

    public void setTexture(String textureName)
    {
        Material material;
        if((material = getComponent(Material.class)) == null)
        {
            material = new Material();
            addComponent(material);
        }

        material.getPack().diffuse = ResourcePool.addAndGetTexture(textureName);
    }

    @Override
    public void start()
    {
        super.addComponent(new Transform());
        super.addComponent(new Material());

        Mesh mesh = new Mesh();
        MeshFactory.asCube(mesh);
        super.addComponent(mesh);

        MeshRenderer renderer = new MeshRenderer();
        renderer.setMesh(mesh);
        super.addComponent(renderer);
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
