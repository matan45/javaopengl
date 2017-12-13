package postProcessing;

import shader.ShaderProgram;

public class ChromaticAberrationsShader extends ShaderProgram {
	private int location_colourTexture;
	private int location_distortionMax;
	private int location_distortionvignet;
	
	public ChromaticAberrationsShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
		
	}

	@Override
	protected void getAllUniformLocations() {
		location_colourTexture = super.getUniformLocation("colourTexture");
		location_distortionMax = super.getUniformLocation("distortionStrength");
		location_distortionvignet = super.getUniformLocation("vignetStrength");
		
	}
	protected void connectTextureUnits() {
		super.loadInt(location_colourTexture, 0);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		
		
	}
	
	protected void distortionMax(float distortionMax){
		super.loadFloat(location_distortionMax, distortionMax);
	}
	
	protected void distortionvignet(float distortionvignet){
		super.loadFloat(location_distortionvignet, distortionvignet);
	}
	

}
