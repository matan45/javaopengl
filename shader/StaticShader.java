package shader;

import entities.Camera;
import maths.Maths;
import maths.Matrix4f;

public class StaticShader extends ShaderProgram {
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;

	public StaticShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix=super.getUniformLocation("transformationMatrix");
		location_projectionMatrix=super.getUniformLocation("projectionMatrix");
		location_viewMatrix=super.getUniformLocation("viewMatrix");
	}
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	public void loadViewnMatrix(Camera camrea){
		Matrix4f viewMatrix=Maths.createViewMatrix(camrea);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	public void loadPorjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}

}
