package editor.windows;

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
    private Join<Node> selection;

    public EditorSceneGraph()
    {
        super("SceneGraph");
        this.selection = new Join<>();
    }

    public void setScene(Scene scene)
    {
        this.scene = scene;
        this.selection.value = scene.getRoot();
    }

    public Join<Node> getSelection()
    {
        return this.selection;
    }

    @Override
    public void show()
    {
        this.isActive = ImGui.begin(this.title, this.windowFlags);
        float width = ImGui.getContentRegionAvail().x, height = ImGui.getContentRegionAvail().y;

        ImGui.beginChild("##graph", width, height, true, ImGuiWindowFlags.HorizontalScrollbar);

        showNodes();
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

    private void showNodes()
    {
        Node root;
        if(this.scene == null || (root = this.scene.getRoot()) == null)
            return;

        int groupFlag = ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.DefaultOpen;

        if(this.selection.value.equals(root))
            groupFlag |= ImGuiTreeNodeFlags.Selected;

        ImGui.pushItemWidth(50.0f);

        boolean groupOpen = ImGui.treeNodeEx(root.hashCode(), groupFlag, root.getName());
        if(ImGui.isItemClicked() || ImGui.isItemToggledOpen())
            this.selection.value = root;

        ImGui.popItemWidth();

        if(groupOpen)
        {
            List<Node> nodes = root.getChildren();
            for(int i = 0; i < nodes.size(); ++i)
            {
                Node child = nodes.get(i);
                int flags = ImGuiTreeNodeFlags.Leaf | ImGuiTreeNodeFlags.NoTreePushOnOpen | ImGuiTreeNodeFlags.Bullet;
                if(this.selection.value.equals(child))
                    flags |= ImGuiTreeNodeFlags.Selected;
                ImGui.treeNodeEx(i, flags, child.getName());
                if(ImGui.isItemClicked() && !ImGui.isItemToggledOpen())
                    this.selection.value = child;
            }
            ImGui.treePop();
        }
    }



}
