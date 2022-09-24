package editor;

import dream.managers.WindowManager;
import editor.windows.*;
import editor.windows.EditorWindow;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Editor
{
    private final Gui gui;
    private final List<EditorWindow> editorWindows;

    public Editor(long windowID)
    {
        this.gui = new Gui();
        this.gui.create(windowID);
        this.editorWindows = new ArrayList<>();

        this.editorWindows.add(new EditorViewport());
        this.editorWindows.add(new EditorSceneGraph());
        this.editorWindows.add(new EditorInspector());

        EditorSettings settings = new EditorSettings();
        this.editorWindows.add(settings);

        this.editorWindows.add(new EditorWindow("Output"));

        this.gui.addMenuBarCallBack("Engine Settings", settings::activate);
        this.gui.addMenuBarCallBack("Quit", WindowManager::closeMain);
    }

    public void refresh()
    {
        this.gui.start();

        this.editorWindows.forEach(EditorWindow::show);
        //ImGui.showStyleEditor();

        this.gui.end();
    }

    public void input()
    {

    }

    public void destroy()
    {
        this.gui.destroy();
    }

}
