package game;

import dream.components.material.Material;
import dream.components.mesh.Mesh;
import dream.components.mesh.MeshFactory;
import dream.components.mesh.MeshRenderer;
import dream.components.transform.Transform;
import dream.environment.Environment;
import dream.managers.ResourcePool;
import dream.node.drawable.DrawableNode;
import dream.scene.Scene;
import dream.shader.Shader;

import java.util.HashMap;
import java.util.Map;

import static dream.shader.ShaderConstants.*;

public class Game
{
    private static Game game;

    public static Game game()
    {
        if(Game.game == null)
            Game.game = new Game();

        return Game.game;
    }

    private final GameEnvironment gameEnvironment;
    private final Map<String, Scene> scenes;

    private Game()
    {
        this.gameEnvironment = new GameEnvironment();
        this.scenes = new HashMap<>();

        defaultScene();
    }

    private void defaultScene()
    {
        Scene scene = new Scene();

        DrawableNode cube = new DrawableNode();
        cube.name("Container Cube");
        Material cubeMaterial = new Material();
        cubeMaterial.getPack().diffuse = ResourcePool.addAndGetTexture("crate diffuse.png");
        cubeMaterial.getPack().specular = ResourcePool.addAndGetTexture("crate specular.png");
        Mesh cubeMesh = MeshFactory.asCube();
        MeshRenderer cubeRenderer = new MeshRenderer();
        cubeRenderer.setMesh(cubeMesh);
        cube.addComponent(cubeMaterial);
        cube.addComponent(new Transform());
        cube.addComponent(cubeRenderer);
        scene.add(cube);

        DrawableNode floor = new DrawableNode();
        floor.name("Floor");
        Material floorMat = new Material();
        floorMat.getPack().diffuse = ResourcePool.addAndGetTexture("Normal Grass.png");
        Mesh floorMesh = MeshFactory.createPlane(1, 5.0f);
        MeshRenderer floorRenderer = new MeshRenderer();
        floorRenderer.setMesh(floorMesh);
        floor.addComponent(floorMat);
        floor.addComponent(floorRenderer);
        Transform floorT = new Transform();
        floorT.position.set(0.0f, -0.51f, 0.0f);
        floor.addComponent(floorT);
        scene.add(floor);

        DrawableNode plane = new DrawableNode();
        Shader shader = ResourcePool.addAndGetShader("blendShader.glsl");
        shader.storeUniforms(projection, view, transformation);
        shader.storeUniforms();
        plane.setShader(shader);
        plane.name("Window Plane");
        Transform planeTransform = new Transform();
        planeTransform.incrementRotation(90.0f, 0.0f, 0.0f);
        planeTransform.incrementPosition(0.0f, 0.0f, 1.5f);
        Material planeMaterial = new Material();
        planeMaterial.transparency = true;
        planeMaterial.getPack().diffuse = ResourcePool.addAndGetTexture("window.png");
        Mesh mesh = MeshFactory.createPlane(1, 0.5f);
        MeshRenderer renderer = new MeshRenderer();
        renderer.setMesh(mesh);
        plane.addComponent(planeMaterial);
        plane.addComponent(planeTransform);
        plane.addComponent(renderer);
        scene.add(plane);

        this.scenes.put(Constants.mainScene, scene);
    }

    public Scene mainScene()
    {
        return this.scenes.get(Constants.mainScene);
    }

    public Environment getEnvironment()
    {
        return this.gameEnvironment.environment();
    }
}
