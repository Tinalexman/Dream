package dream.shader;

import org.joml.*;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL30.*;

public class Shader
{
    protected static final int notFound = -1;

    protected transient int programID;

    public final String filepath;
    protected transient Map<String, Integer> uniformVariables;

    public Shader(String shaderFileName)
    {
        this.filepath = shaderFileName;
    }

    public void destroy()
    {
        stop();
        if(this.programID != 0)
            glDeleteProgram(this.programID);
    }

    public void onStart()
    {
        String[] shaders = processShader(this.filepath);
        this.programID = glCreateProgram();
        if(programID == 0)
            throw new RuntimeException("Cannot create shader program!");

        int vertexShader = createShader(GL_VERTEX_SHADER, shaders[0]);
        int fragmentShader = createShader(GL_FRAGMENT_SHADER, shaders[1]);

        linkProgram(vertexShader, fragmentShader);
        this.uniformVariables = new HashMap<>();
    }

    private int createShader(int shaderType, String shaderSource)
    {
        int shaderID = glCreateShader(shaderType);
        if(shaderID == 0)
            throw new IllegalStateException("Cannot create shader!");

        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0)
            throw new IllegalStateException("Cannot create shader: " + glGetShaderInfoLog(shaderID, 1024));

        glAttachShader(this.programID, shaderID);
        return shaderID;
    }

    private String[] processShader(String shaderFilePath)
    {
        String[] shaders = new String[2];
        try
        {
            String source = new String(Files.readAllBytes(Paths.get(shaderFilePath)));
            String[] splitShaders = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if(firstPattern.equalsIgnoreCase("vertex"))
                shaders[0] = splitShaders[1];
            else if(firstPattern.equalsIgnoreCase("fragment"))
                shaders[1] = splitShaders[1];
            else
                throw new RuntimeException("Illegal Shader Type: '" + firstPattern + "'");

            if(secondPattern.equalsIgnoreCase("vertex"))
                shaders[0] = splitShaders[2];
            else if(secondPattern.equalsIgnoreCase("fragment"))
                shaders[1] = splitShaders[2];
            else
                throw new RuntimeException("Illegal Shader Type: '" + secondPattern + "'");
        }
        catch (IOException ex)
        {
            throw new RuntimeException("Cannot load shader: '" + shaderFilePath + "' due to " + ex.getMessage());
        }
        return shaders;
    }

    private void linkProgram(int vertexShader, int fragmentShader)
    {
        glLinkProgram(this.programID);
        if(glGetProgrami(this.programID, GL_LINK_STATUS) == 0)
        {
            String errorMessage = "Cannot link program: " + glGetProgramInfoLog(this.programID, 1024);
            throw new IllegalStateException(errorMessage);
        }

        glValidateProgram(this.programID);
        if(glGetProgrami(this.programID, GL_VALIDATE_STATUS) == 0)
            System.err.println("Warning! Shader code validation " + glGetProgramInfoLog(this.programID, 1024));

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public Map<String, Integer> getUniformVariables()
    {
        return this.uniformVariables;
    }

    public void start()
    {
        glUseProgram(this.programID);
    }

    public void stop()
    {
        glUseProgram(0);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Shader shader))
            return false;
        return this.filepath.equals(shader.filepath);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), this.filepath);
    }

    private int uniformLocation(String name)
    {
        return glGetUniformLocation(this.programID, name);
    }

    public final void storeUniforms(String ... uniforms)
    {
        for(String uniform : uniforms)
        {
            int location = uniformLocation(uniform);
            if(location == notFound)
            {
                System.err.println("Uniform " + uniform + " could not be located in the shader! " +
                        "Check whether it was used in the shader code or perhaps it was spelt wrongly");
                continue;
            }
            this.uniformVariables.put(uniform, location);
        }
    }

    public void loadUniform(String name, int value)
    {
        int location = this.uniformVariables.get(name);
        glUniform1i(location, value);
    }

    public void loadUniform(String name, float value)
    {
        int location = this.uniformVariables.get(name);
        glUniform1f(location, value);
    }

    public void loadUniform(String name, boolean value)
    {
        int location = this.uniformVariables.get(name);
        glUniform1f(location, value ? 1.0f : 0.0f);
    }

    public void loadUniform(String name, Vector2f value)
    {
        loadUniform(name, value.x, value.y);
    }

    public void loadUniform(String name, float x, float y)
    {
        int location = this.uniformVariables.get(name);
        glUniform2f(location,x, y);
    }

    public void loadUniform(String name, Vector3f value)
    {
        loadUniform(name, value.x, value.y, value.z);
    }

    public void loadUniform(String name, float x, float y, float z)
    {
        int location = this.uniformVariables.get(name);
        glUniform3f(location, x, y, z);
    }

    public void loadUniform(String name, Vector4f value)
    {
        loadUniform(name, value.x, value.y, value.z, value.w);
    }

    public void loadUniform(String name, float x, float y, float z, float w)
    {
        int location = this.uniformVariables.get(name);
        glUniform4f(location, x, y, z, w);
    }

    public void loadUniform(String name, Matrix3f value)
    {
        int location = this.uniformVariables.get(name);
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            glUniformMatrix3fv(location, false, value.get(stack.mallocFloat(9)));
        }
    }

    public void loadUniform(String name, Matrix4f value)
    {
        int location = this.uniformVariables.get(name);
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            glUniformMatrix4fv(location, false, value.get(stack.mallocFloat(16)));
        }
    }

}
