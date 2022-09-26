package dream.shape;

import dream.components.mesh.Mesh;
import dream.components.mesh.MeshFactory;
import dream.components.transform.Transform3D;
import dream.node.Node3D;


public class Shape extends Node3D
{

    public Shape()
    {
        super("Shape");

        super.addComponent(new Transform3D());
        Mesh mesh = new Mesh();
        MeshFactory.asPlane(mesh);
        super.addComponent(mesh);
    }
}
