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

        mesh.create();
    }

}
