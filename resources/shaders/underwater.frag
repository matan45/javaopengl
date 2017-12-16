#version 150
uniform sampler2D colourTexture;

uniform float speed;
uniform float time;

in vec2 textureCoords;
out vec4 FragColor;

// ---- SETTINGS ----------------------------------------------------------------

// the amount of shearing (shifting of a single column or row)
// 1.0 = entire screen height offset (to both sides, meaning it's 2.0 in total)
#define xDistMag 0.05
#define yDistMag 0.05

// cycle multiplier for a given screen height
// 2*PI = you see a complete sine wave from top..bottom
#define xSineCycles 6.50
#define ySineCycles 6.50


vec2 underwater(vec2 uv){

	// the value for the sine has 2 inputs:
    // 1. the time, so that it animates.
    // 2. the y-row, so that ALL scanlines do not distort equally.
    float iTime = time*speed;
    float xAngle = iTime + uv.y * ySineCycles;
    float yAngle = iTime + uv.x * xSineCycles;
    
    vec2 distortOffset = 
        vec2(sin(xAngle), sin(yAngle)) * // amount of shearing
        vec2(xDistMag,yDistMag); // magnitude adjustment
    
    // shear the coordinates
    uv += distortOffset;  
	return uv;
}

vec4 watercolumns( vec2 uv )
{
	const bool leftToRight = false;
    float slopeSign = (leftToRight ? -1.0 : 1.0);
    float slope1 = 5.0 * slopeSign;
    float slope2 = 7.0 * slopeSign;	
	
	float bright = 
	- sin(uv.y * slope1 + uv.x * 30.0+ time *3.10) *.2 
	- sin(uv.y * slope2 + uv.x * 37.0 + time *3.10) *.1
	- cos(              + uv.x * 2.0 * slopeSign + time *2.10) *.1 
	- sin(              - uv.x * 5.0 * slopeSign + time * 2.0) * .3;
	
	float modulate = abs(cos(time*.1) *.5 + sin(time * .7)) *.5;
	bright *= modulate;
	vec4 pix = texture(colourTexture,uv);
	pix.rgb += clamp(bright / 1.0,0.0,1.0);
	return pix;
}


void main(void)
{
	vec2 uv  = underwater(textureCoords);
 	vec4 waterwavs = texture(colourTexture, uv);
 	vec4 waterlight = watercolumns(textureCoords);
    FragColor = waterwavs+waterlight;
    
    // blue shift to look like water
    FragColor.rgb = vec3(0.0, 0.2, 0.9) + 
        FragColor.rgb * vec3(0.5, 0.6, 0.1);

}
