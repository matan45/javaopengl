package postProcessing;

import shader.ShaderProgram;

public class ChromaticAberrationsShader extends ShaderProgram {
	private int location_colourTexture;
	private int location_distortionMax;
	private int location_distortionIter;
	
	public ChromaticAberrationsShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
		
	}

	@Override
	protected void getAllUniformLocations() {
		location_colourTexture = super.getUniformLocation("RT");
		location_distortionMax = super.getUniformLocation("distortionmax");
		location_distortionIter = super.getUniformLocation("distortioniter");
		
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
	
	protected void distortionIter(int distortionIter){
		super.loadInt(location_distortionIter, distortionIter);
	}
	

}
