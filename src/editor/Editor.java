package editor;

import dream.managers.WindowManager;
import dream.scene.Scene;
import dream.shape.Shape;
import editor.util.Constants;
import editor.windows.*;
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

        this.editorWindows.put(Constants.editorViewport, new EditorViewport());
        this.editorWindows.put(Constants.editorScenegraph, new EditorSceneGraph());
        this.editorWindows.put(Constants.editorInspector, new EditorInspector());
        this.editorWindows.put(Constants.engineSettings, new EngineSettings());
        this.editorWindows.put(Constants.editorOutput, new EditorWindow("Output"));
    }

    public void initialize(long windowID)
    {
        this.gui.create(windowID);

        Runnable mainMenu = () ->
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
        };

        this.gui.setMenuBarCallBack(mainMenu);

        Scene scene = new Scene(new Shape());
        ((EditorViewport) this.editorWindows.get(Constants.editorViewport)).setScene(scene);
    }


    public void refresh()
    {
        this.gui.start();

        this.editorWindows.values().forEach(EditorWindow::show);
        //ImGui.showStyleEditor();

        this.gui.end();
    }

    public void input()
    {
        this.editorWindows.values().forEach(EditorWindow::input);
    }

    public void destroy()
    {
        this.gui.destroy();
    }

}
