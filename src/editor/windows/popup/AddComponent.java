package editor.windows.popup;

import dream.node.Node;
import dream.util.collection.Join;
import dream.util.collection.Pair;
import editor.util.components.ComponentTree;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.ImInt;
import imgui.type.ImString;

import java.util.List;

public class AddComponent extends Popup
{
    private final ImString filteredText;
    private Pair<String, String> selectedPair;
    private Join<Node> selectedNode;
    private final ImInt selection;

    public AddComponent()
    {
        super("Add Component", "Attach a component to the currently selected node");
        this.filteredText = new ImString();
        this.selection = new ImInt(0);
        this.isActive = false;
    }

    public void setSelectedNode(Join<Node> selected)
    {
        this.selectedNode = selected;
    }

    @Override
    public void drawTopSheet()
    {
        ImVec2 size = ImGui.getContentRegionAvail();
        ImGui.pushItemWidth(size.x);
        ImGui.inputTextWithHint("##addComponent", "Search Component", this.filteredText);
        ImGui.spacing();
        ImGui.popItemWidth();
    }

    @Override
    public void drawContent()
    {
        if (ImGui.beginListBox("##components", -Float.MIN_VALUE, 5 * ImGui.getTextLineHeightWithSpacing()))
        {
            List<Pair<String, String>> pairs = ComponentTree.getComponents();
            this.selectedPair = pairs.get(0);

            for (int n = 0; n < pairs.size(); n++)
            {
                boolean isSelected = this.selection.get() == n;
                Pair<String, String> pair = pairs.get(n);
                if (ImGui.selectable(pair.first, isSelected))
                {
                    this.selection.set(n);
                    this.selectedPair = pair;
                }

                if (isSelected)
                    ImGui.setItemDefaultFocus();
            }
            ImGui.endListBox();
        }
    }

    @Override
    public void drawBottomSheet()
    {
        ImGui.text("DESCRIPTION");
        ImGui.spacing();

        if(this.selectedPair != null)
            ImGui.textWrapped(this.selectedPair.second);

        ImGui.spacing();

        if (ImGui.button("Cancel", 120.0f, 0.0f))
        {
            deactivate();
            ImGui.closeCurrentPopup();
        }
        ImGui.sameLine();

        if (ImGui.button("Select", 120.0f, 0.0f))
        {
            this.selectedNode.value.addComponent(ComponentTree.getComponent(this.selectedPair.first));
            deactivate();
            ImGui.closeCurrentPopup();
        }
        ImGui.setItemDefaultFocus();
    }

    @Override
    public void deactivate()
    {
        this.isActive = false;
        this.selection.set(-1);
    }

}
