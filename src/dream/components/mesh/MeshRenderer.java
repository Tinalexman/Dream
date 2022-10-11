package dream.components.mesh;

import dream.camera.Camera;
import dream.components.Component;
import dream.components.material.Material;
import dream.components.transform.Transform;
import dream.node.drawable.Drawable;
import dream.shader.Shader;
import dream.shader.ShaderConstants;
import dream.util.buffer.BufferTools;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class MeshRenderer extends Component
{
    protected final int vaoID;
    protected final int[] vboArray;

    protected int count;
    protected boolean hasIndices;

    protected Mesh mesh;

    public MeshRenderer()
    {
        this.mesh = null;
        this.vaoID = glGenVertexArrays();
        this.vboArray = new int[] {-1, -1, -1, -1};
    }

    public void setMesh(Mesh m)
    {
        this.mesh = m;
        create();
    }

    public int[] meshProperties()
    {
        return new int[]
        {
            mesh.vertices.length,
            mesh.textures.length,
            mesh.normals.length,
            mesh.indices.length
        };
    }

    private void create()
    {
        if(this.mesh == null)
            return;

        glBindVertexArray(this.vaoID);

        if(this.mesh.vertices.length > 0)
            createVBO(0, 3, mesh.vertices);
        if(this.mesh.textures.length > 0)
            createVBO(1, 2, mesh.textures);
        if(this.mesh.normals.length > 0)
            createVBO(2, 3, mesh.normals);
        if(this.mesh.indices.length > 0)
            createEBO(mesh.indices);

        onStop();
    }

    private void createEBO(int[] data)
    {
        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        IntBuffer buffer = BufferTools.createIntBuffer(data);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);
        this.vboArray[3] = eboID;
    }

    private void createVBO(int location, int size, float[] data)
    {
        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = BufferTools.createFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(location, size, GL_FLOAT, false, 0, 0);
        MemoryUtil.memFree(buffer);
        this.vboArray[location] = vboID;
    }

    @Override
    public void onStart()
    {
        glBindVertexArray(this.vaoID);
        if(this.mesh.vertices.length > 0)
            glEnableVertexAttribArray(0);
        if(this.mesh.textures.length > 0)
            glEnableVertexAttribArray(1);
        if(this.mesh.normals.length > 0)
            glEnableVertexAttribArray(2);
    }

    @Override
    public void onStop()
    {
        if(this.mesh.vertices.length > 0)
            glDisableVertexAttribArray(0);
        if(this.mesh.textures.length > 0)
            glDisableVertexAttribArray(1);
        if(this.mesh.normals.length > 0)
            glDisableVertexAttribArray(2);

        glBindVertexArray(0);
    }

    @Override
    public void destroy()
    {
        for (int j : this.vboArray)
        {
            if (j != -1)
                glDeleteBuffers(j);
        }

        if(this.vaoID != -1)
            glDeleteVertexArrays(this.vaoID);
    }

    public boolean hasIndices()
    {
        return this.mesh.indices.length > 0;
    }

    public int count()
    {
        return (this.mesh.indices.length == 0) ?
                (this.mesh.vertices.length / 3) : this.mesh.indices.length;
    }
}
