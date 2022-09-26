package editor.util.settings;

import dream.util.contain.Containable;
import dream.util.contain.Contained;
import dream.util.contain.Container;

public class SettingsTree
{
    private static final Container<String> leftSettingsPane = new Container<>(null, null);

    public static void initialize()
    {
        Container<String> graphics = new Container<>("Graphics", "Change your Graphics Settings");

        Contained<String> rendering = new Contained<>("Rendering", null);
        graphics.add(rendering);


        SettingsTree.leftSettingsPane.add(graphics);
    }

    public static Container<String> leftRoot()
    {
        return SettingsTree.leftSettingsPane;
    }

    public static Containable<String> first()
    {
        return leftRoot().getItems().get(0);
    }
}
