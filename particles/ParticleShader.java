package particles;

import maths.Matrix4f;
import shader.ShaderProgram;

public class ParticleShader extends ShaderProgram {


    private int location_numberOfRows;
    private int location_projectionMatrix;
 
 
    public ParticleShader(String vs, String frag) {
		super(vs, frag);
	}
 
    @Override
    protected void getAllUniformLocations() {
        location_numberOfRows = super.getUniformLocation("numberOfRows");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
 
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "modelViewMatrix");
        super.bindAttribute(5, "texOffsets");
        super.bindAttribute(6, "blendFactor");
    }
 
    protected void loadNumberOfRows(float numberOfRows){
        super.loadFloat(location_numberOfRows, numberOfRows);
    }
 
 
    protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix(location_projectionMatrix, projectionMatrix);
    }
 
}
