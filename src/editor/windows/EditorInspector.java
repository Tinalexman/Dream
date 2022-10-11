package editor.windows;

import dream.components.Component;
import dream.components.material.Material;
import dream.components.mesh.MeshRenderer;
import dream.components.transform.Transform;
import dream.graphics.icon.Icons;
import dream.managers.ResourcePool;
import dream.node.Node;
import dream.util.collection.Join;
import editor.util.Controls;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector3f;

public class EditorInspector extends EditorWindow
{
    private Join<Node> selection;
    private Runnable onAddComponent;

    public EditorInspector()
    {
        super("Inspector");
        this.windowFlags |= ImGuiWindowFlags.MenuBar;
    }

    public void setOnAddComponent(Runnable onAddComponent)
    {
        this.onAddComponent = onAddComponent;
    }

    public void setSelection(Join<Node> selection)
    {
        this.selection = selection;
    }

    @Override
    public void show()
    {
        this.isActive = ImGui.begin(this.title, this.windowFlags);
        float width = ImGui.getContentRegionAvail().x,  height = ImGui.getContentRegionAvail().y;
        showMenuBar();
        ImGui.beginChild("##inspector", width, height, true, ImGuiWindowFlags.HorizontalScrollbar);

        showComponents();

        ImGui.endChild();
        ImGui.end();
    }

    private void showMenuBar()
    {
        ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0, 0, 0, 0);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0, 0, 0, 0);

        if(ImGui.beginMenuBar())
        {
            float iconSize = 16.0f;
            if(ImGui.imageButton(ResourcePool.getIcon(Icons.plus), iconSize,
                    iconSize, 0, 1, 1, 0))
                this.onAddComponent.run();

            ImGui.imageButton(ResourcePool.getIcon(Icons.minus), iconSize, iconSize, 0, 1, 1, 0);

            ImGui.endMenuBar();
        }

        ImGui.popStyleColor(3);
    }


    private void showComponents()
    {
        if(this.selection.value == null)
            return;

        for (Component c : this.selection.value.getComponents())
        {
            if (ImGui.collapsingHeader(c.getClass().getSimpleName(), ImGuiTreeNodeFlags.DefaultOpen))
            {
                if (c.getClass().isAssignableFrom(Transform.class))
                    transform((Transform) c);
                else if (c.getClass().isAssignableFrom(Material.class))
                    material((Material) c);
                else if (c.getClass().isAssignableFrom(MeshRenderer.class))
                    meshRenderer((MeshRenderer) c);
            }
        }

    }

    private void meshRenderer(MeshRenderer renderer)
    {
        int[] meshProperties = renderer.meshProperties();

        ImGui.text("Vertices: " + meshProperties[0]);
        ImGui.text("Textures: " + meshProperties[1]);
        ImGui.text("Normals: " + meshProperties[2]);
        ImGui.text("Indices: " + meshProperties[3]);

        ImGui.text("Vertex Count: " + renderer.count());
    }


    private void transform(Transform transform)
    {
        ImGui.text("Position:");
        boolean pos = Controls.drawVector3Control("##pos", transform.getPosition());

        ImGui.text("Rotation:");
        boolean rot = Controls.drawVector3Control("##rot", transform.getOrientation());
        Vector3f or = transform.getOrientation();
        or.x %= 360.0;
        or.y %= 360.0;
        or.z %= 360.0;

        ImGui.text("Scale:");
        boolean sc = Controls.drawVector3Control("##sc", transform.getScale());

        transform.change(pos || rot || sc);
    }


    private void material(Material material)
    {
        ImGui.text("Ambient:");
        ImGui.sameLine();
        Controls.colorPicker3("##ambient", material.ambient);

        ImGui.text("Diffuse:");
        ImGui.sameLine();
        Controls.colorPicker3("##diffuse", material.diffuse);

        ImGui.text("Specular:");
        ImGui.sameLine();
        Controls.colorPicker3("##specular", material.specular);

        if(material.hasTexture())
        {
            ImGui.text("Texture:");
            ImGui.sameLine();
            ImGui.image(material.getPack().diffuse.ID, 32.0f, 32.0f, 0, 1, 1, 0);
        }

    }
}
