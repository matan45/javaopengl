#version 150
uniform sampler2D colourTexture;

uniform float strength;
uniform float time;

in vec2 textureCoords;
out vec4 FragColor;

void main(void){
    
    vec4 color = texture(colourTexture, textureCoords);
    
    float x = (textureCoords.x + 4.0 ) * (textureCoords.y + 4.0 ) * (time * 10.0);
	vec4 grain = vec4(mod((mod(x, 13.0) + 1.0) * (mod(x, 123.0) + 1.0), 0.01)-0.005) * strength;
    
	FragColor = color * grain;
    
}

