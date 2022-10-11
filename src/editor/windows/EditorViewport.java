package editor.windows;

import dream.camera.Camera;
import dream.graphics.icon.Icons;
import dream.managers.ResourcePool;
import dream.managers.WindowManager;
import dream.scene.Scene;
import editor.renderer.Renderer;
import editor.util.Controls;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.*;
import imgui.type.ImInt;
import org.joml.Vector3f;

public class EditorViewport extends EditorWindow
{
    private final float[] position;
    private final float[] size;

    private final int menuIcon;

    private final Renderer renderer;


    public EditorViewport()
    {
        this("Viewport");
    }

    public EditorViewport(String name)
    {
        super(name);
        this.windowFlags = ImGuiWindowFlags.NoScrollbar |
                ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.MenuBar;
        this.menuIcon = ResourcePool.getIcon(Icons.menu);

        this.position = new float[] {0.0f, 0.0f};
        this.size = new float[] {0.0f, 0.0f};

        this.renderer = new Renderer(position, size);
    }

    @Override
    public void show()
    {
        this.renderer.render();

        this.isActive = ImGui.begin(this.title, windowFlags);
        this.isFocused = ImGui.isWindowFocused(ImGuiFocusedFlags.RootAndChildWindows);

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

            showMenu();

            ImGui.imageButton(this.menuIcon, 16.0f, 16.0f, 0.0f, 1.0f, 1.0f, 0.0f);
            ImGui.openPopupOnItemClick("viewportSettings", ImGuiPopupFlags.MouseButtonLeft);

            ImGui.popStyleColor(4);
            ImGui.endMenuBar();
        }
        ImGui.popStyleColor();

        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0.0f, 0.0f);
        ImGui.image(this.renderer.getID(), framebufferSizeInWindow.x, framebufferSizeInWindow.y, 0, 1, 1, 0);
        ImGui.popStyleVar();

        if(hasChanged(actualWindowSize, actualWindowPosition))
        {
            size[0] = framebufferSizeInWindow.x;
            size[1] = framebufferSizeInWindow.y;

            position[0] = actualWindowPosition.x + framebufferPositionInWindow.x;
            position[1] = actualWindowPosition.y + framebufferPositionInWindow.y;
        }

        ImGui.end();
    }

    @Override
    public void destroy()
    {
        this.renderer.destroy();
    }

    private void showMenu()
    {
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 1.0f);
        if(ImGui.beginPopupContextItem("viewportSettings"))
        {
            ImGui.textWrapped("Camera");
            ImGui.separator();
            this.renderer.useCamera = Controls.drawBooleanControl(this.renderer.useCamera, "Use Camera");
            ImGui.beginDisabled(!this.renderer.useCamera);

            Camera cam = this.renderer.camera;
            Vector3f pos = cam.getPosition();

            ImGui.text("Position: ");
            ImGui.sameLine();
            ImGui.text(pos.x + "\t" + pos.y + "\t" + pos.z);

            ImGui.text("Pitch: " + cam.getPitch() + "\t\tYaw: " + cam.getYaw());

            ImGui.text("Field Of View: ");
            ImGui.sameLine();
            ImGui.text("" + cam.getFieldOfView());

            float[] arr = new float[] { cam.getNearPlane() };
            boolean res = Controls.dragFloat("Near Plane", arr);
            ImGui.sameLine();
            Controls.drawHelpMarker("This is the minimum distance from the camera in which an object can be seen.");
            if(res)
                this.renderer.camera.setNearPlane(Math.max(0.001f, arr[0]));

            arr = new float[] { cam.getFarPlane() };
            res = Controls.dragFloat("Far Plane", arr);
            ImGui.sameLine();
            Controls.drawHelpMarker("This is the maximum distance from the camera in which an object can be seen.");
            if(res)
                this.renderer.camera.setFarPlane(Math.min(1000000.0f, arr[0]));

            ImGui.endDisabled();

            ImGui.newLine();

            ImGui.textWrapped("Options");
            ImGui.separator();
            ImGui.pushStyleColor(ImGuiCol.Button, 66, 150, 250, 102);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 66, 150, 250, 255);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 15, 135, 250, 250);
            ImGui.button("New Viewport From Scene");
            ImGui.button("Delete Current Viewport");
            ImGui.popStyleColor(3);

            ImGui.endPopup();
        }
        ImGui.popStyleVar();
    }

    public void setScene(Scene scene)
    {
        this.renderer.setScene(scene);
    }

    @Override
    public void input()
    {
        if(this.isActive && this.isFocused)
            this.renderer.input();
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
        boolean equalSize = (this.size[0] == newWindowSize.x)
                && (this.size[1] == newWindowSize.y);

        boolean samePosition = (this.position[0] == newWindowPos.x)
                && (this.position[1] == newWindowPos.y);

        return !(equalSize && samePosition);
    }


    //    private void postProcess()
//    {
//        if(this.currentFilter.val.equals("None"))
//        {
//            this.framebufferJoin.val = this.colorBuffer.getTextureID();
//            return;
//        }
//
//        this.framebufferJoin.val = this.postProcessBuffer.getTextureID();
//
//        this.postProcessBuffer.onEnable();
//        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
//        glClear(GL_COLOR_BUFFER_BIT);
//
//        Shader shader = Filter.filters.get(this.currentFilter.val);
//        shader.start();
//        postProcessMesh.onEnable();
//
//        glDisable(GL_DEPTH_TEST);
//        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, this.colorBuffer.getTextureID());
//        glDrawElements(GL_TRIANGLES, postProcessMesh.getVertexCount(), GL_UNSIGNED_INT, 0);
//
//        postProcessMesh.onDisable();
//        shader.stop();
//
//        this.postProcessBuffer.onDisable();
//    }
}

