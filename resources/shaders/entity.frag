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
	out_BrightColor = vec4(1.0,0.0,0.0,1.0);
}