package editor.renderer;

import dream.Engine;
import dream.camera.Camera;
import dream.components.material.Material;
import dream.components.mesh.MeshRenderer;
import dream.components.transform.Transform;
import dream.io.FrameBuffer;
import dream.io.Input;
import dream.light.DirectionalLight;
import dream.light.Light;
import dream.light.PointLight;
import dream.light.SpotLight;
import dream.managers.WindowManager;
import dream.node.Node;
import dream.node.drawable.Drawable;
import dream.scene.Scene;
import dream.shader.Shader;

import editor.events.Event;
import editor.events.EventManager;
import editor.events.EventType;
import editor.events.handler.Handler;
import editor.events.type.WindowResize;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import static dream.shader.ShaderConstants.*;


public class Renderer implements Handler
{
    private final float[] position;
    private final float[] size;

    public boolean useCamera;

    private FrameBuffer frameBuffer;
    public final Camera camera;
    private Scene scene;

    public Renderer(float[] position, float[] size)
    {
        this.position = position;
        this.size = size;

        int[] windowSize = WindowManager.getMainSize();
        this.frameBuffer = new FrameBuffer(windowSize[0], windowSize[1]);

        this.camera = new Camera();

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
        Node lights = this.scene.getLights();
        if(root == null)
            return;

        this.frameBuffer.start();
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(!this.useCamera)
        {
            this.frameBuffer.stop();
            return;
        }

        render(root, lights.getChildren(), this.camera);
        for(Node node : root.getChildren())
            render(node, lights.getChildren(), this.camera);

        this.frameBuffer.stop();
    }

    Vector3f[] cubePositions =
    {
        new Vector3f( 0.0f, 0.0f, 0.0f),
        new Vector3f( 2.0f, 5.0f, -15.0f),
        new Vector3f(-1.5f, -2.2f, -2.5f),
        new Vector3f(-3.8f, -2.0f, -12.3f),
        new Vector3f( 2.4f, -0.4f, -3.5f),
        new Vector3f(-1.7f, 3.0f, -7.5f),
        new Vector3f( 1.3f, -2.0f, -2.5f),
        new Vector3f( 1.5f, 2.0f, -2.5f),
        new Vector3f( 1.5f, 0.2f, -1.5f),
        new Vector3f(-1.3f, 1.0f, -1.5f)
    };
    Vector3f rotate = new Vector3f(1.0f, 0.3f, 0.5f);

    private void render(Node node, List<Node> lights, Camera camera)
    {
        Shader shader = (node instanceof Drawable drawable) ? drawable.getShader() : null;
        if(shader == null)
            return;

        if(!node.isVisible())
            return;

        MeshRenderer renderer = node.getComponent(MeshRenderer.class);
        Transform transform = node.getComponent(Transform.class);
        Material material = node.getComponent(Material.class);

        if(renderer == null || transform == null || material == null)
            return;

        shader.start();
        renderer.onStart();
        material.getPack().start();

        if(node.getName().equals("Light Cube"))
        {
            shader.loadUniform(transformation, transform.getMatrix());
            shader.loadUniform(view, camera.getView());
            shader.loadUniform(projection, camera.getProjection());

            if(renderer.hasIndices())
                glDrawElements(GL_TRIANGLES, renderer.count(), GL_UNSIGNED_INT, 0);
            else
                glDrawArrays(GL_TRIANGLES, 0, renderer.count());

            shader.stop();
            renderer.onStop();
            material.getPack().stop();
            return;
        }

        loadLights(lights, shader, camera);

        //shader.loadUniform(transformation, transform.getMatrix());
        shader.loadUniform(view, camera.getView());
        shader.loadUniform(projection, camera.getProjection());

        Matrix4f temp = new Matrix4f();
        Matrix3f inverse = new Matrix3f(transform.getMatrix().invert(temp));
        inverse.transpose();
        shader.loadUniform(inverseNormals, inverse);
        shader.loadUniform(viewPosition, camera.getPosition());

        loadMaterial(material, shader);

        for(int i = 0; i < 10; ++i)
        {
            Matrix4f model = new Matrix4f().identity();
            model.translate(cubePositions[i]);
            float angle = 20.0f * i;
            model.rotate((float) Math.toRadians(angle), rotate);

            shader.loadUniform(transformation, model);

            if(renderer.hasIndices())
                glDrawElements(GL_TRIANGLES, renderer.count(), GL_UNSIGNED_INT, 0);
            else
                glDrawArrays(GL_TRIANGLES, 0, renderer.count());
        }

        material.getPack().stop();
        renderer.onStop();
        shader.stop();
    }

    private void loadLights(List<Node> lights, Shader shader, Camera camera)
    {
        float DIRECTIONAL_LIGHT = 1.0f, POINT_LIGHT = 2.0f, SPOT_LIGHT = 3.0f;

        for(Node node : lights)
        {
            Light light = (Light) node;
            shader.loadUniform(lightAmbient, light.ambient);
            shader.loadUniform(lightDiffuse, light.diffuse);
            shader.loadUniform(lightSpecular, light.specular);

            if(light instanceof DirectionalLight directionalLight)
            {
                shader.loadUniform(lightType, DIRECTIONAL_LIGHT);
                shader.loadUniform(lightDirection, directionalLight.direction);
            }
            else if(light instanceof PointLight pointLight)
            {
                shader.loadUniform(lightType, POINT_LIGHT);
                shader.loadUniform(lightPosition, light.position);
                shader.loadUniform(lightConstant, pointLight.constant);
                shader.loadUniform(lightLinear, pointLight.linear);
                shader.loadUniform(lightQuadratic, pointLight.quadratic);
            }
            else if(light instanceof SpotLight spotLight)
            {
                shader.loadUniform(lightType, SPOT_LIGHT);

                shader.loadUniform(lightPosition, camera.getPosition());
                shader.loadUniform(lightDirection, camera.getForward());

//                shader.loadUniform(lightPosition, light.position);
//                shader.loadUniform(lightDirection, spotLight.direction);
                shader.loadUniform(lightCutoff, spotLight.cutoff);
                shader.loadUniform(lightOuterCutoff, spotLight.outerCutoff);
            }
        }
    }

    private void loadMaterial(Material material, Shader shader)
    {
        shader.loadUniform(materialDiffuse, material.diffuse);
        shader.loadUniform(materialSpecular, material.specular);
        shader.loadUniform(materialReflectance, material.reflectance);

        boolean hasDiffuse = material.hasDiffuse();
        shader.loadUniform(materialHasDiffuseMap, hasDiffuse);
        if(hasDiffuse)
            shader.loadUniform(materialDiffuseMap, 0);

        boolean hasSpecular = material.hasSpecular();
        shader.loadUniform(materialHasSpecularMap, hasSpecular);
        if(hasSpecular)
            shader.loadUniform(materialSpecularMap, 1);
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
