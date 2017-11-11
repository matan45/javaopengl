package Animation.graph;

import org.lwjgl.opengl.GL11;

import Animation.anim.AnimatedEntity;
import Animation.anim.AnimatedFrame;
import entities.Camera;
import maths.Maths;
import maths.Matrix4f;
import maths.Vector3f;

public class Renderer {

	String vertexFile = "src/Animation/graph/animatedEntity.vs";
	String fragmentFile = "src/Animation/graph/animatedEntity.frag";
	private AnimatedModelShader shader;
	
	public Renderer(Matrix4f projectionMatrix) {
		shader = new AnimatedModelShader(vertexFile, fragmentFile);
		shader.start();
		shader.projectionMatrix.loadMatrix(projectionMatrix);
		shader.stop();
	}


	public void renderScene(Camera camera, AnimatedEntity anim,Vector3f sun) {
		shader.start();
		
		shader.lightDirection.loadVec3(sun);

		GL11.glEnable(GL11.GL_DEPTH_TEST);

		anim.getAim().getMeshes()[0].getTex().bindToUnit(0);

		anim.getAim().getMeshes()[0].render();

		AnimatedFrame frame = anim.getAim().getCurrentAnimation().getCurrentFrame();
		shader.jointTransforms.loadMatrixArray(frame.getJointMatrices());
		
		Matrix4f Transformation=Maths.createTransformationMatrix(anim.getTranslation(),anim.getScale(), anim.getRotation());
		shader.Transformation.loadMatrix(Transformation);
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		shader.viewMatrix.loadMatrix(viewMatrix);

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.stop();
	}

}
