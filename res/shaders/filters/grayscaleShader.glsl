#type VERTEX
#version 330 core

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 textures;

out vec2 passTextures;

void main()
{
    gl_Position = vec4(vertices.x, vertices.y, 0.0, 1.0);
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
    float average = (0.2126 * outColor.r) + (0.7152 * outColor.g) + (0.0722 * outColor.b);
    outColor = vec4(average, average, average, 1.0);
}