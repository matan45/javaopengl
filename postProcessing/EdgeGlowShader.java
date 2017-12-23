package postProcessing;

import maths.Vector2f;
import shader.ShaderProgram;

public class EdgeGlowShader extends ShaderProgram {
	private int location_colourTexture;
	private int location_iResolution;
	private int location_time;
	private int location_speed;
	
	public EdgeGlowShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void getAllUniformLocations() {
		location_colourTexture = super.getUniformLocation("colourTexture");
		location_iResolution = super.getUniformLocation("iResolution");
		location_time=super.getUniformLocation("time");
		location_speed=super.getUniformLocation("speed");
		
	}
	
	protected void connectTextureUnits() {
		super.loadInt(location_colourTexture, 0);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");	
	}
	
	public void loadResolution(float x, float y) {
		super.load2DVector(location_iResolution, new Vector2f(x, y));
	}
	protected void time(float time){
		super.loadFloat(location_time, time);
	}
	protected void speed(float speed){
		super.loadFloat(location_speed, speed);
	}

}
