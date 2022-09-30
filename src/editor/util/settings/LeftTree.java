package editor.util.settings;

import dream.util.contain.Containable;
import dream.util.contain.Contained;
import dream.util.contain.Container;

public class LeftTree
{
    private static final Container<String> leftSettingsPane = new Container<>(null, null);

    public static void initialize()
    {
        Container<String> graphics = new Container<>("Graphics", "Change your Graphics Settings");

        Container<String> rendering = new Container<>("Rendering", "Change your Rendering Settings");
        Contained<String> culling = new Contained<>("Culling", null);
        rendering.add(culling);

        Contained<String> wireframe = new Contained<>("Face Mode", null);
        rendering.add(wireframe);

        graphics.add(rendering);


        LeftTree.leftSettingsPane.add(graphics);
    }

    public static Container<String> leftRoot()
    {
        return LeftTree.leftSettingsPane;
    }

    public static Containable<String> first()
    {
        return leftRoot().getItems().get(0);
    }
}
