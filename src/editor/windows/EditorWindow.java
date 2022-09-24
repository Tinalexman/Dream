package editor.windows;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class EditorWindow
{
    protected boolean isActive;
    protected String title;
    protected int windowFlags;

    public EditorWindow(String title)
    {
        this(title, ImGuiWindowFlags.None);
    }

    public EditorWindow(String title, int flags)
    {
        this.isActive = true;
        this.title = title;
        this.windowFlags = flags;
    }

    public void activate()
    {
        this.isActive = true;
    }

    public void deactivate()
    {
        this.isActive = false;
    }

    public void show()
    {
        ImGui.begin(this.title, this.windowFlags);

        ImGui.end();
    }
}
