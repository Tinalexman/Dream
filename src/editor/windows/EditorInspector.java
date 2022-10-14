package editor.windows;

import dream.components.Component;
import dream.components.material.Material;
import dream.components.mesh.Mesh;
import dream.components.mesh.MeshRenderer;
import dream.components.transform.Transform;
import dream.graphics.icon.Icons;
import dream.graphics.texture.Texture;
import dream.light.DirectionalLight;
import dream.light.Light;
import dream.light.PointLight;
import dream.light.SpotLight;
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

public class EditorInspector extends EditorWindow
{
    private Join<Node> node;
    private Join<Light> light;

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

    public void setNode(Join<Node> node)
    {
        this.node = node;
    }

    public void setLight(Join<Light> light)
    {
        this.light = light;
    }

    @Override
    public void show()
    {
        this.isActive = ImGui.begin(this.title, this.windowFlags);
        float width = ImGui.getContentRegionAvail().x,  height = ImGui.getContentRegionAvail().y;
        showMenuBar();
        ImGui.beginChild("##inspector", width, height, true, ImGuiWindowFlags.HorizontalScrollbar);

        showComponents();
        showLights();

        ImGui.endChild();
        ImGui.end();
    }

    private void showLights()
    {
        Light light;
        if((light = this.light.value) == null)
            return;

        light(light);
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
        Node selected;
        if((selected = this.node.value) == null)
            return;

        boolean visibility = Controls.drawBooleanControl(selected.isVisible(), "Visibility");
        selected.isVisible(visibility);

        ImGui.pushStyleColor(ImGuiCol.Header, 0, 0, 0, 0);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, 0, 0, 0, 0);
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, 0, 0, 0, 0);
        for (Component c : selected.getComponents())
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

    private void light(Light light)
    {
        light.active = Controls.drawBooleanControl(light.active, "Visibility");

        if(light instanceof DirectionalLight directionalLight)
        {
            ImGui.text("Direction:");
            ImGui.sameLine();
            Controls.drawVector3Control("##pos", directionalLight.direction);
        }
        else
        {
            ImGui.text("Position:");
            ImGui.sameLine();
            Controls.drawVector3Control("##pos", light.position);
        }

        ImGui.text("Ambience:");
        ImGui.sameLine();
        Controls.colorPicker3("##ambient", light.ambient);

        ImGui.text("Diffuse:");
        ImGui.sameLine();
        Controls.colorPicker3("##diffuse", light.diffuse);

        ImGui.text("Specular:");
        ImGui.sameLine();
        Controls.colorPicker3("##specular", light.specular);

        if(light instanceof PointLight pointLight)
        {
            float[] val = { pointLight.constant };
            boolean res = Controls.dragFloat("Constant", val);
            if(res)
                pointLight.constant = val[0];

            val[0] = pointLight.linear;
            res = Controls.dragFloat("Linear", val);
            if(res)
                pointLight.linear = val[0];

            val[0] = pointLight.quadratic;
            res = Controls.dragFloat("Quadratic" ,val);
            if(res)
                pointLight.quadratic = val[0];
        }
        else if(light instanceof SpotLight spotLight)
        {
            float[] val = { spotLight.cutoff };
            boolean res = Controls.dragFloat("CutOff", val);
            if(res)
                spotLight.cutoff = val[0];

            val[0] = spotLight.outerCutoff;
            res = Controls.dragFloat("Outer CutOff", val);
            if(res)
                spotLight.outerCutoff = val[0];
        }
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
        ImGui.text("Diffuse Color:");
        ImGui.sameLine();
        Controls.colorPicker3("##diffuse", material.diffuse);

        ImGui.text("Specular Color:");
        ImGui.sameLine();
        Controls.colorPicker3("##specular", material.specular);

        float width = 150.0f;

        if(material.hasDiffuse())
        {
            Texture diffuse = material.getPack().diffuse;
            int index = diffuse.filePath.lastIndexOf("\\");
            String textureName = diffuse.filePath.substring(index + 1);

            ImGui.text("Diffuse Map:");
            ImGui.sameLine();
            ImGui.pushItemWidth(width);
            ImGui.combo("##" + textureName, new ImInt(1), new String[]{"None", textureName},
                    ImGuiComboFlags.NoArrowButton);
            ImGui.popItemWidth();

            if(ImGui.isItemHovered())
            {
                ImGui.beginTooltip();
                ImGui.text(textureName);
                ImGui.separator();
                ImGui.sameLine(10.0f);
                ImGui.image(diffuse.ID, 80.0f, 80.0f, 0, 1, 1, 0);
                ImGui.endTooltip();
            }
        }

        if(material.hasSpecular())
        {
            Texture specular = material.getPack().specular;
            int index = specular.filePath.lastIndexOf("\\");
            String textureName = specular.filePath.substring(index + 1);

            ImGui.text("Specular Map:");
            ImGui.sameLine();
            ImGui.pushItemWidth(width);
            ImGui.combo("##" + textureName, new ImInt(1), new String[]{"None", textureName},
                    ImGuiComboFlags.NoArrowButton);
            ImGui.popItemWidth();

            if(ImGui.isItemHovered())
            {
                ImGui.beginTooltip();
                ImGui.text(textureName);
                ImGui.separator();
                ImGui.newLine();
                ImGui.sameLine(10.0f);
                ImGui.image(specular.ID, 80.0f, 80.0f, 0, 1, 1, 0);
                ImGui.endTooltip();
            }
        }

    }
}
