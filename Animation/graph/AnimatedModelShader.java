package Animation.graph;

import Animation.shaders.ShaderProgram;
import Animation.shaders.UniformMat4Array;
import Animation.shaders.UniformMatrix;
import Animation.shaders.UniformSampler;
import Animation.shaders.UniformVec3;

public class AnimatedModelShader extends ShaderProgram {

	private static final int MAX_JOINTS = 150;// max number of joints in a
												// skeleton
	private static final int DIFFUSE_TEX_UNIT = 0;

	protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	protected UniformMatrix Transformation = new UniformMatrix("Transformation");
	protected UniformVec3 lightDirection = new UniformVec3("lightDirection");
	protected UniformMat4Array jointTransforms = new UniformMat4Array("jointsMatrix", MAX_JOINTS);
	private UniformSampler diffuseMap = new UniformSampler("diffuseMap");

	public AnimatedModelShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
		super.storeAllUniformLocations(projectionMatrix,viewMatrix,Transformation, diffuseMap, lightDirection, jointTransforms);
		connectTextureUnits();
		// TODO Auto-generated constructor stub
	}

	private void connectTextureUnits() {
		super.start();
		diffuseMap.loadTexUnit(DIFFUSE_TEX_UNIT);
		super.stop();
	}

}
