package editor.util.components;

import dream.components.Component;
import dream.components.material.Material;
import dream.components.mesh.MeshRenderer;
import dream.components.transform.Transform;
import dream.util.collection.Pair;

import java.util.ArrayList;
import java.util.List;

public class ComponentTree
{
    public static final List<Pair<String, String>> components = new ArrayList<>();

    public static void initialize()
    {
        ComponentTree.components.add(new Pair<>("Transform", ""));
        ComponentTree.components.add(new Pair<>("Material", ""));
        ComponentTree.components.add(new Pair<>("Mesh Renderer", ""));
    }

    public static List<Pair<String, String>> getComponents()
    {
        return ComponentTree.components;
    }

    public static Component getComponent(String componentClass)
    {
        return switch (componentClass)
        {
            case "Transform" -> new Transform();
            case "Material" -> new Material();
            case "Mesh Renderer" -> new MeshRenderer();
            default -> throw new IllegalStateException("Unexpected value: " + componentClass);
        };
    }
}
