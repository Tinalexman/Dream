package dream.components.transform;

import dream.components.Component;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform implements Component
{
    public Vector3f position;
    public Vector3f orientation;
    public Vector3f scale;
    public boolean changed;

    private final Matrix4f matrix;

    public Transform()
    {
        this.position = new Vector3f(0.0f);
        this.orientation = new Vector3f(0.0f);
        this.scale = new Vector3f(1.0f);
        this.matrix = new Matrix4f().identity();
        this.changed = true;
    }

    public Vector3f getPosition()
    {
        return this.position;
    }

    public void incrementPosition(float xPosition, float yPosition, float zPosition)
    {
        this.position.x += xPosition;
        this.position.y += yPosition;
        this.position.z += zPosition;
        this.changed = true;
    }

    public void setPosition(Vector3f position)
    {
        if(!this.position.equals(position))
        {
            this.position = position;
            this.changed = true;
        }
    }

    public Vector3f getOrientation()
    {
        return this.orientation;
    }

    public void incrementRotation(float xRotation, float yRotation, float zRotation)
    {
        this.orientation.x += xRotation;
        this.orientation.y += yRotation;
        this.orientation.z += zRotation;

        this.orientation.x %= 360.0f;
        this.orientation.y %= 360.0f;
        this.orientation.z %= 360.0f;

        this.changed = true;
    }

    public void reset()
    {
        this.position.set(0.0f);
        this.orientation.set(0.0f);
        this.scale.set(1.0f);
  }

    public void setOrientation(Vector3f orientation)
    {
        if(!this.orientation.equals(orientation))
        {
            this.orientation = orientation;
            this.changed = true;
        }
    }

    public Vector3f getScale()
    {
        return this.scale;
    }

    public void incrementScale(float xScale, float yScale, float zScale)
    {
        this.scale.x *= xScale;
        this.scale.y *= yScale;
        this.scale.z *= zScale;
        this.changed = true;
    }

    public void setScale(Vector3f scale)
    {
        if(!this.scale.equals(scale))
        {
            this.scale = scale;
            this.changed = true;
        }
    }

    public Matrix4f getMatrix()
    {
        return this.matrix;
    }

    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof Transform transform))
            return false;
        return this.position.equals(transform.position) &&
                this.orientation.equals(transform.orientation) &&
                this.scale.equals(transform.scale);
    }

    @Override
    public void onStart()
    {

    }

    @Override
    public void onChanged()
    {
        this.matrix.identity()
                .translate(this.position)
                .rotateX((float) Math.toRadians(this.orientation.x))
                .rotateY((float) Math.toRadians(this.orientation.y))
                .rotateZ((float) Math.toRadians(this.orientation.z))
                .scale(this.scale);
       change(false);
    }

    @Override
    public void onStop()
    {

    }

    @Override
    public boolean hasChanged()
    {
        return this.changed;
    }

    @Override
    public void change(boolean change)
    {
        this.changed = change;
    }

}
