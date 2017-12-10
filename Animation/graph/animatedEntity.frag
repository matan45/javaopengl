#version 150

const vec2 lightBias = vec2(0.7, 0.6);//just indicates the balance between diffuse and ambient lighting

in vec2 pass_textureCoords;
in vec3 pass_normal;

out vec4 out_colour;

uniform sampler2D diffuseMap;
uniform vec3 lightDirection;


void main(void){
	
	vec4 diffuseColour = texture(diffuseMap, pass_textureCoords);	
		
	vec3 unitNormal = normalize(pass_normal);
	float diffuseLight = max(dot(-lightDirection, unitNormal), 0.0) * lightBias.x + lightBias.y;
	
	vec4 color = diffuseColour * diffuseLight;
	 // HDR tonemapping
    color.rgb = color.rgb / (color.rgb + vec3(1.0));
     // gamma correct
    color.rgb = pow(color.rgb, vec3(1.0/2.2)); 
    
	out_colour = color;
	
}

