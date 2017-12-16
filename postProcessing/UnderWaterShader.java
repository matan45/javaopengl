package postProcessing;

import shader.ShaderProgram;

public class UnderWaterShader extends ShaderProgram {
	private int location_colourTexture;
	private int location_speed;
	private int location_time;
	
	public UnderWaterShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void getAllUniformLocations() {
		location_colourTexture = super.getUniformLocation("colourTexture");
		location_speed = super.getUniformLocation("speed");
		location_time=super.getUniformLocation("time");
		
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		
	}
	protected void connectTextureUnits() {
		super.loadInt(location_colourTexture, 0);
	}
	
	protected void speed(float speed){
		super.loadFloat(location_speed, speed);
	}
	protected void time(float time){
		super.loadFloat(location_time, time);
	}
}
