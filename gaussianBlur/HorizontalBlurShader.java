package gaussianBlur;

import shader.ShaderProgram;

public class HorizontalBlurShader extends ShaderProgram {
	
	private int location_targetWidth;
	
	protected HorizontalBlurShader(String vs, String frag) {
		super(vs, frag);
	}

	protected void loadTargetWidth(float width){
		super.loadFloat(location_targetWidth, width);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_targetWidth = super.getUniformLocation("targetWidth");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
}
