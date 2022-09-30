package dream.managers;

import dream.Engine;
import dream.graphics.texture.Texture;
import dream.shader.Shader;
import dream.graphics.icon.Icons;
import dream.shader.ShaderConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class ResourcePool
{
    private static final int[] systemIcons = new int[Icons.total];

    private static final Map<String, Shader> shaders = new HashMap<>();
    private static final Map<String, Texture> textures = new HashMap<>();

    private static Shader defaultShader;

    public static Shader getDefault()
    {
        if(ResourcePool.defaultShader == null)
        {
            ResourcePool.defaultShader = addAndGetShader("default.glsl");
            ResourcePool.defaultShader.onStart();
            ResourcePool.defaultShader.storeUniforms(ShaderConstants.projection, ShaderConstants.view,
                    ShaderConstants.transformation, ShaderConstants.color, ShaderConstants.isActive);
        }
        return ResourcePool.defaultShader;
    }

    public static Shader getShader(String resourceName)
    {
        String path = Engine.resourcePath + "\\shaders\\" + resourceName;
        return ResourcePool.shaders.getOrDefault(path, null);
    }

    public static void addShader(String resourceName)
    {
        String path = Engine.resourcePath + "\\shaders\\" + resourceName;
        if(ResourcePool.shaders.containsKey(path))
            return;

        Shader shader = new Shader(path);
        shader.onStart();
        ResourcePool.shaders.put(path, shader);
    }

    public static Shader addAndGetShader(String resourceName)
    {
        String path = Engine.resourcePath + "\\shaders\\" + resourceName;
        if(ResourcePool.shaders.containsKey(path))
            return ResourcePool.shaders.get(path);

        Shader shader = new Shader(path);
        shader.onStart();
        ResourcePool.shaders.put(path, shader);
        return shader;
    }

    public static Texture getTexture(String resourceName)
    {
        String path = Engine.resourcePath + "\\textures\\" + resourceName;
        return ResourcePool.textures.getOrDefault(path, null);
    }

    public static int getIcon(int icon)
    {
        return ResourcePool.systemIcons[icon];
    }

    public static void loadIcons()
    {
        String path = Engine.resourcePath + "\\icons";

        String[] files = new File(path).list();
        if(files == null)
        {
            System.err.println("The required editor textures are absent!");
            return;
        }

        for(String file : files)
        {
            String filePath = path + "\\" + file;
            if(ResourcePool.textures.containsKey(filePath))
                return;

            Texture texture = new Texture(filePath);
            ResourcePool.textures.put(filePath, texture);

            int systemIconID = createIcon(file);
            if(systemIconID >= 0)
                systemIcons[systemIconID] = texture.ID;
        }
    }

    public static Texture addAndGetTexture(String resourceName)
    {
        String path = Engine.resourcePath + "\\textures\\" + resourceName;

        if(ResourcePool.textures.containsKey(path))
            return ResourcePool.textures.get(path);

        Texture texture = new Texture(path);
        ResourcePool.textures.put(path, texture);
        return texture;
    }

    public static ArrayList<Texture> getAllTextures()
    {
        return new ArrayList<>(ResourcePool.textures.values());
    }

    public static ArrayList<Shader> getAllShaders()
    {
        return new ArrayList<>(ResourcePool.shaders.values());
    }

    private static int createIcon(String textureName)
    {
        // Note: If you want to add a new system icon, add it here, make sure it is named appropriately
        // E.G (SystemLightBlahBlahBlah.whatever)
        // And also change the total variable in the Icons class.
        int ID = -1;
        if(textureName.startsWith("menu"))
            ID = Icons.menu;
        else if(textureName.startsWith("about"))
            ID = Icons.about;
        else if(textureName.startsWith("done"))
            ID = Icons.done;
        else if(textureName.startsWith("close"))
            ID = Icons.close;
        else if(textureName.startsWith("plus"))
            ID = Icons.plus;
        else if(textureName.startsWith("minus"))
            ID = Icons.minus;

        return ID;
    }

    public static int[] getIcons()
    {
        return ResourcePool.systemIcons;
    }
}
