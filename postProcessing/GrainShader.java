package postProcessing;

import shader.ShaderProgram;

public class GrainShader extends ShaderProgram {
	
	private int location_colourTexture;
	private int location_nosietexture;
	private int location_strength;
	private int location_time;
	
	public GrainShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void getAllUniformLocations() {
		location_colourTexture = super.getUniformLocation("RT");
		location_nosietexture = super.getUniformLocation("nosietexture");
		location_strength = super.getUniformLocation("strength");
		location_time=super.getUniformLocation("time");
		
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		
	}
	protected void connectTextureUnits() {
		super.loadInt(location_colourTexture, 0);
		super.loadInt(location_nosietexture, 1);
	}
	
	protected void strength(float strength){
		super.loadFloat(location_strength, strength);
	}
	protected void time(float time){
		super.loadFloat(location_time, time);
	}


}
