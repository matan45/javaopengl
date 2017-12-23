#version 150

uniform sampler2D colourTexture;

uniform float time;
uniform float speed;
uniform vec2 iResolution;

in vec2 textureCoords;
out vec4 FragColor;

const float kernelWidth = 3.;
const float kernelHeight = 3.;
const float kernelSize = kernelWidth * kernelHeight;

bool isEdgeFragment(vec2 fragCoord) {
	float kernel[(int(kernelWidth * kernelHeight))];
	kernel[0] = -1.;
	kernel[1] = -1.;
	kernel[2] = -1.;
	kernel[3] = -1.;
	kernel[4] =  8.;
	kernel[5] = -1.;
	kernel[6] = -1.;
	kernel[7] = -1.;
	kernel[8] = -1.;
	
	vec4 result = vec4(0.);
	vec2 uv = fragCoord;
	
	for(float y = 0.; y < kernelHeight; ++y) {
		for(float x = 0.; x < kernelWidth; ++x) {
			result += texture(colourTexture, vec2(uv.x + (float(int(x - kernelWidth / 2.)) / iResolution.x), 
												uv.y + (float(int(y - kernelHeight / 2.)) / iResolution.y)))
										   * kernel[int(x + (y * kernelWidth))];
		}
	}
	
	return ((length(result) > 0.2) ? true : false);
}

void main(void) {

	vec4 actualColor = texture(colourTexture,textureCoords);
	if(!isEdgeFragment(textureCoords)) {
		FragColor = actualColor;
	} else {
		FragColor = vec4(0., 1., 0., 1.) * sin(time * speed) + actualColor * cos(time * speed);
    }
}

