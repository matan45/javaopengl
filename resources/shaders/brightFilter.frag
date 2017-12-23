#version 150

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colourTexture;
uniform int SHADES; 
uniform float scale; 

float Sigmoid(float x){
	return 1.0 / (1.0 + (exp(-(x - 0.5) * 14.0)));
}

float SCurve (float value, float amount, float correction) {

	float curve = 1.0; 

    if (value < 0.5)
    {

        curve = pow(value, amount) * pow(2.0, amount) * 0.5; 
    }
        
    else
    { 	
    	curve = 1.0 - pow(1.0 - value, amount) * pow(2.0, amount) * 0.5; 
    }

    return pow(curve, correction);
}

vec3 colorplay(vec3 color,vec3 colormix1,vec3 colormix2){

	float shade = dot(color, vec3(0.333333));

	vec3 col = mix(colormix1 * (1.0-2.0*abs(shade-0.5)), colormix2, 1.0-shade);
	return col;
}

vec4 celshading(vec4 colour){

    //Calculating the brightness of the fragment (Actually it is the average color but that works fine too)
    float brightness = (colour.r + colour.g + colour.g) / 3.; 
    //Calculating the shade 
	float shade = floor(brightness * float(SHADES));
    //Calculating the brightness of the shade
	float brighnessOfShade = shade / float(SHADES);
	//Calculating the brightness difference
	float factor = brightness / brighnessOfShade;
	//Applying the brightness difference
	colour.rgb /= vec3(factor);
	return colour;
}


vec4 c = vec4(0.2124,0.7153,0.0722,0.0);

vec4 bleach(vec4 p, vec4 m, vec4 s) 
{
    vec4 a = vec4(1.0);
 	vec4 b = vec4(2.0);
	float l = dot(m,c);
	float x = clamp((l - 0.45) * 10.0, 0.0, 1.0);
	vec4 t = b * m * p;
	vec4 w = a - (b * (a - m) * (a - p));
	vec4 r = mix(t, w, vec4(x) );
	return mix(m, r, s);
}

void main(void){
	vec4 colour = texture(colourTexture,textureCoords);
	float brightness = (colour.r * 0.2126)+(colour.g * 0.7152)+(colour.b * 0.0722);
	
	vec4 color = colour * brightness;
	color = vec4(Sigmoid(color.r),Sigmoid(color.g),Sigmoid(color.b),1.0);
	color = pow(color,vec4(0.5));
	
	if(scale > 0){
		vec4 k = vec4(vec3(dot(color,c)),color.a);
		color = bleach(k, color, vec4(scale));
	}
	
	if(SHADES == 0){
		out_Colour = color;
	}else{
		out_Colour = celshading(color);
	}

}


