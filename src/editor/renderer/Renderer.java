package editor.renderer;

import dream.Engine;
import dream.camera.Camera3D;
import dream.components.material.Material;
import dream.components.mesh.Mesh;
import dream.components.transform.Transform;
import editor.events.Event;
import editor.events.EventManager;
import editor.events.EventType;
import editor.events.handler.Handler;
import editor.events.type.WindowResize;
import dream.io.FrameBuffer;
import dream.io.Input;
import dream.managers.WindowManager;
import dream.node.Node;
import dream.scene.Scene;
import dream.shader.Shader;
import dream.shader.ShaderConstants;
import dream.shape.Shape;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Renderer implements Handler
{
    private final float[] position;
    private final float[] size;

    public boolean useCamera;

    private FrameBuffer frameBuffer;
    public final Camera3D camera;
    public final RenderConfigs configs;
    private Scene scene;

    public Renderer(float[] position, float[] size)
    {
        this.position = position;
        this.size = size;

        int[] windowSize = WindowManager.getMainSize();
        this.frameBuffer = new FrameBuffer(windowSize[0], windowSize[1]);

        this.camera = new Camera3D();
        this.configs = new RenderConfigs();

        this.useCamera = true;

        EventManager.add(EventType.WindowResize, this);
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
        this.configs.activate();

        if(!this.useCamera)
        {
            this.configs.deactivate();
            this.frameBuffer.stop();
            return;
        }

        if(root instanceof Shape shape)
        {
            Shader shader = shape.getShader();
            Transform t = shape.getComponent(Transform.class);
            Material m = shape.getComponent(Material.class);

            if(t != null && m != null)
            {
                shader.start();
                Mesh mesh = shape.getComponent(Mesh.class);

                shader.loadUniform(ShaderConstants.transformation, t.getMatrix());
                shader.loadUniform(ShaderConstants.view, this.camera.getView());
                shader.loadUniform(ShaderConstants.projection, this.camera.getProjection());
                shader.loadUniform(ShaderConstants.color, m.diffuse);
                shader.loadUniform(ShaderConstants.isActive, m.hasTexture());

                m.getPack().start();

                mesh.onStart();
                if(mesh.hasIndices())
                    glDrawElements(GL_TRIANGLES, mesh.count(), GL_UNSIGNED_INT, 0);
                else
                    glDrawArrays(GL_TRIANGLES, 0, mesh.count());

                m.getPack().stop();

                mesh.onStop();
                shader.stop();
            }
        }

        this.configs.deactivate();
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
        if(this.scene == null || !this.useCamera)
            return;

        int[] winSize = WindowManager.getMainSize();
        float[] coordinates = Input.getScreenCoordinates(winSize, position, size);

        if(coordinates[0] < 0 || coordinates[0] > winSize[0] ||
                coordinates[1] < 0 || coordinates[1] > winSize[1])
            return;

        if(Input.getScrollY() != 0.0f)
            this.camera.incrementZoom(-Input.getScrollY() * 0.1f);

        if(Input.isButtonJustPressed(GLFW_MOUSE_BUTTON_LEFT))
        {
            int x = (int) coordinates[0];
            int y = (int) coordinates[1];
        }

        if(Input.isButtonPressed(GLFW_MOUSE_BUTTON_RIGHT))
        {
            Vector2f mouseDelta = Input.getMouseDelta();
            mouseDelta.mul(0.25f); // Reduce the sensitivity
            this.camera.rotate(mouseDelta.y , mouseDelta.x);
        }

        float speed = (float) (Engine.deltaTime * Engine.nanoSeconds);
        if(Input.isKeyShiftPressed())
            speed *= 2.5f;

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
        this.frameBuffer.destroy();
    }
}
