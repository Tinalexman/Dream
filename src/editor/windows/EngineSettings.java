package editor.windows;


import dream.util.contain.Containable;
import dream.util.contain.Container;
import editor.util.settings.Settings;
import editor.util.settings.SettingsTree;
import editor.windows.modal.Modal;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;

import java.util.List;

public class EngineSettings extends Modal<Void>
{
    private final ImString filteredText = new ImString();
    private final int defaultTreeFlags = ImGuiTreeNodeFlags.OpenOnArrow
        | ImGuiTreeNodeFlags.OpenOnDoubleClick
        | ImGuiTreeNodeFlags.DefaultOpen;

    private Containable<String> selected = SettingsTree.first();

    public EngineSettings()
    {
        super("Settings", "Change the settings of the engine.");
        this.isActive = false;
    }

    @Override
    public void drawTopSheet()
    {
        ImVec2 size = ImGui.getContentRegionAvail();
        ImGui.pushItemWidth(size.x);
        ImGui.inputTextWithHint("##settings", "Search In Settings", this.filteredText);
        ImGui.spacing();
        ImGui.popItemWidth();
    }

    @Override
    public void drawContent()
    {
        // Left Window
        ImVec2 size = ImGui.getContentRegionAvail();

        ImGui.beginChild("##leftWindow", size.x * 0.35f, size.y, true, ImGuiWindowFlags.HorizontalScrollbar);
        showLeftPane(SettingsTree.leftRoot());

        ImGui.endChild();

        ImGui.sameLine(size.x * 0.4f);

        ImGui.beginChild("##rightWindow", size.x * 0.6f, size.y, true, ImGuiWindowFlags.HorizontalScrollbar);
        showRightPane();
        ImGui.endChild();

    }

    @Override
    public void drawBottomSheet()
    {
        ImGui.text("DESCRIPTION");
        ImGui.spacing();


        ImGui.spacing();

        if (ImGui.button("Cancel", 120.0f, 0.0f))
        {
            deactivate();
            ImGui.closeCurrentPopup();
        }
        ImGui.sameLine();

        if (ImGui.button("Apply", 120.0f, 0.0f))
        {
            deactivate();
            ImGui.closeCurrentPopup();
        }
        ImGui.setItemDefaultFocus();

    }

    private void showLeftPane(Container<String> root)
    {
        List<Containable<String>> children = root.getItems();

        for (int i = 0; i < children.size(); i++)
        {
            int flags = defaultTreeFlags;
            Containable<String> currentNode = children.get(i);
            boolean isSelected = currentNode.equals(this.selected);
            if (isSelected)
                flags |= ImGuiTreeNodeFlags.Selected;

            Container<String> container = null;
            boolean isContainer = currentNode.isContainer();
            boolean hasChildren = isContainer && (container = (Container<String>) currentNode).hasChildren();

            if(!hasChildren)
                flags |= ImGuiTreeNodeFlags.Leaf | ImGuiTreeNodeFlags.NoTreePushOnOpen | ImGuiTreeNodeFlags.Bullet;

            boolean nodeOpen = ImGui.treeNodeEx(i, flags, currentNode.getName());
            if (ImGui.isItemClicked() && !ImGui.isItemToggledOpen())
                this.selected = currentNode;

            if(hasChildren && nodeOpen)
            {
                showLeftPane(container);
                ImGui.treePop();
            }
        }
    }

    private void showRightPane()
    {
        if(this.selected.isContainer())
            ImGui.text(this.selected.getValue());
        else
            Settings.getRightContent(this.selected).run();
    }
}

// CULL FACE
// WIREFRAME OR SOLID
// POST PROCESSING

