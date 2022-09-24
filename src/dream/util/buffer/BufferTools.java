package dream.util.buffer;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferTools
{
    public static FloatBuffer createFloatBuffer(float[] coordinates)
    {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(coordinates.length);
        buffer.put(coordinates).flip();
        return buffer;
    }

    public static IntBuffer createIntBuffer(int[] coordinates)
    {
        IntBuffer buffer = MemoryUtil.memAllocInt(coordinates.length);
        buffer.put(coordinates).flip();
        return buffer;
    }

}
