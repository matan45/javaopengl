#version 330 core

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec2 TexCoords;
out vec3 WorldPos;
out vec3 Normals;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;


void main(void){
	TexCoords = textureCoordinates;
    WorldPos = vec3(transformationMatrix * vec4(position, 1.0));
    Normals = mat3(transformationMatrix) * normal; 
      
	vec4 worldPosition = transformationMatrix * vec4(position.x, position.y, position.z, 1.0);
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
 	gl_Position = projectionMatrix * positionRelativeToCam;

	

}