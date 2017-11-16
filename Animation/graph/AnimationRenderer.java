package Animation.graph;

import Animation.anim.AnimatedEntity;
import Animation.anim.AnimatedFrame;
import entities.Camera;
import maths.Maths;
import maths.Matrix4f;
import maths.Vector3f;

public class AnimationRenderer {

	String vertexFile = "src/Animation/graph/animatedEntity.vs";
	String fragmentFile = "src/Animation/graph/animatedEntity.frag";
	private AnimatedModelShader shader;

	public AnimationRenderer(Matrix4f projectionMatrix) {
		shader = new AnimatedModelShader(vertexFile, fragmentFile);
		shader.start();
		shader.projectionMatrix.loadMatrix(projectionMatrix);
		shader.stop();
	}

	public void renderScene(Camera camera, AnimatedEntity anim, Vector3f sun) {
		shader.start();

		shader.lightDirection.loadVec3(sun);

		anim.getAim().getMeshes()[0].getTex().bindToUnit(0);
		for (int i = 0; i < anim.getAim().getMeshes().length; i++) {
			anim.getAim().getMeshes()[i].render();
		}

		AnimatedFrame frame = anim.getAim().getCurrentAnimation().getCurrentFrame();
		shader.jointTransforms.loadMatrixArray(frame.getJointMatrices());

		Matrix4f Transformation = Maths.createTransformationMatrix(anim.getTranslation(), anim.getScale(),
				anim.getRotation());
		shader.Transformation.loadMatrix(Transformation);
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		shader.viewMatrix.loadMatrix(viewMatrix);

		shader.stop();
	}

	public void close() {
		shader.cleanUp();
	}

}
