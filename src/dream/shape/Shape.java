package dream.shape;

import dream.components.mesh.Mesh;
import dream.components.mesh.MeshFactory;
import dream.components.transform.Transform;
import dream.node.Node3D;


public class Shape extends Node3D
{

    public Shape()
    {
        super("Shape");

        super.addComponent(new Transform());
        Mesh mesh = new Mesh();
        MeshFactory.asPlane(mesh);
        super.addComponent(mesh);
    }
}
