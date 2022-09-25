#type VERTEX
#version 330 core

layout(location = 0) in vec3 positions;
layout(location = 1) in vec3 color;

out vec3 fColor;

uniform mat4 projection;
uniform mat4 view;

void main()
{
    gl_Position = projection * view * vec4(positions, 1.0);
    fColor = color;
}


#type FRAGMENT
#version 330 core

in vec3 fColor;
out vec4 outColor;

void main()
{
    outColor = vec4(fColor, 1.0);
}