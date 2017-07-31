package shadows;

import maths.Matrix4f;
import shader.ShaderProgram;

public class ShadowShader extends ShaderProgram {

	private int location_mvpMatrix;

	protected ShadowShader(String vs, String frag) {
		super(vs, frag);
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvpMatrix = super.getUniformLocation("mvpMatrix");

	}

	protected void loadMvpMatrix(Matrix4f mvpMatrix) {
		super.loadMatrix(location_mvpMatrix, mvpMatrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
		super.bindAttribute(1, "in_textureCoords");
	}

}
