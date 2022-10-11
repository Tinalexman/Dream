package dream.components.mesh;

import dream.components.Component;

public class Mesh extends Component
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


}
