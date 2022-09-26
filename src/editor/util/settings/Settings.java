package editor.util.settings;

import dream.events.EventManager;
import dream.events.type.EditorSettings;
import dream.util.contain.Containable;
import imgui.ImGui;
import imgui.flag.ImGuiComboFlags;
import imgui.type.ImInt;

import java.util.HashMap;
import java.util.Map;

public class Settings
{
    private final static Map<String, Runnable> contents = new HashMap<>();

    public static void initialize()
    {
        Settings.contents.put("Rendering", rendering());
    }

    public static Runnable getRightContent(Containable<String> selection)
    {
        String name = selection.getName();
        return Settings.contents.getOrDefault(name, () -> {});
    }

    private static Runnable rendering()
    {
        return new Runnable()
        {
            private ImInt cullFace = new ImInt(0);
            private String[] cullOptions = new String[] { "None", "Front Face", "Back Face", "Both Faces" };

            @Override
            public void run()
            {
                ImGui.text("Cull Face");
                ImGui.sameLine();
                int prevValue = this.cullFace.get();
                ImGui.combo("##cullOptions", this.cullFace, this.cullOptions, ImGuiComboFlags.NoArrowButton);
                if(prevValue != this.cullFace.get())
                    EventManager.push(new EditorSettings(() ->
                    {

//                        if(this.cullFaces.val)
//                        {
//                            glEnable(GL_CULL_FACE);
//                            if(this.cullMode.val.equals(SceneRenderer.cullModes[0]))
//                                GL11.glCullFace(GL_FRONT);
//                            else if(this.cullMode.val.equals(SceneRenderer.cullModes[1]))
//                                GL11.glCullFace(GL_BACK);
//                            else if(this.cullMode.val.equals(SceneRenderer.cullModes[2]))
//                                GL11.glCullFace(GL_FRONT_AND_BACK);
//                        }
//                        else
//                            glDisable(GL_CULL_FACE);
                    }));

            }
        };
    }
}
