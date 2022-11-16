package dream.postprocessing;

import dream.managers.ResourcePool;
import dream.shader.Shader;

import java.util.HashMap;
import java.util.Map;

import static dream.shader.ShaderConstants.sampler;

public class FilterManager
{
    public static Map<String, Shader> filters = new HashMap<>();

    public static void createFilters()
    {
        try
        {
            Shader shader = ResourcePool.addAndGetShader("filters\\inversionShader.glsl");
            shader.storeUniforms(sampler);
            FilterManager.filters.put("Inversion", shader);

            shader = ResourcePool.addAndGetShader("filters\\grayscaleShader.glsl");
            shader.storeUniforms(sampler);
            FilterManager.filters.put("GrayScale", shader);

            shader = ResourcePool.addAndGetShader("filters\\sharpenShader.glsl");
            shader.storeUniforms(sampler);
            FilterManager.filters.put("Sharpen", shader);

            shader = ResourcePool.addAndGetShader("filters\\blurShader.glsl");
            shader.storeUniforms(sampler);
            FilterManager.filters.put("Blur", shader);

            shader = ResourcePool.addAndGetShader("filters\\edgeShader.glsl");
            shader.storeUniforms(sampler);
            FilterManager.filters.put("Edge Detection", shader);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void add(String name, Shader shader)
    {
        if(!FilterManager.filters.containsKey(name))
            FilterManager.filters.put(name, shader);
    }

    public static Shader getShader(String name)
    {
        return FilterManager.filters.getOrDefault(name, null);
    }

    public static Shader getShader(int index)
    {
        String key = FilterManager.filters.keySet().toArray(new String[0])[index];
        return FilterManager.filters.get(key);
    }

    public static Map<String, Shader> getFilters()
    {
        return FilterManager.filters;
    }
}
