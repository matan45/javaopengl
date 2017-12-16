#version 150

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colourTexture;

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


void main(void){
	vec4 colour = texture(colourTexture,textureCoords);
	float brightness = (colour.r * 0.2126)+(colour.g * 0.7152)+(colour.b * 0.0722);
	
	vec4 color = colour * brightness;
	color = vec4(Sigmoid(color.r),Sigmoid(color.g),Sigmoid(color.b),1.0);
	color = pow(color,vec4(0.5));
	out_Colour = color;

}