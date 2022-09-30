package editor.renderer;

import static org.lwjgl.opengl.GL20.*;

public class RenderConfigs
{
    public static final String[] faceOptions = new String[] { "Wireframe", "Solid" };
    public static final String[] cullOptions = new String[] { "None", "Front Face", "Back Face", "Both Faces" };

    public int cullFlag;
    public int faceFlag;

    public RenderConfigs()
    {
        this.cullFlag = -1;

        this.faceFlag = GL_FILL;
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
    }

    public void activate()
    {
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

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
