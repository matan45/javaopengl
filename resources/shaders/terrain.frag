#version 400 core


in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;
in vec4 shadowCoords;

layout (location = 0) out vec4 out_Color;
layout (location = 1) out vec4 out_BrightColor;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;
uniform sampler2D shadowMap;

uniform vec3 lightColour[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

const int pcfCount = 2;
const float totalTexels = (pcfCount * 2.0 + 1.0) * (pcfCount * 2.0 + 1.0);

// tonemapping
vec3 Burgess(vec3 color){
	vec3 maxColor = max( vec3(0.0), color - 0.004);
	vec3 retColor = (maxColor * (6.2 * maxColor + 0.05)) / (maxColor * (6.2 * maxColor + 2.3) + 0.06);
	return retColor;

}

void main(void){

	float mapSize = 2048.0;
	float texelSize = 1.0 / mapSize;
	float total =0.0;
	
	for(int x = -pcfCount ; x <= pcfCount ; x++){
		for(int y = -pcfCount; y <=pcfCount; y++){
			float objectNearestLight = texture(shadowMap, shadowCoords.xy + vec2(x,y) * texelSize).r;
			if(shadowCoords.z > objectNearestLight+0.002){
				total += 1.0;
			}
		}	
	}
	total /= totalTexels;
	
	float lightFactor = 1.0 - (total * shadowCoords.w);
	
	vec4 blendMapColour = texture(blendMap,pass_textureCoordinates);
	float backTextureAmount = 1 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);
	vec2 tiledCoords = pass_textureCoordinates * 40;
	vec4 backgroundTextureColour = texture(backgroundTexture,tiledCoords) * backTextureAmount;
	vec4 rTextureColour = texture(rTexture,tiledCoords) * blendMapColour.r;
	vec4 gTextureColour = texture(gTexture,tiledCoords) * blendMapColour.g;
	vec4 bTextureColour = texture(bTexture,tiledCoords) * blendMapColour.b;
	
	vec4 totalColour = backgroundTextureColour + rTextureColour + gTextureColour + bTextureColour;

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
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
		float specularFactor = dot(reflectedLightDirection , unitVectorToCamera);
		specularFactor = max(specularFactor,0.0);
		float dampedFactor = pow(specularFactor,shineDamper);
		totalDiffuse = totalDiffuse + (brightness * lightColour[i]) / attFactor;
		totalSpecular = totalSpecular +  (dampedFactor * reflectivity * lightColour[i]) / attFactor;
	}
	totalDiffuse = max(totalDiffuse * lightFactor,0.4);

	out_Color =  vec4(totalDiffuse,1.0) * totalColour + vec4(totalSpecular,1.0);
	out_Color = mix(vec4(skyColour,1.0),out_Color,visibility);
	vec3 tonemapping = vec3(totalDiffuse * totalColour + vec3(totalSpecular));
	vec3 outcolor = Burgess(tonemapping);
	out_BrightColor = vec4(outcolor,1.0);
}