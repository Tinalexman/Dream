package dream.components.mesh;

public class Mesh
{
    protected float[] vertices;
    protected float[] textures;
    protected float[] normals;
    protected int[] indices;

    protected MeshType meshType;

    public Mesh()
    {
        this.vertices = new float[0];
        this.textures = new float[0];
        this.normals = new float[0];
        this.indices = new int[0];
    }

    public MeshType getMeshType()
    {
        return this.meshType;
    }
}
