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

const float offset = 1.0 / 300.0;
const int kernelSize = 9;

void main()
{
    vec2 offsets[kernelSize] = vec2[]
    (
    vec2(-offset, offset),
    vec2(0.0, offset),
    vec2(offset, offset),

    vec2(-offset, 0.0),
    vec2(0.0, 0.0),
    vec2(offset, 0.0),

    vec2(-offset, -offset),
    vec2(0.0, -offset),
    vec2(offset, -offset)
    );

    float kernels[kernelSize] = float[]
    (
        1.0 / 16, 2.0 / 16, 1.0 / 16,
        2.0 / 16, 4.0 / 16, 2.0 / 16,
        1.0 / 16, 2.0 / 16, 1.0 / 16
    );

    vec3 sampleTextures[kernelSize];
    for(int i = 0; i < kernelSize; i++)
    {
        sampleTextures[i] = vec3(texture(sampler, passTextures.st + offsets[i]));
    }

    vec3 color = vec3(0.0);
    for(int i = 0; i < kernelSize; i++)
    {
        color += sampleTextures[i] * kernels[i];
    }

    outColor = vec4(color, 1.0);
}