package flare.sunRenderer;

import org.lwjgl.opengl.GL11;

import entities.Camera;
import maths.Maths;
import maths.Matrix4f;
import maths.Vector3f;
import flare.openglObjects.Vao;
import utill.OpenGlUtils;

public class SunRenderer {

	private final SunShader shader;

	private static final float[] POSITIONS = { -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f };

	private final Vao quad;

	public SunRenderer() {
		this.shader = new SunShader();
		this.quad = Vao.create();
		quad.bind();
		quad.storeData(4, POSITIONS);
		quad.unbind();
	}

	public void render(Sun sun, Vector3f cameraPosition,Camera camera,Matrix4f ProjectionMatrix) {
		prepare(sun, cameraPosition,camera,ProjectionMatrix);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		endRendering();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	private void prepare(Sun sun, Vector3f cameraPosition,Camera camera,Matrix4f ProjectionMatrix) {
		OpenGlUtils.antialias(false);
		GL11.glDepthMask(false);
		OpenGlUtils.enableAlphaBlending();
		shader.start();
		Matrix4f mvpMat = calculateMvpMatrix(sun, cameraPosition,camera,ProjectionMatrix);
		shader.mvpMatrix.loadMatrix(mvpMat);
		quad.bind(0);
		sun.getTexture().bindToUnit(0);
	}

	private Matrix4f calculateMvpMatrix(Sun sun, Vector3f cameraPosition,Camera camera,Matrix4f ProjectionMatrix ) {
		Matrix4f modelMatrix = new Matrix4f();
		Vector3f sunPos = sun.getWorldPosition(cameraPosition);
		Matrix4f.translate(sunPos, modelMatrix, modelMatrix);
		Matrix4f ViewMatrix=new Matrix4f();
		ViewMatrix=Maths.createViewMatrix(camera);
		Matrix4f modelViewMat = applyViewMatrix(modelMatrix, ViewMatrix);
		Matrix4f.scale(new Vector3f(sun.getScale(), sun.getScale(), sun.getScale()), modelViewMat, modelViewMat);
		return Matrix4f.mul(ProjectionMatrix, modelViewMat, null);
	}

	/**
	 * Check the particle tutorial for explanations of this. Basically we remove
	 * the rotation effect of the view matrix, so that the sun quad is always
	 * facing the camera.
	 * 
	 * @param modelMatrix
	 * @param viewMatrix
	 * @return The model-view matrix.
	 */
	private Matrix4f applyViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix) {
		modelMatrix.m00 = viewMatrix.m00;
		modelMatrix.m01 = viewMatrix.m10;
		modelMatrix.m02 = viewMatrix.m20;
		modelMatrix.m10 = viewMatrix.m01;
		modelMatrix.m11 = viewMatrix.m11;
		modelMatrix.m12 = viewMatrix.m21;
		modelMatrix.m20 = viewMatrix.m02;
		modelMatrix.m21 = viewMatrix.m12;
		modelMatrix.m22 = viewMatrix.m22;
		return Matrix4f.mul(viewMatrix, modelMatrix, null);
	}

	private void endRendering() {
		GL11.glDepthMask(true);
		quad.unbind(0);
		shader.stop();
		OpenGlUtils.disableBlending();
	}

}
