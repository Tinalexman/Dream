package editor.windows;

import dream.events.Event;
import dream.events.EventManager;
import dream.events.EventType;
import dream.events.handler.Handler;
import dream.events.type.WindowResize;
import dream.graphics.icon.Icons;
import dream.graphics.mesh.Mesh;
import dream.io.FrameBuffer;
import dream.managers.ResourcePool;
import dream.managers.WindowManager;
import dream.scene.Scene;
import dream.shader.Shader;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;

public class EditorViewport extends EditorWindow implements Handler
{
    private FrameBuffer frameBuffer;
    private Shader shader;
    private Mesh mesh;

    private float previousWidth;
    private float previousHeight;
    private float previousPosX;
    private float previousPosY;

    private final int menuIcon;

    public EditorViewport()
    {
        super("Viewport");

        int[] size = WindowManager.getMainSize();
        this.frameBuffer = new FrameBuffer(size[0], size[1]);
        this.windowFlags = ImGuiWindowFlags.NoScrollbar |
                ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.MenuBar;
        this.menuIcon = ResourcePool.getIcon(Icons.menu);

        EventManager.add(EventType.WindowResize, this);
    }

    @Override
    public void respond(Event event)
    {
        if(event.type == EventType.WindowResize)
        {
            WindowResize w = (WindowResize) event;
            if(w.minimized())
                return;

            this.frameBuffer.destroy();
            this.frameBuffer = new FrameBuffer(w.width, w.height);
        }
    }

    @Override
    public void show()
    {
        this.isActive = ImGui.begin(this.title, windowFlags);

        ImVec2 framebufferSizeInWindow = getLargestSizeForViewPort();
        ImVec2 framebufferPositionInWindow = getCenteredPositionForViewPort(framebufferSizeInWindow);

        ImVec2 actualWindowPosition = ImGui.getWindowPos();
        ImVec2 actualWindowSize = ImGui.getWindowSize();

        ImGui.setCursorPos(framebufferPositionInWindow.x, framebufferPositionInWindow.y);

        ImGui.pushStyleColor(ImGuiCol.MenuBarBg, 0, 0, 0, 20);
        if(ImGui.beginMenuBar())
        {
            ImGui.pushStyleColor(ImGuiCol.Border, 0, 0, 0, 0);
            ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0, 0, 0, 20);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0, 0, 0, 0);

            ImGui.imageButton(this.menuIcon, 16.0f, 16.0f, 0.0f, 1.0f, 1.0f, 0.0f);
            ImGui.popStyleColor(4);
            ImGui.endMenuBar();
        }
        ImGui.popStyleColor();

        viewScene(null);

        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0.0f, 0.0f);
        ImGui.image(this.frameBuffer.getID(), framebufferSizeInWindow.x, framebufferSizeInWindow.y, 0, 1, 1, 0);
        ImGui.popStyleVar();

        if(hasChanged(actualWindowSize, actualWindowPosition))
        {
            this.previousWidth = actualWindowSize.x;
            this.previousHeight = actualWindowSize.y;

            this.previousPosX = actualWindowPosition.x;
            this.previousPosY = actualWindowPosition.y;
        }

        ImGui.end();
    }

    public FrameBuffer getFrameBuffer()
    {
        return this.frameBuffer;
    }

    private ImVec2 getCenteredPositionForViewPort(ImVec2 aspectSize)
    {
        ImVec2 windowSize = setup();
        float viewportX = (windowSize.x * 0.5f) - (aspectSize.x * 0.5f);
        float viewportY = (windowSize.y * 0.5f) - (aspectSize.y * 0.5f);
        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

    private ImVec2 getLargestSizeForViewPort()
    {
        ImVec2 windowSize = setup();
        float aspectRatio = WindowManager.getMainRatio();
        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / aspectRatio;
        if(aspectHeight > windowSize.y)
        {
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * aspectRatio;
        }
        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 setup()
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();
        return windowSize;
    }

    private boolean hasChanged(ImVec2 newWindowSize, ImVec2 newWindowPos)
    {
        boolean equalSize = (this.previousWidth == newWindowSize.x)
                && (this.previousHeight == newWindowSize.y);

        boolean samePosition = (this.previousPosX == newWindowPos.x)
                && (this.previousPosY == newWindowPos.y);

        return !(equalSize && samePosition);
    }

    public void viewScene(Scene scene)
    {
        this.frameBuffer.start();



        this.frameBuffer.stop();
    }
}
