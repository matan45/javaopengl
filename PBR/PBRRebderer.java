package PBR;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entities.Camera;
import entities.Light;
import maths.Maths;
import maths.Matrix4f;
import renderer.MasterRenderer;
import renderer.RawModel;

public class PBRRebderer {
	private PBRShader shader;

	public PBRRebderer(Matrix4f projectionMatrix) {
		this.shader = new PBRShader("PBRE.vs", "PBRE.frag");
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(PBREntity entity, List<Light> lights, Camera camera) {
		shader.start();
		prepare(lights, camera);
		prepareTexturedModel(entity.getModel(), entity.getMaterial());
		prepareInstance(entity);
		GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT,
				0);

		unbindTexturedModel();

		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	private void prepareTexturedModel(RawModel rawModel, Material mat) {
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mat.getAlbedoMap());
		if (mat.getAlbedo() != null)
			shader.loadalbedo(mat.getAlbedo());
		shader.loadmetallic(mat.getMetallic());
		shader.loadhasMetallicMap(mat.isHasmetallicMap());
		if (mat.isHasmetallicMap()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mat.getMetallicMap());
		}
		shader.loadroughness(mat.getRoughness());
		shader.loadhasroughnessMap(mat.isHasroughnessMap());
		if (mat.isHasroughnessMap()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE2);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mat.getRoughnessMap());
		}
		shader.loadhasnormalMap(mat.isHasnormalMap());
		if (mat.isHasnormalMap()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE3);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mat.getNormalMap());
		}
		shader.loadao(mat.getAo());
		shader.loadhasaoMap(mat.isHasaoMap());
		if (mat.isHasaoMap()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE4);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mat.getAoMap());
		}

	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(PBREntity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation().x,entity.getRotation().y,entity.getRotation().z,entity.getScale().x);
		shader.loadTransformationMatrix(transformationMatrix);
	}

	private void prepare(List<Light> lights, Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		shader.loadLights(lights);
		shader.loadViewMatrix(viewMatrix);
		shader.loadcamera(camera.getPosition());
	}

}
