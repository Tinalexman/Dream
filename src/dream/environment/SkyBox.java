package dream.environment;

import dream.components.mesh.Mesh;
import dream.components.mesh.MeshFactory;
import dream.components.mesh.MeshRenderer;
import dream.managers.ResourcePool;
import dream.shader.Shader;
import dream.shader.ShaderConstants;
import org.joml.Matrix4f;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class SkyBox
{
    // RIGHT LEFT TOP BOTTOM FRONT BACK
    private final Shader shader;
    private final MeshRenderer renderer;
    private String[] imagePaths;
    private int textureID;


    public SkyBox()
    {
        this.shader = ResourcePool.addAndGetShader("skyboxShader.glsl");
        this.shader.storeUniforms(ShaderConstants.projection, ShaderConstants.view);
        Mesh mesh = MeshFactory.asCube();
        this.renderer = new MeshRenderer();
        this.renderer.setMesh(mesh);
    }

    public void setImagePaths(String ... paths)
    {
        this.imagePaths = paths;
        if(paths != null)
            load();
    }

    private void load()
    {
        this.textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, this.textureID);

        for(int i = 0; i < this.imagePaths.length; ++i)
        {
            try(MemoryStack stack = MemoryStack.stackPush())
            {
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);

                STBImage.stbi_set_flip_vertically_on_load(true);
                ByteBuffer buffer = STBImage.stbi_load(this.imagePaths[i], w, h, channels, 4);
                if(buffer == null)
                {
                    System.err.println("Cannot load texture file: " + this.imagePaths[i]
                            + " due to " + STBImage.stbi_failure_reason());
                    continue;
                }

                glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, w.get(), h.get(),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

                STBImage.stbi_image_free(buffer);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
    }

    public void start()
    {
        this.shader.start();
    }

    public void stop()
    {
        this.shader.stop();
    }

    public void destroy()
    {
        glDeleteTextures(this.textureID);
    }

    public void loadUniforms(Matrix4f projection, Matrix4f view)
    {
        this.shader.loadUniform(ShaderConstants.projection, projection);
        this.shader.loadUniform(ShaderConstants.view, view);
    }

    public void show()
    {
        this.renderer.onStart();
        glBindTexture(GL_TEXTURE_CUBE_MAP, this.textureID);
        glDrawArrays(GL_TRIANGLES, 0, this.renderer.count());
        this.renderer.onStop();
    }
}
