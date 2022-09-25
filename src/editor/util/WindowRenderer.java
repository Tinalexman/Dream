package editor.util;

import dream.Engine;
import dream.camera.Camera3D;
import dream.components.mesh.Mesh;
import dream.events.Event;
import dream.events.EventManager;
import dream.events.EventType;
import dream.events.handler.Handler;
import dream.events.type.WindowResize;
import dream.io.FrameBuffer;
import dream.io.Input;
import dream.managers.ResourcePool;
import dream.managers.WindowManager;
import dream.node.Node;
import dream.scene.Scene;
import dream.shader.Shader;
import dream.shape.Shape;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class WindowRenderer implements Handler
{
    private final float[] position;
    private final float[] size;

    private FrameBuffer frameBuffer;
    private final Shader shader;
    private final Camera3D camera;
    private Scene scene;

    public WindowRenderer(float[] position, float[] size)
    {
        this.position = position;
        this.size = size;

        int[] windowSize = WindowManager.getMainSize();
        this.frameBuffer = new FrameBuffer(windowSize[0], windowSize[1]);

        this.camera = new Camera3D();

        EventManager.add(EventType.WindowResize, this);

        this.shader = ResourcePool.addAndGetShader("default.glsl");
        this.shader.onStart();
    }

    @Override
    public void respond(Event event)
    {
        if(event.type == EventType.WindowResize)
        {
            WindowResize w = (WindowResize) event;
            if(w.minimized())
                return;

            this.frameBuffer.destroy();
            this.frameBuffer = new FrameBuffer(w.width, w.height);
        }
    }

    public void render()
    {
        if(this.scene == null)
            return;

        Node root = this.scene.getRoot();
        if(root == null)
            return;

        this.frameBuffer.start();
        this.shader.start();

        glClearColor(0.0f, 0.0f, 0.0f, 0.7f);
        glClear(GL_COLOR_BUFFER_BIT);

        if(root instanceof Shape)
        {
            //Transform t = root.getComponent(Transform.class);
            Mesh mesh = root.getComponent(Mesh.class);
//            this.shader.loadUniform(ShaderConstants.transformation, t.getMatrix());
//            this.shader.loadUniform(ShaderConstants.view, this.camera.getView());
//            this.shader.loadUniform(ShaderConstants.projection, this.camera.getProjection());
//            this.shader.loadUniform(ShaderConstants.color, 1.0f, 1.0f, 1.0f);

            mesh.onStart();
            if(mesh.hasIndices())
                glDrawElements(GL_TRIANGLES, mesh.count(), GL_UNSIGNED_INT, 0);
            else
                glDrawArrays(GL_TRIANGLES, 0, mesh.count());
            mesh.onStop();
        }

        this.shader.stop();
        this.frameBuffer.stop();
    }

    public void setScene(Scene scene)
    {
        this.scene = scene;
    }

    public int getID()
    {
        return this.frameBuffer.getID();
    }

    public void input()
    {
        if(this.scene == null)
            return;

        if(Input.getScrollY() != 0.0f)
            this.camera.incrementZoom(-Input.getScrollY() * 0.1f);

        if(Input.isButtonJustPressed(GLFW_MOUSE_BUTTON_LEFT))
        {
            float[] coordinates = Input.getScreenCoordinates(position, size);
            int x = (int) coordinates[0];
            int y = (int) coordinates[1];

            //int ID = this.sceneRenderer.readPixelAt(x, y);
            System.out.println("X: " + x + " Y: " + y);
        }

        if(Input.isButtonPressed(GLFW_MOUSE_BUTTON_RIGHT))
        {
            Vector2f mouseDelta = Input.getMouseDelta();
            mouseDelta.mul(0.1f); // Reduce the sensitivity
            this.camera.rotate(mouseDelta.y , mouseDelta.x);
        }

        float speed = Engine.deltaTime * 0.1f;
        Vector3f temp = new Vector3f();

        if(Input.isKeyPressed(GLFW_KEY_W))
        {
            this.camera.getForward().mul(speed, temp);
            this.camera.incrementPosition(temp);
        }

        if(Input.isKeyPressed(GLFW_KEY_S))
        {
            this.camera.getForward().mul(-speed, temp);
            this.camera.incrementPosition(temp);
        }

        if(Input.isKeyPressed(GLFW_KEY_LEFT))
        {
            this.camera.getRight().mul(-speed, temp);
            this.camera.incrementPosition(temp);
        }

        if(Input.isKeyPressed(GLFW_KEY_RIGHT))
        {
            this.camera.getRight().mul(speed, temp);
            this.camera.incrementPosition(temp);
        }

        if(Input.isKeyPressed(GLFW_KEY_UP))
        {
            this.camera.getUpVector().mul(speed, temp);
            this.camera.incrementPosition(temp);
        }

        if(Input.isKeyPressed(GLFW_KEY_DOWN))
        {
            this.camera.getUpVector().mul(-speed, temp);
            this.camera.incrementPosition(temp);
        }
    }

    public void destroy()
    {
        this.shader.destroy();
        this.frameBuffer.destroy();
    }
}
