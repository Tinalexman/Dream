package editor.windows;

import dream.light.Light;
import dream.node.Node;
import dream.scene.Scene;
import dream.util.collection.Join;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;

import java.util.List;

public class EditorSceneGraph extends EditorWindow
{
    private Scene scene;
    private final Join<Node> node;
    private final Join<Light> light;

    public EditorSceneGraph()
    {
        super("SceneGraph");
        this.node = new Join<>();
        this.light = new Join<>();
    }

    public void setScene(Scene scene)
    {
        this.scene = scene;
        this.node.value = scene.getRoot();
    }

    public Join<Node> getNode()
    {
        return this.node;
    }

    public Join<Light> getLight()
    {
        return this.light;
    }

    @Override
    public void show()
    {
        this.isActive = ImGui.begin(this.title, this.windowFlags);
        float width = ImGui.getContentRegionAvail().x, height = ImGui.getContentRegionAvail().y;

        ImGui.beginChild("##graph", width, height, true, ImGuiWindowFlags.HorizontalScrollbar);

        showGroupNodes();
        contextMenu();

        ImGui.endChild();
        ImGui.end();
    }

    private void contextMenu()
    {
        if(ImGui.beginPopupContextWindow())
        {
            ImGui.menuItem("Add Child");

            ImGui.endPopup();
        }
    }

    private void showGroupNodes()
    {
        showNodes(this.scene.getRoot());
        showLights(this.scene.getLights());
    }

    private void showNodes(Node root)
    {
        if(root == null)
            return;

        int groupFlag = ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.DefaultOpen;

        if(this.node.value != null && this.node.value.equals(root))
            groupFlag |= ImGuiTreeNodeFlags.Selected;

        ImGui.pushItemWidth(50.0f);

        boolean groupOpen = ImGui.treeNodeEx(root.hashCode(), groupFlag, "Nodes");
        if(ImGui.isItemClicked() || ImGui.isItemToggledOpen())
        {
            this.node.value = root;
            this.light.value = null;
        }

        ImGui.popItemWidth();

        if(groupOpen)
        {
            List<Node> nodes = root.getChildren();
            for(int i = 0; i < nodes.size(); ++i)
            {
                Node child = nodes.get(i);
                int flags = ImGuiTreeNodeFlags.Leaf | ImGuiTreeNodeFlags.NoTreePushOnOpen | ImGuiTreeNodeFlags.Bullet;
                if(this.node.value != null && this.node.value.equals(child))
                    flags |= ImGuiTreeNodeFlags.Selected;
                ImGui.treeNodeEx(i, flags, child.getName());
                if(ImGui.isItemClicked() && !ImGui.isItemToggledOpen())
                {
                    this.node.value = child;
                    this.light.value = null;
                }
            }
            ImGui.treePop();
        }
    }

    private void showLights(List<Light> lights)
    {
        if(ImGui.treeNodeEx(lights.hashCode(), ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.DefaultOpen, "Lights"))
        {
            for(int i = 0; i < lights.size(); ++i)
            {
                Light child = lights.get(i);
                int flags = ImGuiTreeNodeFlags.Leaf | ImGuiTreeNodeFlags.NoTreePushOnOpen | ImGuiTreeNodeFlags.Bullet;
                if(this.light.value != null && this.light.value.equals(child))
                    flags |= ImGuiTreeNodeFlags.Selected;
                ImGui.treeNodeEx(i, flags, child.getClass().getSimpleName());
                if(ImGui.isItemClicked() && !ImGui.isItemToggledOpen())
                {
                    this.light.value = child;
                    this.node.value = null;
                }
            }
            ImGui.treePop();
        }
    }

}
