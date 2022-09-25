#type VERTEX
#version 330 core

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 textures;
layout (location = 2) in vec3 normals;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 transformation;

out vec2 passedTextures;
out vec3 passedNormals;

void main()
{
    gl_Position = projection * view * transformation * vec4(vertices, 1.0);
    passedNormals = normals;
    passedTextures = textures;
}

#type FRAGMENT
#version 330 core

in vec2 passedTextures;
in vec3 passedNormals;

struct Material
{
    int hasTexture;
    vec4 diffuseColor;
};
uniform Material material;

uniform sampler2D sampler;

out vec4 outColor;

void main()
{
    if(material.hasTexture == 1)
        outColor = texture(sampler, passedTextures);
    else
        outColor = material.diffuseColor;
}