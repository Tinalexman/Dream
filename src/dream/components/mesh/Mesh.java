package dream.components.mesh;

import dream.components.Component;
import dream.util.buffer.BufferTools;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Mesh extends Component
{
    protected float[] vertices;
    protected float[] textures;
    protected float[] normals;
    protected int[] indices;

    protected final int vaoID;

    public Mesh()
    {
        this.vaoID = glGenVertexArrays();
    }

    protected void create()
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

        onStop();
    }

    public boolean hasIndices()
    {
        return this.indices != null;
    }

    public int count()
    {
        return (this.indices == null) ? (this.vertices.length / 3) : this.indices.length;
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

    @Override
    public void onStart()
    {
        glBindVertexArray(this.vaoID);
        if(this.vertices != null)
            glEnableVertexAttribArray(0);
        if(this.textures != null)
            glEnableVertexAttribArray(1);
        if(this.normals != null)
            glEnableVertexAttribArray(2);
    }

    @Override
    public void onStop()
    {
        if(this.vertices != null)
            glDisableVertexAttribArray(0);
        if(this.textures != null)
            glDisableVertexAttribArray(1);
        if(this.normals != null)
            glDisableVertexAttribArray(2);

        glBindVertexArray(0);
    }
}
