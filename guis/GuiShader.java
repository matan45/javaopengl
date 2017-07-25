package guis;

import maths.Matrix4f;
import shader.ShaderProgram;

public class GuiShader extends ShaderProgram {
	

	private int location_transformationMatrix;

	public GuiShader() {
		super("gui.vs", "gui.frag");
	}

	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
