#version 150

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colourTexture;
uniform sampler2D highlightTexture;

vec3 vignetting(vec3 col,float strong){
	col *= strong + 0.3 * sqrt(16.0*textureCoords.x*textureCoords.y*(1.0-textureCoords.x)*(1.0-textureCoords.y));
	return col;
}

vec3 colorplay(vec3 color,vec3 colormix1,vec3 colormix2){

	float shade = dot(color, vec3(0.333333));

	vec3 col = mix(colormix1 * (1.0-2.0*abs(shade-0.5)), colormix2, 1.0-shade);
	return col;
}

vec3 ColorGrade( vec3 vColor )
{
    vec3 vHue = vec3(1.0, .7, .2);
    
    vec3 vGamma = 1.0 + vHue * 0.6;
    vec3 vGain = vec3(.9) + vHue * vHue * 8.0;
    
    vColor *= 1.5;
    
    float fMaxLum = 100.0;
    vColor /= fMaxLum;
    vColor = pow( vColor, vGamma );
    vColor *= vGain;
    vColor *= fMaxLum;  
    return vColor;
}

void main(void){
	vec4 sceneColour = texture(colourTexture,textureCoords);
	vec4 highlightColur = texture(highlightTexture,textureCoords);
	
	vec4 col = sceneColour + highlightColur * 0.3;
	out_Colour = vec4(ColorGrade(vignetting(col.rgb,1.0)),1.0);
}



