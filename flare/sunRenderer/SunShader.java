package flare.sunRenderer;

import flare.shaders.ShaderProgram;
import flare.shaders.UniformMatrix;
import flare.shaders.UniformSampler;

public class SunShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "src/flare/sunRenderer/sunVertex.glsl";
	private static final String FRAGMENT_SHADER = "src/flare/sunRenderer/sunFragment.glsl";
	
	protected UniformSampler sunTexture = new UniformSampler("sunTexture");
	protected UniformMatrix mvpMatrix = new UniformMatrix("mvpMatrix");

	public SunShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position");
		super.storeAllUniformLocations(sunTexture, mvpMatrix);
		connectTextureUnits();
	}

	private void connectTextureUnits() {
		super.start();
		sunTexture.loadTexUnit(0);
		super.stop();
	}

}
