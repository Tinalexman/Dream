package editor.renderer;

import dream.Engine;
import dream.camera.Camera;
import dream.components.material.Material;
import dream.components.mesh.MeshRenderer;
import dream.components.transform.Transform;
import dream.io.FrameBuffer;
import dream.io.Input;
import dream.managers.WindowManager;
import dream.node.Node;
import dream.node.drawable.Drawable;
import dream.scene.Scene;
import dream.shader.Shader;
import dream.shader.ShaderConstants;
import editor.events.Event;
import editor.events.EventManager;
import editor.events.EventType;
import editor.events.handler.Handler;
import editor.events.type.WindowResize;
import org.joml.Matrix4f;
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
    public final Camera camera;
    public final RenderConfigs configs;
    private Scene scene;

    public Renderer(float[] position, float[] size)
    {
        this.position = position;
        this.size = size;

        int[] windowSize = WindowManager.getMainSize();
        this.frameBuffer = new FrameBuffer(windowSize[0], windowSize[1]);

        this.camera = new Camera();
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
            {
                glViewport(0, 0, w.width, w.height);
                return;
            }

            this.frameBuffer.destroy();
            this.frameBuffer = new FrameBuffer(w.width, w.height);
            this.camera.setAspectRatio((float) w.width / w.height);
            glViewport(0, 0, w.width, w.height);
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

        render(root, this.camera);
        for(Node node : root.getChildren())
            render(node, this.camera);

        this.configs.deactivate();
        this.frameBuffer.stop();
    }

    private void render(Node node, Camera camera)
    {
        Shader shader = (node instanceof Drawable drawable) ? drawable.getShader() : null;
        if(shader == null)
            return;

        MeshRenderer renderer = node.getComponent(MeshRenderer.class);
        Transform transform = node.getComponent(Transform.class);
        Material material = node.getComponent(Material.class);

        if(renderer == null || transform == null || material == null)
            return;

        shader.start();
        renderer.onStart();
        material.getPack().start();

        shader.loadUniform(ShaderConstants.transformation, transform.getMatrix());
        shader.loadUniform(ShaderConstants.view, camera.getView());
        shader.loadUniform(ShaderConstants.projection, camera.getProjection());
        shader.loadUniform(ShaderConstants.color, material.diffuse);
        shader.loadUniform(ShaderConstants.isActive, material.hasTexture());

        if(renderer.hasIndices())
            glDrawElements(GL_TRIANGLES, renderer.count(), GL_UNSIGNED_INT, 0);
        else
            glDrawArrays(GL_TRIANGLES, 0, renderer.count());

        material.getPack().stop();
        renderer.onStop();
        shader.stop();
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
