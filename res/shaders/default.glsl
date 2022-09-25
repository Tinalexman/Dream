#type VERTEX
#version 330 core

layout (location = 0) in vec3 vertices;

void main()
{
    gl_Position = vec4(vertices, 1.0);
}

#type FRAGMENT
#version 330 core

out vec4 outColor;

void main()
{
    outColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);
}