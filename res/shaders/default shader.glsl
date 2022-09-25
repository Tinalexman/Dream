#type VERTEX
#version 330 core

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 textures;
layout (location = 2) in vec3 normals;

out vec3 passNormals;
out vec2 passTextures;
out vec3 fragmentPosition;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

void main()
{
    vec4 worldPosition = transformation * vec4(vertices, 1.0);
    gl_Position = projection * view * worldPosition;
    fragmentPosition = worldPosition.xyz;
    passTextures = textures;
    passNormals = normals;
}

#type FRAGMENT
#version 330 core

const int maxDirectionalLights = 1;
const int maxPointLights = 2;
const int maxSpotLights = 2;

in vec3 passNormals;
in vec2 passTextures;
in vec3 fragmentPosition;

out vec4 outColor;

struct Material
{
    sampler2D diffuse;
    sampler2D specular;
    float reflectance;
};

struct PointLight
{
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    vec3 position;
    float constant;
    float linear;
    float quadratic;
    int isActive;
};

struct DirectionalLight
{
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    vec3 direction;
    int isActive;
};

struct SpotLight
{
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    vec3 direction;
    vec3 position;
    float cutoff;
    float outerCutoff;
    int isActive;
};

uniform Material material;
uniform PointLight pointLight;
uniform DirectionalLight directionalLight;
uniform SpotLight spotLight;

uniform vec3 viewPosition;
uniform vec3 ambientLight;

vec3 calculateDirectionalLight(DirectionalLight light, vec3 normal, vec3 viewDirection)
{
    vec3 lightDirection = normalize(-light.direction);
    float diff = max(dot(normal, lightDirection), 0.0);
    vec3 reflectDirection = reflect(-lightDirection, normal);
    float spec = pow(max(dot(viewDirection, reflectDirection), 0.0), material.reflectance);
    vec3 diffuseTexture = vec3(texture(material.diffuse, passTextures));

    vec3 ambient = light.ambient * diffuseTexture;
    vec3 diffuse = light.diffuse * diff * diffuseTexture;
    vec3 specular = light.specular * spec * vec3(texture(material.specular, passTextures));

    return (ambient + diffuse + specular);
}

vec3 calculatePointLight(PointLight light, vec3 normal, vec3 fragmentPosition, vec3 viewDirection)
{
    vec3 lightDirection = normalize(light.position - fragmentPosition);
    float diff = max(dot(normal, lightDirection), 0.0);
    vec3 reflectDirection = reflect(-lightDirection, normal);
    float spec = pow(max(dot(viewDirection, reflectDirection), 0.0), material.reflectance);
    vec3 diffuseTexture = vec3(texture(material.diffuse, passTextures));

    float distance = length(light.position - fragmentPosition);
    float attenuation = 1.0 / (light.constant + (light.linear * distance) + (light.quadratic * distance * distance));

    vec3 ambient = light.ambient * diffuseTexture * attenuation;
    vec3 diffuse = light.diffuse * diff * diffuseTexture * attenuation;
    vec3 specular = light.specular * spec * attenuation * vec3(texture(material.specular, passTextures));

    return (ambient + diffuse + specular);
}

vec3 calculateSpotLight(SpotLight light, vec3 normal, vec3 fragmentPosition, vec3 viewDirection)
{
    vec3 lightDirection = normalize(light.position - fragmentPosition);
    float diff = max(dot(normal, lightDirection), 0.0);
    vec3 reflectDirection = reflect(-lightDirection, normal);
    float spec = pow(max(dot(viewDirection, reflectDirection), 0.0), material.reflectance);
    vec3 diffuseTexture = vec3(texture(material.diffuse, passTextures));

    float theta = dot(lightDirection, normalize(-light.direction));
    float epsilon = light.cutoff - light.outerCutoff;
    float intensity = clamp((theta - light.outerCutoff) / epsilon, 0.0, 1.0);

    vec3 ambient = vec3(0.0);
    vec3 diffuse = vec3(0.0);
    vec3 specular = vec3(0.0);

    if(theta > light.cutoff)
    {
        ambient = light.ambient * diffuseTexture;
        diffuse = light.diffuse * diff * diffuseTexture;
        specular = light.specular * spec * vec3(texture(material.specular, passTextures));
    }
    else
    {
        vec3 color = light.ambient * diffuseTexture;
        ambient = color;
        diffuse = color;
        specular = color;
    }

    ambient *= intensity;
    diffuse *= intensity;
    specular *= intensity;

    return (ambient + diffuse + specular);
}

void main()
{
    vec3 normal = normalize(passNormals);
    vec3 viewDirection = (viewPosition - fragmentPosition);

    vec3 result = vec3(0.0);
    result += ambientLight * vec3(texture(material.diffuse, passTextures));
    //for(int i = 0; i < maxDirectionalLights; ++i)
        if(directionalLight.isActive == 1)
            result += calculateDirectionalLight(directionalLight, normal, viewDirection);
    //for(int i = 0; i < maxPointLights; ++i)
        if(pointLight.isActive == 1)
            result += calculatePointLight(pointLight, normal, fragmentPosition, viewDirection);
    //for(int i = 0; i < maxSpotLights; ++i)
        if(spotLight.isActive == 1)
            result += calculateSpotLight(spotLight, normal, fragmentPosition, viewDirection);

    outColor = vec4(result, 1.0);
}