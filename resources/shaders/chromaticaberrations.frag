#version 150
uniform sampler2D colourTexture;

uniform float distortionStrength;
uniform float vignetStrength;

in vec2 textureCoords;
out vec4 FragColor;


void main(void){
	float 	centerBuffer = 0.15;
	
	float 	chrDist,
			vigDist;
	
	//calculate how far each pixel is from the center of the screen
	vec2 vecDist = textureCoords - ( 0.5 , 0.5 );
	chrDist = vigDist = length( vecDist );
	
	//modify the distance from the center, so that only the edges are affected
	chrDist	-= centerBuffer;
	if( chrDist < 0.0 ) chrDist = 0.0;
	
	//distort the UVs
	vec2 uvR = textureCoords * ( 1.0 + chrDist * 0.02 * distortionStrength ),
		 uvB = textureCoords * ( 1.0 - chrDist * 0.02 * distortionStrength );
	
	//get the individual channels using the modified UVs
	vec4 c;
	
	c.x = texture( colourTexture , uvR ).x; 
	c.y = texture( colourTexture , textureCoords ).y; 
	c.z = texture( colourTexture , uvB ).z;
	
	//apply vignette
	c *= 1.0 - vigDist* vignetStrength;
	
	FragColor = c;

}