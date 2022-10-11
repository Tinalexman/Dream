package dream.components.mesh;

public class MeshFactory
{

    public static void asPlane(Mesh mesh)
    {
        mesh.vertices = new float[]
        {
            0.5f, 0.5f, 0.0f, // top right
            0.5f, -0.5f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f // top left
        };

        mesh.indices = new int[]
        {
            0, 1, 3, // first triangle
            1, 2, 3 // second triangle
        };

        mesh.textures = new float[]
        {
           1.0f, 1.0f,
           1.0f, 0.0f,
           0.0f, 0.0f,
           0.0f, 1.0f
        };

        mesh.normals = new float[]
        {
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f
        };

        mesh.meshType = MeshType.Plane;
    }

    public static void asCube(Mesh mesh)
    {
        mesh.vertices = cubeVertices(0.0f, 0.0f, 0.0f, 0.5f);
        mesh.normals = cubeNormals();
        mesh.textures = cubeTextures();

        mesh.meshType = MeshType.Cube;
    }


    private static float[] cubeVertices(float x, float y, float z, float size)
    {
        return new float[]
        {
            x - size, y - size, z - size,
            x + size, y + size, z - size,
            x + size, y - size, z - size,
            x + size, y + size, z - size,
            x - size, y - size, z - size,
            x - size, y + size, z - size,

            x - size, y - size, z + size,
            x + size, y - size, z + size,
            x + size, y + size, z + size,
            x + size, y + size, z + size,
            x - size, y + size, z + size,
            x - size, y - size, z + size,

            x - size, y + size, z + size,
            x - size, y + size, z - size,
            x - size, y - size, z - size,
            x - size, y - size, z - size,
            x - size, y - size, z + size,
            x - size, y + size, z + size,

            x + size, y + size, z + size,
            x + size, y - size, z - size,
            x + size, y + size, z - size,
            x + size, y - size, z - size,
            x + size, y + size, z + size,
            x + size, y - size, z + size,

            x - size, y - size, z - size,
            x + size, y - size, z - size,
            x + size, y - size, z + size,
            x + size, y - size, z + size,
            x - size, y - size, z + size,
            x - size, y - size, z - size,

            x - size, y + size, z - size,
            x + size, y + size, z + size,
            x + size, y + size, z - size,
            x + size, y + size, z + size,
            x - size, y + size, z - size,
            x - size, y + size, z + size
        };
    }

    private static float[] cubeTextures()
    {
        return new float[]
        {
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,

            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,

            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,

            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,

            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            0.0f, 0.0f
        };
    }

    private static float[] cubeNormals()
    {
        return new float[]
        {
            // Back Face
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,

            // Front Face
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,

            //Left Face
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,

            // Right Face
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,

            //Bottom Face
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,

            //Top Face
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
        };
    }

    

}