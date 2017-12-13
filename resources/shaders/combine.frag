#version 150

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colourTexture;
uniform sampler2D highlightTexture;

vec3 vignetting(vec3 col,float strong){
	col *= strong + 0.3 * sqrt(16.0*textureCoords.x*textureCoords.y*(1.0-textureCoords.x)*(1.0-textureCoords.y));
	return col;
}

void main(void){
	vec4 sceneColour = texture(colourTexture,textureCoords);
	vec4 highlightColur = texture(highlightTexture,textureCoords);
	
	vec4 col = sceneColour + highlightColur * 0.3;
	out_Colour = vec4(vignetting(col.rgb,1.0),1.0);
	

}