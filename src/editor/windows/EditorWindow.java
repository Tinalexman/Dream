package editor.windows;

import imgui.ImGui;
import imgui.ImVec2;
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

    public void drawWindowTiles(int[] IDs, String[] titles)
    {
        if(IDs.length != titles.length)
            return;

        ImVec2 windowPosition = ImGui.getWindowPos();
        ImVec2 windowSize = ImGui.getWindowSize();
        ImVec2 itemSpacing = ImGui.getStyle().getItemSpacing();

        float windowX2 = windowPosition.x + windowSize.x;
        float winSize = 70.0f, imgSize = 40.0f;

        int numberOfTiles = IDs.length;

        for(int i = 0; i < numberOfTiles; i++)
        {
            ImGui.beginChild("##tile" + i, winSize, winSize, false , ImGuiWindowFlags.NoScrollbar);

            ImGui.sameLine(15.0f);
            ImGui.image(IDs[i], imgSize, imgSize, 0, 1, 1, 0);
            ImGui.newLine();

            ImVec2 size = new ImVec2();
            ImGui.calcTextSize(size, titles[i]);
            ImGui.sameLine((70 - size.x) * 0.5f);
            ImGui.textWrapped(titles[i]);
            ImGui.endChild();

            ImVec2 lastPos = ImGui.getItemRectMax();
            float nextX = lastPos.x + itemSpacing.x + winSize;
            if((i + 1) < numberOfTiles && nextX < windowX2)
                ImGui.sameLine();

        }
    }

}
