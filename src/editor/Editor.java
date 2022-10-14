package editor;

import dream.components.transform.Transform;
import dream.light.DirectionalLight;
import dream.light.Light;
import dream.light.PointLight;
import dream.light.SpotLight;
import dream.managers.ResourcePool;
import dream.managers.WindowManager;
import dream.scene.Scene;
import dream.shape.Shape;
import editor.loader.Functions;
import editor.util.Constants;
import editor.util.components.ComponentTree;
import editor.util.settings.RightTree;
import editor.util.settings.LeftTree;
import editor.windows.*;
import editor.windows.modal.EngineSettings;
import editor.windows.popup.AddComponent;
import imgui.ImGui;

import java.util.HashMap;
import java.util.Map;

public class Editor
{
    private final Gui gui;
    private final Map<String, EditorWindow> editorWindows;

    public Editor()
    {
        this.gui = new Gui();
        this.editorWindows = new HashMap<>();
    }

    public void initialize(long windowID)
    {
        this.gui.create(windowID);
        this.gui.setMenuBarCallBack(() ->
        {
            if(ImGui.beginMenuBar())
            {
                if(ImGui.beginMenu("Home"))
                {
                    ImGui.menuItem("New Project", "Ctrl + N");

                    ImGui.menuItem("Open Project", "Ctrl + O");

                    ImGui.separator();

                    ImGui.menuItem("Save Project", "Ctrl + S");

                    ImGui.separator();

                    if(ImGui.menuItem("Quit"))
                        WindowManager.closeMain();

                    ImGui.endMenu();
                }

                if(ImGui.beginMenu("Preferences"))
                {
                    if(ImGui.menuItem("Engine Settings", "Ctrl + Alt + S"))
                        editorWindows.get(Constants.engineSettings).activate();

                    ImGui.endMenu();
                }

                ImGui.endMenuBar();
            }
        });

        LeftTree.initialize();
        RightTree.initialize();
        ComponentTree.initialize();
        Functions.initialize(this.editorWindows);

        //this.editorWindows.put("V", new EditorViewport("V"));
        EditorSceneGraph sceneGraph = new EditorSceneGraph();
        EditorInspector inspector = new EditorInspector();
        EditorViewport viewport = new EditorViewport();
        AddComponent addComponent = new AddComponent();

        this.editorWindows.put(Constants.editorViewport, viewport);
        this.editorWindows.put(Constants.editorScenegraph, sceneGraph);
        this.editorWindows.put(Constants.editorInspector, inspector);
        this.editorWindows.put(Constants.editorAddComponent, addComponent);
        this.editorWindows.put(Constants.engineSettings, new EngineSettings());
        this.editorWindows.put(Constants.editorOutput, new EditorWindow("Output"));

        Scene scene = new Scene();

        Shape shape = new Shape();
        shape.setTextures("crate diffuse.png", "crate specular.png");
        shape.setShader(ResourcePool.defaultMesh());
        scene.addChild(shape);

        Light light = new SpotLight();
        light.position.set(1.2f, 1.0f, 2.0f);
        scene.addLight(light);
        light = new DirectionalLight();
        scene.addLight(light);
        light = new PointLight();
        light.position.set(-1.2f, 1.0f, 2.0f);
        scene.addLight(light);

        viewport.setScene(scene);
        sceneGraph.setScene(scene);

        inspector.setOnAddComponent(addComponent::activate);
        inspector.setNode(sceneGraph.getNode());
        inspector.setLight(sceneGraph.getLight());
        addComponent.setSelectedNode(sceneGraph.getNode());

        //((EditorViewport) this.editorWindows.get("V")).setScene(scene);
    }

    public void refresh()
    {
        this.gui.start();

        this.editorWindows.values().forEach(EditorWindow::show);
        //ImGui.showStyleEditor();
        //ImGui.showDemoWindow();

        this.gui.end();
    }

    public void input()
    {
        this.editorWindows.values().forEach(EditorWindow::input);
    }

    public void destroy()
    {
        this.gui.destroy();
        this.editorWindows.values().forEach(EditorWindow::destroy);
    }

}
