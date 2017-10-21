#version 400
in vec3 position;

out vec2 textureCoords;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){
	vec4 worldPosition = transformationMatrix * vec4(position.x, position.y, position.z, 1.0);
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
 	gl_Position = projectionMatrix * positionRelativeToCam;
 	
	textureCoords = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);
}