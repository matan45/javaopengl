#version 150

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colourTexture;
uniform sampler2D highlightTexture;

vec3 Tonemap( vec3 x )
{
    float a = 0.010;
    float b = 0.132;
    float c = 0.010;
    float d = 0.163;
    float e = 0.101;

    return ( x * ( a * x + b ) ) / ( x * ( c * x + d ) + e );
}

void main(void){
	vec4 sceneColour = texture(colourTexture,textureCoords);
	vec4 highlightColur = texture(highlightTexture,textureCoords);
	highlightColur = vec4(Tonemap(highlightColur.rgb),1.0);
	
	out_Colour = sceneColour + highlightColur * 0.3;

}