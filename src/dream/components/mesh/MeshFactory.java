package dream.components.mesh;

public class MeshFactory
{

    public static void asCube(Mesh mesh)
    {
        mesh.vertices = cubeVertices(0.0f, 0.0f, 0.0f, 0.5f);
        mesh.normals = cubeNormals();
        mesh.textures = cubeTextures();

        int[] properties = mesh.properties();
        properties[0] = mesh.vertices.length;
        properties[1] = mesh.textures.length;
        properties[2] = mesh.normals.length;
        properties[3] = mesh.indices.length;
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

    public static void createPlane(Mesh mesh, int subdivisions, float size)
    {
        float increment = (size * 2.0f) / subdivisions;
        float x = -size, z = -size;

        int vertexCount = 0;
        int textureCount = 0;
        int indexCount = 0;
        int value = subdivisions + 1;
        int numberOfVertices = value * value;

        mesh.vertices = new float[3 * numberOfVertices];
        mesh.textures = new float[2 * numberOfVertices];
        mesh.normals = new float[3 * numberOfVertices];
        mesh.indices = new int[6 * subdivisions * subdivisions];

        for(int row = 0; row < (subdivisions + 1); row++)
        {
            for(int column = 0; column < (subdivisions + 1); column++)
            {
                mesh.vertices[vertexCount] = x + (column * increment);
                mesh.vertices[vertexCount + 1] = 0.0f;
                mesh.vertices[vertexCount + 2] = z + (row * increment);

                mesh.textures[textureCount] = (float) column / subdivisions;
                mesh.textures[textureCount + 1] = (float) row / subdivisions;

                mesh.normals[vertexCount] = 0.0f;
                mesh.normals[vertexCount + 1] = 1.0f;
                mesh.normals[vertexCount + 2] = 0.0f;

                if(row < subdivisions && column < subdivisions)
                {
                    mesh.indices[indexCount] = column + (row * value);
                    mesh.indices[indexCount + 1] = column + ((row + 1) * value);
                    mesh.indices[indexCount + 2] = (column + 1) + (row * value);

                    mesh.indices[indexCount + 3] = (column + 1) + (row * value);
                    mesh.indices[indexCount + 4] = column + ((row + 1) * value);
                    mesh.indices[indexCount + 5] = (column + 1) + ((row + 1) * value);

                    indexCount += 6;
                }

                vertexCount += 3;
                textureCount += 2;
            }
        }

        int[] properties = mesh.properties();
        properties[0] = mesh.vertices.length;
        properties[1] = mesh.textures.length;
        properties[2] = mesh.normals.length;
        properties[3] = mesh.indices.length;
    }

}