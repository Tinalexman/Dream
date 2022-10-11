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

out vec4 outColor;

uniform vec3 color;

void main()
{
    outColor = vec4(color, 1.0);
}