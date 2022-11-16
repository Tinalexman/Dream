package dream.components.mesh;

public class Mesh
{
    protected float[] vertices;
    protected float[] textures;
    protected float[] normals;
    protected int[] indices;

    protected final int[] properties;

    public Mesh()
    {
        this.vertices = new float[0];
        this.textures = new float[0];
        this.normals = new float[0];
        this.indices = new int[0];
        this.properties = new int[] {0, 0, 0, 0};
    }

    public boolean hasIndices()
    {
        return this.indices.length > 0;
    }

    public int count()
    {
        return (this.indices.length == 0) ?
                (this.vertices.length / 3) : this.indices.length;
    }

    public int[] properties()
    {
        return this.properties;
    }

    public float[] getVertices()
    {
        return this.vertices;
    }

    public float[] getTextures()
    {
        return this.textures;
    }

    public float[] getNormals()
    {
        return this.normals;
    }

    public int[] getIndices()
    {
        return this.indices;
    }

    public void setVertices(float[] vertices)
    {
        this.vertices = vertices;
    }

    public void setTextures(float[] textures)
    {
        this.textures = textures;
    }

    public void setNormals(float[] normals)
    {
        this.normals = normals;
    }

    public void setIndices(int[] indices)
    {
        this.indices = indices;
    }
}
