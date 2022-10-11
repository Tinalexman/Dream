package editor.windows;

import dream.components.Component;
import dream.components.material.Material;
import dream.components.mesh.Mesh;
import dream.components.mesh.MeshRenderer;
import dream.components.transform.Transform;
import dream.graphics.icon.Icons;
import dream.managers.ResourcePool;
import dream.node.Node;
import dream.util.collection.Join;
import editor.util.Controls;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiComboFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import org.joml.Vector3f;

import java.util.List;

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

        ImGui.pushStyleColor(ImGuiCol.Header, 0, 0, 0, 0);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, 0, 0, 0, 0);
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, 0, 0, 0, 0);
        for (Component c : this.selection.value.getComponents())
        {
            if (ImGui.collapsingHeader(c.getClass().getSimpleName(), ImGuiTreeNodeFlags.DefaultOpen))
            {
                if (c.getClass().isAssignableFrom(Transform.class))
                    transform((Transform) c);
                else if (c.getClass().isAssignableFrom(Material.class))
                    material((Material) c);
                else if (c.getClass().isAssignableFrom(Mesh.class))
                    mesh((Mesh) c);
                else if (c.getClass().isAssignableFrom(MeshRenderer.class))
                    meshRenderer((MeshRenderer) c);
            }
        }
        ImGui.popStyleColor(3);
    }

    private void mesh(Mesh mesh)
    {
        int[] meshProperties = mesh.properties();
        float indent = 10.0f;

        ImGui.text("Vertices: ");
        ImGui.indent(indent);

        ImGui.text("Position: " + meshProperties[0] + " ( " +
                (Float.BYTES * meshProperties[0]) + " bytes )");
        ImGui.text("UV: " + meshProperties[1] + " ( " +
                (Float.BYTES * meshProperties[1]) + " bytes )");
        ImGui.text("Normals: " + meshProperties[2] + " ( " +
                (Float.BYTES * meshProperties[2]) + " bytes )");

        ImGui.unindent(indent);

        ImGui.text("Indices: " + mesh.count() + " ( "
                + (Integer.BYTES * mesh.count()) + " bytes )");
        ImGui.indent(indent);

        ImGui.text("1 subMesh:");
        ImGui.text("#0 " + (mesh.count() / 3) + " triangles ( " + mesh.count()
                + " indices starting from 0 )");

        ImGui.unindent(indent);
    }

    private void meshRenderer(MeshRenderer renderer)
    {
        float width = 120.0f;
        ImGui.text("Culling:");
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, 66, 150, 250, 102);
        ImGui.sameLine();
        ImInt selection = new ImInt(renderer.getCull());
        ImGui.pushItemWidth(width);
        boolean res = ImGui.combo("##cull", selection, MeshRenderer.Configuration.cullOptions,
                ImGuiComboFlags.NoArrowButton);
        ImGui.popItemWidth();
        ImGui.sameLine();
        Controls.drawHelpMarker("Culling determines which faces of a mesh are rendered.");
        if(res)
            renderer.setCull(selection.get());

        ImGui.text("Face Mode:");
        ImGui.sameLine();
        selection.set(renderer.getFace());
        ImGui.pushItemWidth(width);
        res = ImGui.combo("##face", selection, MeshRenderer.Configuration.faceOptions,
                ImGuiComboFlags.NoArrowButton);
        ImGui.popItemWidth();
        ImGui.sameLine();
        Controls.drawHelpMarker("Face Mode determines how the faces of a mesh are rendered.");
        if(res)
            renderer.setFace(selection.get());
        ImGui.popStyleColor();
    }

    private void transform(Transform transform)
    {
        ImGui.text("Position:");
        boolean pos = Controls.drawVector3Control("##pos", transform.getPosition());

        ImGui.text("Rotation:");
        boolean rot = Controls.drawVector3Control("##rot", transform.getOrientation());
        Vector3f orientation = transform.getOrientation();
        orientation.x %= 360.0;
        orientation.y %= 360.0;
        orientation.z %= 360.0;

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
