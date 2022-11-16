package dream.components.mesh;

public class MeshFactory
{
    public enum Orientation
    {
        xAxis,
        yAxis,
        zAxis
    }

    public static Mesh asCube()
    {
        Mesh mesh = new Mesh();
        mesh.vertices = cubeVertices(0.0f, 0.0f, 0.0f, 0.5f);
        mesh.normals = cubeNormals();
        mesh.textures = cubeTextures();

        int[] properties = mesh.properties();
        properties[0] = mesh.vertices.length;
        properties[1] = mesh.textures.length;
        properties[2] = mesh.normals.length;
        properties[3] = mesh.indices.length;

        return mesh;
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

    private static void set(float[] vertices, float[] normals, int index, float first, float second, Orientation orientation)
    {
        if(orientation == Orientation.xAxis)
        {
            vertices[index] = 0.0f;
            vertices[index + 1] = first;
            vertices[index + 2] = second;

            normals[index] = 1.0f;
            normals[index + 1] = 0.0f;
            normals[index + 2] = 0.0f;
        }
        else if(orientation == Orientation.yAxis)
        {
            vertices[index] = first;
            vertices[index + 1] = 0.0f;
            vertices[index + 2] = second;

            normals[index] = 0.0f;
            normals[index + 1] = 1.0f;
            normals[index + 2] = 0.0f;
        }
        else if(orientation == Orientation.zAxis)
        {
            vertices[index] = first;
            vertices[index + 1] = second;
            vertices[index + 2] = 0.0f;

            normals[index] = 0.0f;
            normals[index + 1] = 0.0f;
            normals[index + 2] = 1.0f;
        }
    }

    public static Mesh createPlane(int subdivisions, float size, Orientation orientation)
    {
        Mesh mesh = new Mesh();

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
                float first = x + (column * increment);
                float second = z + (row * increment);

                set(mesh.vertices, mesh.normals, vertexCount, first, second, orientation);

                mesh.textures[textureCount] = (float) column / subdivisions;
                mesh.textures[textureCount + 1] = (float) row / subdivisions;

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

        return mesh;
    }

    public static Mesh createPlane(int subdivisions, float size)
    {
        return createPlane(subdivisions, size, Orientation.yAxis);
    }
}