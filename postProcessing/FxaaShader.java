package postProcessing;

import maths.Vector2f;
import shader.ShaderProgram;

public class FxaaShader extends ShaderProgram {
	private int location_colourTexture;
	private int location_iResolution;
	public FxaaShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void getAllUniformLocations() {
		location_colourTexture = super.getUniformLocation("colourTexture");
		location_iResolution = super.getUniformLocation("iResolution");
		
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

}
