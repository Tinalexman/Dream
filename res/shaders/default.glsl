#type VERTEX
#version 330 core

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 textures;
layout (location = 2) in vec3 normals;

uniform mat3 inverseNormals;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

out vec3 vertexNormal;
out vec2 vertexTexture;
out vec3 fragmentPosition;

void main()
{
    vec4 position = transformation * vec4(vertices, 1.0);

    gl_Position = projection * view * position;

    vertexNormal = inverseNormals * normals;
    vertexTexture = textures;
    fragmentPosition = position.xyz;
}

#type FRAGMENT
#version 330 core

const float DIRECTIONAL_LIGHT = 1;
const float POINT_LIGHT = 2;
const float SPOT_LIGHT = 3;

in vec3 vertexNormal;
in vec2 vertexTexture;
in vec3 fragmentPosition;

out vec4 outColor;

struct Material
{
    vec3 diffuse;
    vec3 specular;
    float reflectance;

    sampler2D diffuseMap;
    sampler2D specularMap;

    float hasDiffuseMap;
    float hasSpecularMap;
};

struct Light
{
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    vec3 position;
    vec3 direction;

    float constant;
    float linear;
    float quadratic;

    float cutoff;
    float outerCutoff;

    float type;
};

uniform Light light;
uniform Material material;

uniform vec3 viewPosition;

void main()
{
    vec4 diffuseColor = vec4(0.0);
    vec4 specularColor = vec4(0.0);
    vec3 ambient = vec3(0.0);

    if(material.hasDiffuseMap == 1.0)
    {
        diffuseColor = texture(material.diffuseMap, vertexTexture);
        ambient = diffuseColor.xyz * light.ambient;
    }
    else
    {
        diffuseColor = vec4(material.diffuse, 1.0);
        ambient = vec3(0.1) * material.diffuse * light.ambient;
    }

    specularColor =  (material.hasSpecularMap == 1.0) ?
        texture(material.specularMap, vertexTexture) : material.specular;

    vec3 norm = normalize(vertexNormal);
    vec3 lightDirection = vec3(0.0);

    if(light.type == DIRECTIONAL_LIGHT)
        lightDirection = normalize(-light.direction);
    else if(light.type == POINT_LIGHT || light.type == SPOT_LIGHT)
        lightDirection = normalize(light.position - fragmentPosition);

    float diffuseStrength = max(dot(norm, lightDirection), 0.0);
    vec3 diffuse = diffuseStrength * light.diffuse * diffuseColor.xyz;

    vec3 viewDirection = normalize(viewPosition - fragmentPosition);
    vec3 reflectDirection = reflect(-lightDirection, norm);
    float spec = pow(max(dot(viewDirection, reflectDirection), 0.0), material.reflectance);
    vec3 specular = specularColor.xyz * spec * light.specular;

    if(light.type == POINT_LIGHT)
    {
        float distance = length(light.position - fragmentPosition);
        float attenuation = 1.0 / (light.constant + light.linear * distance +
        light.quadratic * (distance * distance));

        ambient *= attenuation;
        diffuse *= attenuation;
        specular *= attenuation;
    }

    if(light.type == SPOT_LIGHT)
    {
        float theta = dot(lightDirection, normalize(-light.direction));
        float epsilon = (light.cutoff - light.outerCutoff);
        float intensity = clamp((theta - light.outerCutoff) / epsilon, 0.0, 1.0);
        diffuse  *= intensity;
        specular *= intensity;
    }

    vec3 result = ambient + diffuse + specular;

    outColor = vec4(result, 1.0);
}