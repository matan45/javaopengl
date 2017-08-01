package bloom;

import shader.ShaderProgram;

public class BrightFilterShader extends ShaderProgram {

	public BrightFilterShader(String vs, String frag) {
		super(vs, frag);
	}

	@Override
	protected void getAllUniformLocations() {
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
