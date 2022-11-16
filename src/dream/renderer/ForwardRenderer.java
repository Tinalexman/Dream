package dream.renderer;

import dream.Engine;
import dream.components.material.Material;
import dream.components.mesh.MeshRenderer;
import dream.components.transform.Transform;
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
import dream.shader.ShaderConstants;
import game.Game;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static dream.shader.ShaderConstants.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class ForwardRenderer extends Renderer
{
    private final float[] position;
    private final float[] size;

    public ForwardRenderer(float[] position, float[] size)
    {
        super();

        this.position = position;
        this.size = size;
    }

    @Override
    public void render(Scene scene)
    {
        if(scene == null)
            return;

        Node root = scene.root();
        List<Light> lights = new ArrayList<>();
        if(root == null)
            return;

        if(!this.useCamera)
        {
            this.colorBuffer.stop();
            return;
        }

        renderPicking(root.getChildren());

        start();

        renderColorBuffer(root, lights);
        for(Node node : root.getChildren())
            renderColorBuffer(node, lights);

        Game.game().getEnvironment().show(this.camera);

        stop();
    }

    private void renderColorBuffer(Node node, List<Light> lights)
    {
        Shader shader = (node instanceof Drawable drawable) ? drawable.getShader() : null;
        if(shader == null || !node.isVisible())
            return;

        MeshRenderer renderer = node.getComponent(MeshRenderer.class);
        Transform transform = node.getComponent(Transform.class);
        Material material = node.getComponent(Material.class);

        if(renderer == null || transform == null || material == null)
            return;

        if(material.transparency)
            blend(true);

        shader.start();
        renderer.onStart();
        material.getPack().start();

        if(!material.transparency)
            loadLights(lights, shader);

        shader.loadUniform(transformation, transform.getMatrix());
        shader.loadUniform(view, this.camera.getView());
        shader.loadUniform(projection, this.camera.getProjection());

        if(!material.transparency)
        {
            Matrix4f temp = new Matrix4f();
            Matrix3f inverse = new Matrix3f(transform.getMatrix().invert(temp));
            inverse.transpose();
            shader.loadUniform(inverseNormals, inverse);
            shader.loadUniform(viewPosition, this.camera.getPosition());

            loadMaterial(material, shader);
        }

        if(renderer.hasIndices())
            glDrawElements(GL_TRIANGLES, renderer.count(), GL_UNSIGNED_INT, 0);
        else
            glDrawArrays(GL_TRIANGLES, 0, renderer.count());

        blend(false);

        material.getPack().stop();
        renderer.onStop();
        shader.stop();
    }

    private void blend(boolean condition)
    {
        if(condition)
        {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }
        else
            glDisable(GL_BLEND);
    }

    private void loadLights(List<Light> lights, Shader shader)
    {
        float NONE = 0.0f, DIRECTIONAL_LIGHT = 1.0f, POINT_LIGHT = 2.0f, SPOT_LIGHT = 3.0f;

        for(int i = 0; i < lights.size(); ++i)
        {
            Light light = lights.get(i);
            if(!light.active)
            {
                shader.loadUniform("lights[" + i + "].type", NONE);
                continue;
            }

            shader.loadUniform("lights[" + i + "]." + ambient, light.ambient);
            shader.loadUniform("lights[" + i + "]." + diffuse, light.diffuse);
            shader.loadUniform("lights[" + i + "]." + specular, light.specular);

            if(light instanceof DirectionalLight directionalLight)
            {
                shader.loadUniform("lights[" + i + "].type", DIRECTIONAL_LIGHT);
                shader.loadUniform("lights[" + i + "]." + direction, directionalLight.direction);
            }
            else if(light instanceof PointLight pointLight)
            {
                shader.loadUniform("lights[" + i + "].type", POINT_LIGHT);
                shader.loadUniform("lights[" + i + "]." + ShaderConstants.position, light.position);
                shader.loadUniform("lights[" + i + "]." + constant, pointLight.constant);
                shader.loadUniform("lights[" + i + "]." + linear, pointLight.linear);
                shader.loadUniform("lights[" + i + "]." + quadratic, pointLight.quadratic);
            }
            else if(light instanceof SpotLight spotLight)
            {
                shader.loadUniform("lights[" + i + "].type", SPOT_LIGHT);
                shader.loadUniform("lights[" + i + "]." + ShaderConstants.position, light.position);
                shader.loadUniform("lights[" + i + "]." + direction, spotLight.direction);
                shader.loadUniform("lights[" + i + "]." + cutoff, spotLight.cutoff);
                shader.loadUniform("lights[" + i + "]." + outerCutoff, spotLight.outerCutoff);
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

    @Override
    public void input()
    {
        if(!this.useCamera)
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
            int x = (int) coordinates[0], y = (int) coordinates[1];
            float[] ID = readPixelAt(x, y);
            //this.selectedNode.value = Node.getNode((int) ID[0] - 1);
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

    private void pickingUniforms(Matrix4f transform, int position, int nodeID)
    {
        this.pickingShader.loadUniform(transformation, transform);

        if(this.camera.hasViewChanged())
            this.pickingShader.loadUniform(view, this.camera.getView());
        if(this.camera.hasProjectionChanged())
            this.pickingShader.loadUniform(projection, this.camera.getProjection());

        this.pickingShader.loadUniform(drawIndex, (float) position);
        this.pickingShader.loadUniform(objectIndex, (float) nodeID);
    }

    public void renderPicking(List<Node> nodes)
    {
        this.pickingTexture.start();

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        this.pickingShader.start();

        for(int pos = 0; pos < nodes.size(); ++pos)
        {
            Node node = nodes.get(pos);
            if(node instanceof Drawable)
            {
                Transform transform = node.getComponent(Transform.class);
                MeshRenderer mesh = node.getComponent(MeshRenderer.class);

                pickingUniforms(transform.getMatrix(), pos, (node.getID() + 1));

                mesh.onStart();
                if(mesh.hasIndices())
                    glDrawElements(GL_TRIANGLES, mesh.count(), GL_UNSIGNED_INT, 0);
                else
                    glDrawArrays(GL_TRIANGLES, 0, mesh.count());
                mesh.onStop();
            }
        }

        this.pickingShader.stop();
        this.pickingTexture.stop();
    }
}
