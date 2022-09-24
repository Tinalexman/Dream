package dream.graphics.mesh;

import dream.util.buffer.BufferTools;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Mesh
{
    public static final int x_Axis = 1;
    public static final int y_Axis = 2;
    public static final int z_Axis = 3;


    private float[] vertices;
    private float[] textures;
    private float[] normals;
    private int[] indices;

    private final float size;
    private final int vaoID;

    public Mesh(float size, int orientation)
    {
        Mesh.setAsPlane(this, orientation);

        this.size = size;
        this.vaoID = glGenVertexArrays();

        create();
    }

    private void create()
    {
        glBindVertexArray(this.vaoID);

        if(this.vertices != null)
            createVBO(0, 3, this.vertices);
        if(this.textures  != null)
            createVBO(1, 2, this.textures);
        if(this.normals != null)
            createVBO(2, 3, this.normals);
        if(this.indices != null)
            createEBO(this.indices);

        stop();
    }

    public void start()
    {
        glBindVertexArray(this.vaoID);
        if(this.vertices != null)
            glEnableVertexAttribArray(0);
        if(this.textures != null)
            glEnableVertexAttribArray(1);
        if(this.normals != null)
            glEnableVertexAttribArray(2);
    }

    public int count()
    {
        return (this.indices == null) ? (this.vertices.length / 3) : this.indices.length;
    }

    public void stop()
    {
        if(this.vertices != null)
            glDisableVertexAttribArray(0);
        if(this.textures != null)
            glDisableVertexAttribArray(1);
        if(this.normals != null)
            glDisableVertexAttribArray(2);

        glBindVertexArray(0);
    }

    private void createEBO(int[] data)
    {
        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        IntBuffer buffer = BufferTools.createIntBuffer(data);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);
    }

    private void createVBO(int location, int size, float[] data)
    {
        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = BufferTools.createFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(location, size, GL_FLOAT, false, 0, 0);
        MemoryUtil.memFree(buffer);
    }

    private static float[] getPlaneMeshVertices(float x, float y, float z, float size, float orientation)
    {
        if(orientation == Mesh.x_Axis)
        {
            return new float[]
                    {
                            x, y + size,  z + size,
                            x, y + size, z - size,
                            x, y - size, z - size,
                            x, y - size, z + size
                    };
        }
        else if(orientation == Mesh.y_Axis)
        {
            return new float[]
                    {
                            x + size, y,  z + size, // forward right
                            x + size, y, z - size, // backward right
                            x - size, y, z - size, // backward left
                            x - size, y, z + size // forward left
                    };
        }
        else if(orientation == Mesh.z_Axis)
        {
            return new float[]
                    {
                            x + size, y + size,  z, // top right
                            x + size, y - size, z, // bottom right
                            x - size, y - size, z, // bottom left
                            x - size, y + size, z // top left
                    };
        }
        return null;
    }

    private static float[] getPlaneMeshTextures()
    {
        return new float[]
        {
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 1.0f
        };
    }

    private static float[] getPlaneMeshNormals()
    {
        return new float[]
        {
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f
        };
    }

    private static int[] getPlaneMeshIndices()
    {
        return new int[]
        {
                0, 1, 3,
                1, 2, 3
        };
    }

    private static void setAsPlane(Mesh mesh, int orientation)
    {
        mesh.vertices = getPlaneMeshVertices(0.0f, 0.0f, 0.0f, mesh.size, orientation);
        mesh.indices = getPlaneMeshIndices();
        mesh.textures = getPlaneMeshTextures();
        mesh.normals = getPlaneMeshNormals();
    }
}
