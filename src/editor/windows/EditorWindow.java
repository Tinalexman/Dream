package editor.windows;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class EditorWindow
{
    protected boolean isActive;
    protected String title;
    protected int windowFlags;
    protected boolean isFocused;

    public EditorWindow(String title)
    {
        this(title, ImGuiWindowFlags.None);
    }

    public EditorWindow(String title, int flags)
    {
        this.isActive = true;
        this.title = title;
        this.windowFlags = flags;
        this.isFocused = true;
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

    public void input()
    {

    }

    public void destroy()
    {

    }

}
