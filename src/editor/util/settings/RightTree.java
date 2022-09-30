package editor.util.settings;

import dream.util.contain.Containable;
import editor.util.Controls;
import imgui.ImGui;
import imgui.flag.ImGuiComboFlags;
import imgui.type.ImInt;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class RightTree
{
    private final static Map<String, Runnable> contents = new HashMap<>();

    public static void initialize()
    {
        RightTree.contents.put("Culling", culling());
        RightTree.contents.put("Face Mode", faceMode());
    }

    public static Runnable getRightContent(Containable<String> selection)
    {
        String name = selection.getName();
        return RightTree.contents.getOrDefault(name, () -> {});
    }

    private static Runnable culling()
    {
        return new Runnable()
        {
            private final ImInt cullFace = new ImInt(1);
            private final String[] cullOptions = new String[] { "Front Face", "Back Face", "Both Faces" };
            private boolean activate = false;

            @Override
            public void run()
            {
                ImGui.textWrapped("Culling is a property in which certain faces of a mesh are not rendered. " +
                        "Activating this property optimizes the renderer and increases performance a bit!");
                ImGui.newLine();

                this.activate = Controls.drawBooleanControl(this.activate, "Cull Face");

                if(!this.activate)
                {
                    ImGui.beginDisabled();
                    glDisable(GL_CULL_FACE);
                }

                int prevValue = this.cullFace.get();
                ImGui.combo("##cullOptions", this.cullFace, this.cullOptions, ImGuiComboFlags.NoArrowButton);

                if(prevValue != this.cullFace.get())
                {
                    if(this.activate)
                    {
                        glEnable(GL_CULL_FACE);
                        if(this.cullFace.get() == 0)
                            glCullFace(GL_FRONT);
                        else if(this.cullFace.get() == 1)
                            glCullFace(GL_BACK);
                        else if(this.cullFace.get() == 2)
                            glCullFace(GL_FRONT_AND_BACK);
                    }
                }

                if(!this.activate)
                    ImGui.endDisabled();
            }
        };
    }

    private static Runnable faceMode()
    {
        return new Runnable()
        {
            private final ImInt face = new ImInt(1);
            private final String[] cullOptions = new String[] { "Wireframe", "Solid" };
            private boolean activate = false;

            @Override
            public void run()
            {
                ImGui.textWrapped("Face Mode simply dictates how the faces of a mesh are rendered.");
                ImGui.newLine();

                this.activate = Controls.drawBooleanControl(this.activate, "Face Mode");

                if(!this.activate)
                    ImGui.beginDisabled();

                int prevValue = this.face.get();
                ImGui.combo("##faceOptions", this.face, this.cullOptions, ImGuiComboFlags.NoArrowButton);

                if(prevValue != this.face.get())
                {
                    if(this.activate)
                    {
                        if(this.face.get() == 0)
                            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                        else if (this.face.get() == 1)
                            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                    }
                }

                if(!this.activate)
                    ImGui.endDisabled();
            }
        };
    }
}
