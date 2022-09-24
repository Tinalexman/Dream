package dream.graphics.material;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture
{
    public transient int width;
    public transient int height;
    public transient int ID;

    private transient ByteBuffer buffer;

    public String filePath;

    public Texture(String filePath)
    {
        this.width = 0;
        this.height = 0;
        this.buffer = null;
        this.filePath = filePath;

        load();
    }

    public void destroy()
    {
        glDeleteTextures(this.ID);
    }

    public Texture(int textureWidth, int textureHeight)
    {
        this.ID = glGenTextures();
        this.filePath =  "Generated";
        this.width = textureWidth;
        this.height = textureHeight;
        this.buffer = null;
        glBindTexture(GL_TEXTURE_2D, this.ID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, textureWidth, textureHeight,
                0, GL_RGB, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    }

    private void loadTextureData()
    {
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(true);
            this.buffer = STBImage.stbi_load(this.filePath, w, h, channels, 4);
            if(this.buffer == null)
                throw new Exception("Cannot load texture file: " + this.filePath
                        + " due to " + STBImage.stbi_failure_reason());

            this.width = w.get();
            this.height = h.get();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void load()
    {
        loadTextureData();

        if(this.buffer == null)
            return;

        this.ID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.ID);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, this.buffer);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glGenerateMipmap(GL_TEXTURE_2D);
        STBImage.stbi_image_free(this.buffer);
    }

}
