package gaussianBlur;

import shader.ShaderProgram;

public class VerticalBlurShader extends ShaderProgram{
	
	private int location_targetHeight;
	
	protected VerticalBlurShader(String vs, String frag) {
		super(vs, frag);
	}
	
	protected void loadTargetHeight(float height){
		super.loadFloat(location_targetHeight, height);
	}

	@Override
	protected void getAllUniformLocations() {	
		location_targetHeight = super.getUniformLocation("targetHeight");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}
