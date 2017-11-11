#version 330

const int MAX_WEIGHTS = 4;
const int MAX_JOINTS = 150;

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 vertexNormal;
layout (location=3) in vec4 jointWeights;
layout (location=4) in ivec4 jointIndices;

out vec2 pass_textureCoords;
out vec3 pass_normal;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 Transformation;

uniform mat4 jointsMatrix[MAX_JOINTS];

void main()
{
    vec4 totalLocalPos = vec4(0.0);
	vec4 totalNormal = vec4(0.0);
	
	for(int i=0;i<MAX_WEIGHTS;i++){
		mat4 jointTransform = jointsMatrix[jointIndices[i]];
		vec4 posePosition = Transformation * jointTransform * vec4(position, 1.0);
		totalLocalPos += posePosition * jointWeights[i];
		
		vec4 worldNormal = jointTransform * vec4(vertexNormal, 0.0);
		totalNormal += worldNormal * jointWeights[i];
	}
	
	gl_Position = projectionMatrix *  viewMatrix * totalLocalPos ;
	pass_normal = totalNormal.xyz;
	pass_textureCoords = texCoord;
   
   
}