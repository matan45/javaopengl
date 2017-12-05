#version 400 core 
 
 in vec2 pass_textureCoords;
 in vec3 surfaceNormal;
 in vec3 toLightVector[4];
 in vec3 toCameraVector;
 in float visibility;
 
layout (location = 0) out vec4 out_Color;
layout (location = 1) out vec4 out_BrightColor;
 
 uniform sampler2D modelTexture;
 uniform sampler2D spcularMap;
 uniform float usesSpecularMap;
 uniform vec3 lightColour[4];
 uniform vec3 attenuation[4];
 uniform float shineDamper;
 uniform float reflectivity;
 uniform vec3 skyColour;
 
 const float levels = 5.0;
 
 // tonemapping
float A = 0.15;
float B = 0.50;
float C = 0.10;
float D = 0.15;
float E = 0.50;
float F = 0.10;
float W = 11.2;

vec3 uncharted2Math(vec3 X){
	return ((X * (A * X + C * B) + D * E) / (X * (A * X + B) + D * F)) - E / F;
}

vec3 uncharted2Tonemap(vec3 color){

	float exposureBias = 2.0;
	
	vec3 curr = uncharted2Math(exposureBias * color);
	vec3 whitScale = vec3(1.0) / uncharted2Math(vec3(W));
	
	return (curr * whitScale);
}
 
 void main () {
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for(int i=0;i<4;i++){
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDotl = dot(unitNormal,unitLightVector);
		float brightness = max(nDotl,0.0);
		float level = floor(brightness * levels);
		brightness = level / levels;
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
		float specularFactor = dot(reflectedLightDirection,unitVectorToCamera);
		specularFactor = max(specularFactor,0.0);
		float dampedFactor = pow(specularFactor,shineDamper);
		level = floor(dampedFactor * levels);
		dampedFactor = level / levels;
		totalDiffuse = totalDiffuse + (brightness * lightColour[i]) / attFactor;
		totalSpecular = totalSpecular +  (dampedFactor * reflectivity * lightColour[i]) / attFactor;
	}
	
	totalDiffuse = max(totalDiffuse,0.2);
	
	vec4 textureColour = texture(modelTexture,pass_textureCoords);
 	if(textureColour.a<0.5){
 		discard;
 	} 
 	
 	if(usesSpecularMap > 0.5){
 		vec4 mapInfo = texture(spcularMap,pass_textureCoords);
 		totalSpecular *= mapInfo.r;
 	} 	
    out_Color = vec4(totalDiffuse,1.0) * textureColour + vec4(totalSpecular,1.0);
    out_Color = mix(vec4(skyColour,1.0),out_Color,visibility);
    vec3 temp= vec3(out_Color.r,out_Color.g,out_Color.b);
    temp=uncharted2Tonemap(temp);
    temp=pow(temp, vec3(1.0 / 2.2));
	out_BrightColor = vec4(temp,1.0);
}