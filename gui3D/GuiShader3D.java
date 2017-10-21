package gui3D;

import entities.Camera;
import maths.Maths;
import maths.Matrix4f;
import shader.ShaderProgram;

public class GuiShader3D extends ShaderProgram {

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;

	public GuiShader3D() {
		super("gui3D.vs", "gui3D.frag");
	}

	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadViewMatrix(Camera camrea) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camrea);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadPorjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}

}
