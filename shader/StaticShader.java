package shader;

import entities.Camera;
import entities.Light;
import maths.Maths;
import maths.Matrix4f;

public class StaticShader extends ShaderProgram {
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	
	public StaticShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix=super.getUniformLocation("transformationMatrix");
		location_projectionMatrix=super.getUniformLocation("projectionMatrix");
		location_viewMatrix=super.getUniformLocation("viewMatrix");
		location_lightPosition=super.getUniformLocation("lightPosition");
		location_lightColour=super.getUniformLocation("lightColour");
		location_shineDamper=super.getUniformLocation("shineDamper");
		location_reflectivity=super.getUniformLocation("reflectivity");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadShineVariables(float damper,float reflectivity ){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadLight(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
	}
	
	public void loadViewnMatrix(Camera camrea){
		Matrix4f viewMatrix=Maths.createViewMatrix(camrea);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadPorjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}

}
