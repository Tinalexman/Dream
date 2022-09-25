#type VERTEX
#version 330 core

const int maxJoints = 50;
const int maxWeights = 3;

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 textureCoordinates;
layout (location = 2) in vec3 normals;
layout (location = 3) in ivec3 jointIndices;
layout (location = 4) in vec3 weights;

uniform mat4 jointTransforms[maxJoints];
uniform mat4 projection;
uniform mat4 view;

out vec2 outTextureCoordinates;
out vec3 outNormal;

void main()
{
    vec4 totalLocalPos = vec4(0.0);
    vec4 totalNormal = vec4(0.0);

    for(int i = 0; i < maxWeights; i++)
    {
        vec4 localPosition = jointTransforms[jointIndices[i]] * vec4(vertices, 1.0);
        totalLocalPos += localPosition * weights[i];

        vec4 worldNormal = jointTransforms[jointIndices[i]] * vec4(normals, 1.0);
        totalNormal += worldNormal * weights[i];
    }

    gl_Position = projection * view * totalLocalPos;
    outNormal = totalNormal.xyz;
    outTextureCoordinates = textureCoordinates;
}

#type FRAGMENT
#version 330 core

const vec2 lightBias = vec2(0.7, 0.6);

in vec2 outTextureCoordinates;
in vec3 outNormal;

//uniform vec3 lightDirection;
uniform sampler2D diffuseMap;

out vec4 outColor;

void main()
{
    vec4 diffuseColor = texture(diffuseMap, outTextureCoordinates);
    //vec3 unitNormal = normalize(outNormal);
    //float diffuseLight = max(dot(-lightDirection, unitNormal), 0.0) * lightBias.x + lightBias.y;
   // outColor = diffuseColor * diffuseLight;
    outColor = diffuseColor;;
}