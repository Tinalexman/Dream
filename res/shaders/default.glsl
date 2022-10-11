#type VERTEX
#version 330 core

layout (location = 0) in vec3 vertices;
layout (location = 2) in vec3 normals;

uniform mat3 inverseNormals;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

out vec3 vertexNormal;
out vec3 fragmentPosition;

void main()
{
    vec4 position = transformation * vec4(vertices, 1.0);

    gl_Position = projection * view * position;

    vertexNormal = inverseNormals * normals;
    fragmentPosition = position.xyz;
}

#type FRAGMENT
#version 330 core

out vec4 outColor;

in vec3 vertexNormal;
in vec3 fragmentPosition;

uniform vec3 color;
uniform vec3 lightColor;
uniform vec3 lightPosition;
uniform vec3 viewPosition;

void main()
{
    float ambientStrength = 0.1f;
    vec3 ambient = ambientStrength * lightColor;

    vec3 norm = normalize(vertexNormal);
    vec3 lightDirection = normalize(lightPosition - fragmentPosition);
    float diffuseStrength = max(dot(norm, lightDirection), 0.0);
    vec3 diffuse = diffuseStrength * lightColor;

    float specularStrength = 0.5;
    vec3 viewDirection = normalize(viewPosition - fragmentPosition);
    vec3 reflectDirection = reflect(-lightDirection, norm);
    float spec = pow(max(dot(viewDirection, reflectDirection), 0.0), 32);
    vec3 specular = specularStrength * spec * lightColor;

    vec3 result = (ambient + diffuse + specular) * color;

    outColor = vec4(result, 1.0);
}