package dream.shader;

public class ShaderConstants
{
    // General Uniforms
    public static final String transformation = "transformation";
    public static final String view = "view";
    public static final String projection = "projection";

    public static final String color = "color";
    public static final String position = "position";
    public static final String direction = "direction";
    public static final String viewPosition = "viewPosition";

    public static final String lightPosition = "lightPosition";
    public static final String lightColor = "lightColor";
    public static final String inverseNormals = "inverseNormals";


    public static final String material = "material";
    public static final String diffuse = "diffuse";
    public static final String ambient = "ambient";
    public static final String specular = "specular";
    public static final String reflectance = "reflectance";
    public static final String constant = "constant";
    public static final String linear = "linear";
    public static final String quadratic = "quadratic";
    public static final String cutoff = "cutoff";
    public static final String outerCutoff = "outerCutoff";
    public static final String isActive = "isActive";
    public static final String sampler = "sampler";
    public static final String ambientLight = "ambientLight";


    public static final String directionalLight = "directionalLight";
    public static final String spotLight = "spotLight";
    public static final String pointLight = "pointLight";
    public static final String light = "light";

    // Combined
    public static final String materialDiffuse = material + "." + diffuse;
    public static final String materialSpecular = material + "." + specular;
    public static final String materialReflectance = material + "." + reflectance;

    public static final String directionalLightDiffuse = directionalLight + "." + diffuse;
    public static final String directionalLightAmbient = directionalLight + "." + ambient;
    public static final String directionalLightSpecular = directionalLight + "." + specular;
    public static final String directionalLightDirection = directionalLight + "." + direction;
    public static final String directionalLightActive = directionalLight + "." + isActive;

    public static final String pointLightDiffuse = pointLight + "." + diffuse;
    public static final String pointLightAmbient = pointLight + "." + ambient;
    public static final String pointLightSpecular = pointLight + "." + specular;
    public static final String pointLightPosition = pointLight + "." + position;
    public static final String pointLightConstant = pointLight + "." + constant;
    public static final String pointLightLinear = pointLight + "." + linear;
    public static final String pointLightQuadratic = pointLight + "." + quadratic;
    public static final String pointLightActive = pointLight + "." + isActive;

    public static final String spotLightDiffuse = spotLight + "." + diffuse;
    public static final String spotLightAmbient = spotLight + "." + ambient;
    public static final String spotLightSpecular = spotLight + "." + specular;
    public static final String spotLightPosition = spotLight + "." + position;
    public static final String spotLightDirection = spotLight + "." + direction;
    public static final String spotLightCutoff = spotLight + "." + cutoff;
    public static final String spotLightOuterCutoff = spotLight + "." + outerCutoff;
    public static final String spotLightActive = spotLight + "." + isActive;

}
