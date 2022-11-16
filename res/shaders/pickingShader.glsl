#type VERTEX
#version 330 core

layout (location = 0) in vec3 vertices;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;

void main()
{
    gl_Position = projection * view * transformation * vec4(vertices, 1.0);
}

#type FRAGMENT
#version 330 core

const float maxColorRange = 255.0;

uniform float drawIndex;
uniform float objectIndex;

out vec3 outColor;

void main()
{
    outColor = vec3(objectIndex / maxColorRange, drawIndex / maxColorRange, (gl_PrimitiveID + 1) / maxColorRange);
}