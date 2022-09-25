#type VERTEX
#version 330 core

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 textures;

out vec2 passTextures;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

void main()
{
    gl_Position = projection * view * transformation * vec4(vertices, 1.0);
    passTextures = textures;
}

#type FRAGMENT
#version 330 core

in vec2 passTextures;
out vec4 outColor;

uniform sampler2D sampler;

void main()
{
    outColor = texture(sampler, passTextures);
}