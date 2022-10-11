package dream.components.mesh;

import dream.components.Component;
import dream.util.buffer.BufferTools;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class MeshRenderer extends Component
{
    protected final int vaoID;
    protected final int[] vboArray;

    protected Mesh mesh;
    private final Configuration configuration;


    public MeshRenderer()
    {
        this.mesh = null;
        this.configuration = new Configuration();

        this.vaoID = glGenVertexArrays();
        this.vboArray = new int[] {-1, -1, -1, -1};
    }

    public void setMesh(Mesh m)
    {
        this.mesh = m;
        create();
    }


    private void create()
    {
        if(this.mesh == null)
            return;

        glBindVertexArray(this.vaoID);

        createVBO(0, 3, this.mesh.vertices);
        createVBO(1, 2, this.mesh.textures);
        createVBO(2, 3, this.mesh.normals);
        createEBO(this.mesh.indices);

        onStop();
    }


    private void createEBO(int[] data)
    {
        if(data.length <= 0)
            return;

        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        IntBuffer buffer = BufferTools.createIntBuffer(data);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);
        this.vboArray[3] = eboID;
    }

    private void createVBO(int location, int size, float[] data)
    {
        if(data.length <= 0)
            return;

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

        this.configuration.activate();
    }

    @Override
    public void onStop()
    {
        this.configuration.deactivate();

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
        return this.mesh.hasIndices();
    }

    public int count()
    {
        return this.mesh.count();
    }

    public int getCull()
    {
        return this.configuration.getCull();
    }

    public void setCull(int val)
    {
        this.configuration.setCull(val);
    }

    public int getFace()
    {
        return this.configuration.getFace();
    }

    public void setFace(int val)
    {
        this.configuration.setFace(val);
    }

    public static class Configuration
    {
        public static final String[] faceOptions = new String[] { "Wireframe", "Solid" };
        public static final String[] cullOptions = new String[] { "None", "Front Face", "Back Face", "Both Faces" };

        public int cullFlag;
        public int faceFlag;

        public Configuration()
        {
            this.cullFlag = -1;
            this.faceFlag = GL_FILL;

            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LESS);
        }

        public void activate()
        {
            glPolygonMode(GL_FRONT_AND_BACK, this.faceFlag);
            if(this.cullFlag > 0)
            {
                glEnable(GL_CULL_FACE);
                glCullFace(this.cullFlag);
            }
        }

        public void deactivate()
        {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            glDisable(GL_CULL_FACE);
        }

        public int getCull()
        {
            if(this.cullFlag == GL_FRONT)
                return 1;
            else if(this.cullFlag == GL_BACK)
                return 2;
            else if(this.cullFlag == GL_FRONT_AND_BACK)
                return 3;
            return 0;
        }

        public void setCull(int val)
        {
            if(val == 1)
                this.cullFlag = GL_FRONT;
            else if(val == 2)
                this.cullFlag = GL_BACK;
            else if(val == 3)
                this.cullFlag = GL_FRONT_AND_BACK;
            else
                this.cullFlag = 0;
        }

        public int getFace()
        {
            if(this.faceFlag == GL_LINE)
                return 0;
            else if(this.faceFlag == GL_FILL)
                return 1;
            return -1;
        }

        public void setFace(int val)
        {
            if(val == 0)
                this.faceFlag = GL_LINE;
            else if(val == 1)
                this.faceFlag = GL_FILL;
        }
    }


}