//protected static Modal<Node> addNodes()
//    {
//        return  new Modal<>("Add Node", "Add a new node to your scene!")
//        {
//            private Container<String> root = new Container<>(Node.getNodeTree());
//            private String selectedNodeDescription = null;
//            private String selectedNode = null;
//            private final ImString filteredText = new ImString();
//            private final int defaultTreeFlags = ImGuiTreeNodeFlags.OpenOnArrow
//                | ImGuiTreeNodeFlags.OpenOnDoubleClick
//                | ImGuiTreeNodeFlags.DefaultOpen;
//
//            private void filterNodes()
//            {
//                this.root.clear();
//                this.root = searchNode(Node.getNodeTree(), this.filteredText.get());
//            }
//
//            private Container<String> searchNode(Container<String> root, String filter)
//            {
//                Container<String> result = new Container<>(root.getName(), root.getValue());
//                for(Containable<String> containable : root.getItems())
//                {
//                    if(containable instanceof Container<String> container)
//                    {
//                        Container<String> filtered = searchNode(container, filter);
//                        if(filtered.size() > 0)
//                            result.add(filtered);
//                    }
//                    else if(containable instanceof Contained<String> contained)
//                    {
//                        if(contained.getName().contains(filter))
//                            result.add(contained);
//                    }
//                }
//                return result;
//            }
//
//            private void showRootNode()
//            {
//                if(this.root.size() == 0)
//                {
//                    String description = "Invalid Search Parameters";
//                    ImVec2 size = ImGui.getContentRegionAvail();
//                    ImVec2 textSize = new ImVec2();
//                    ImGui.calcTextSize(textSize, description);
//                    ImGui.setCursorPos((size.x - textSize.x) * 0.5f, size.y * 0.5f);
//                    ImGui.text(description);
//                    this.selectedNode = null;
//                    return;
//                }
//
//                int flags = this.defaultTreeFlags;
//                boolean isSelected = this.root.getName().equals(this.selectedNode);
//                if (isSelected)
//                    flags |= ImGuiTreeNodeFlags.Selected;
//
//                boolean hasChildren = this.root.size() > 0;
//                if(!hasChildren)
//                    flags |= ImGuiTreeNodeFlags.Leaf | ImGuiTreeNodeFlags.NoTreePushOnOpen | ImGuiTreeNodeFlags.Bullet;
//
//                boolean nodeOpen = ImGui.treeNodeEx(root.hashCode(), flags, root.getName());
//                if (ImGui.isItemClicked() && !ImGui.isItemToggledOpen())
//                {
//                    this.selectedNode = root.getName();
//                    this.selectedNodeDescription = root.getValue();
//                }
//
//                if(hasChildren && nodeOpen)
//                {
//                    showFilteredNodes(root.getItems());
//                    ImGui.treePop();
//                }
//            }
//
//            private void showFilteredNodes(List<Containable<String>> nodes)
//            {
//                for (int i = 0; i < nodes.size(); i++)
//                {
//                    int flags = defaultTreeFlags;
//                    Containable<String> currentNode = nodes.get(i);
//                    boolean isSelected = currentNode.getName().equals(this.selectedNode);
//                    if (isSelected)
//                        flags |= ImGuiTreeNodeFlags.Selected;
//
//                    Container<String> container = null;
//                    boolean isContainer = currentNode.isContainer();
//                    boolean hasChildren = isContainer && (container = (Container<String>) currentNode).getItems().size() > 0;
//
//                    if(!hasChildren)
//                        flags |= ImGuiTreeNodeFlags.Leaf | ImGuiTreeNodeFlags.NoTreePushOnOpen | ImGuiTreeNodeFlags.Bullet;
//
//                    boolean nodeOpen = ImGui.treeNodeEx(i, flags, currentNode.getName());
//                    if (ImGui.isItemClicked() && !ImGui.isItemToggledOpen())
//                    {
//                        this.selectedNode = currentNode.getName();
//                        this.selectedNodeDescription = currentNode.getValue();
//                    }
//
//                    if(hasChildren && nodeOpen)
//                    {
//                        showFilteredNodes(container.getItems());
//                        ImGui.treePop();
//                    }
//                }
//            }
//
//            @Override
//            public void drawTopSheet()
//            {
//                ImVec2 size = ImGui.getContentRegionAvail();
//                ImGui.pushItemWidth(size.x);
//                ImGui.inputTextWithHint("##label", "Search Node", this.filteredText);
//                ImGui.spacing();
//                ImGui.popItemWidth();
//            }
//
//            @Override
//            public float getBottomHeight()
//            {
//                return 100.0f;
//            }
//
//            @Override
//            public void drawContent()
//            {
//                ImGui.pushItemWidth(this.childSize.x - 10.0f);
//                filterNodes();
//                showRootNode();
//                this.selection = NodeManager.createNode(this.selectedNode);
//                ImGui.popItemWidth();
//            }
//
//            @Override
//            public void drawBottomSheet()
//            {
//                ImGui.text("DESCRIPTION");
//                ImGui.spacing();
//
//                if(this.selectedNodeDescription != null)
//                    ImGui.text(this.selectedNodeDescription);
//
//                ImGui.spacing();
//
//                if (ImGui.button("Cancel", 120.0f, 0.0f))
//                {
//                    this.listener.onCancelButton();
//                    ImGui.closeCurrentPopup();
//                }
//                ImGui.sameLine();
//
//                if (ImGui.button("Add Node", 120.0f, 0.0f))
//                {
//                    this.listener.onAcceptButton();
//                    ImGui.closeCurrentPopup();
//                }
//                ImGui.setItemDefaultFocus();
//            }
//
//        };
//    }
