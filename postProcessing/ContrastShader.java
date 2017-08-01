package postProcessing;

import shader.ShaderProgram;

public class ContrastShader extends ShaderProgram {

	
	public ContrastShader(String vs,String frag) {
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
