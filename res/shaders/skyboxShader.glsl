#type VERTEX
#version 330 core

layout (location = 0) in vec3 vertices;

out vec3 textures;

uniform mat4 projection;
uniform mat4 view;

void main()
{
    gl_Position = projection * view * vec4(vertices, 1.0);
    textures = vertices;
}

#type FRAGMENT
#version 330 core

in vec3 textures;
out vec4 outColor;
uniform samplerCube cubeMap;

void main()
{
    outColor = texture(cubeMap, textures);
}