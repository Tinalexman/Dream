package dream.renderer;

import dream.camera.Camera;
import dream.components.mesh.MeshFactory;
import dream.components.mesh.MeshRenderer;
import dream.io.FrameBuffer;
import dream.io.PickingTexture;
import dream.managers.ResourcePool;
import dream.managers.WindowManager;
import dream.node.Node;
import dream.postprocessing.FilterManager;
import dream.scene.Scene;
import dream.shader.Shader;
import dream.util.collection.Join;
import editor.events.Event;
import editor.events.EventType;
import editor.events.handler.Handler;
import editor.events.type.WindowResize;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import static dream.shader.ShaderConstants.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Renderer implements Handler
{
    public enum IDType
    {
        colorFramebuffer,
        pickingTexture
    }

    protected FrameBuffer colorBuffer, postProcessBuffer;
    protected Shader pickingShader;
    protected PickingTexture pickingTexture;
    protected MeshRenderer postProcessRenderer;

    protected final Vector4f clearColor;
    protected final Camera camera;
    protected boolean useCamera;
    protected Join<Node> selectedNode;

    protected int currentFilterIndex;
    protected boolean postProcess;

    public Renderer()
    {
        int[] windowSize = WindowManager.getMainSize();
        this.colorBuffer = new FrameBuffer(windowSize[0], windowSize[1]);
        this.postProcessBuffer = new FrameBuffer(windowSize[0], windowSize[1]);
        this.pickingTexture = new PickingTexture(windowSize[0], windowSize[1]);
        this.postProcessRenderer = new MeshRenderer();
        this.postProcessRenderer.setMesh(MeshFactory.createPlane(1, 1.0f, MeshFactory.Orientation.zAxis));

        this.camera = new Camera();

        this.clearColor = new Vector4f(0.4f, 0.8f, 0.9f, 1.0f);
        this.pickingShader = ResourcePool.addAndGetShader("pickingShader.glsl");
        this.pickingShader.onStart();
        this.pickingShader.storeUniforms(transformation, projection,
                view, objectIndex, drawIndex);

        this.useCamera = true;
    }

    public void setFilterIndex(int index)
    {
        this.currentFilterIndex = index;
    }

    public void setSelectedNode(Join<Node> selectedNode)
    {
        this.selectedNode = selectedNode;
    }

    public void postProcess(boolean postProcess)
    {
        this.postProcess = postProcess;
    }

    public boolean postProcess()
    {
        return this.postProcess;
    }

    @Override
    public void respond(Event event)
    {
        if(event.type == EventType.WindowResize)
        {
            WindowResize w = (WindowResize) event;
            if(w.minimized())
            {
                glViewport(0, 0, w.width, w.height);
                return;
            }

            this.colorBuffer.destroy();
            this.postProcessBuffer.destroy();
            this.pickingTexture.destroy();

            this.colorBuffer = new FrameBuffer(w.width, w.height);
            this.postProcessBuffer = new FrameBuffer(w.width, w.height);
            this.pickingTexture = new PickingTexture(w.width, w.height);
            this.camera.setAspectRatio((float) w.width / w.height);
            glViewport(0, 0, w.width, w.height);
        }
    }

    public void input()
    {

    }

    public Vector4f getClearColor()
    {
        return this.clearColor;
    }

    public Camera getCamera()
    {
        return this.camera;
    }

    public int getID(IDType type)
    {
        return switch (type)
        {
            case colorFramebuffer -> (this.postProcess) ? this.postProcessBuffer.getID() : this.colorBuffer.getID();
            case pickingTexture -> this.pickingTexture.getID();
        };
    }

    public void render(Scene scene)
    {

    }

    public void destroy()
    {
        this.colorBuffer.destroy();
        this.pickingTexture.destroy();
        this.pickingShader.destroy();
    }

    protected float[] readPixelAt(int x, int y)
    {
        return this.pickingTexture.readPixel(x, y);
    }

    public boolean useCamera()
    {
        return this.useCamera;
    }

    public void useCamera(boolean use)
    {
        this.useCamera = use;
    }

    protected void postProcess(Shader filterShader)
    {
        if(!postProcess || filterShader == null)
            return;

        this.postProcessBuffer.start();
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        this.postProcessRenderer.onStart();
        filterShader.start();

        glDisable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this.colorBuffer.getID());
        glDrawElements(GL_TRIANGLES, this.postProcessRenderer.count(), GL_UNSIGNED_INT, 0);

        this.postProcessRenderer.onStop();
        filterShader.stop();

        this.postProcessBuffer.stop();
    }

    public void start()
    {
        this.colorBuffer.start();
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void stop()
    {
        this.colorBuffer.stop();

        postProcess(FilterManager.getShader(this.currentFilterIndex));
    }

}



