package vbo;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entities.Entity;
import maths.Maths;
import maths.Matrix4f;
import shader.StaticShader;
import texture.ModelTexture;
import texture.TexturedModel;
import window.WindowManager;

public class Renderer {
	static final float FOV = 70;
	static final float NEAR_PLANE = 0.1f;
	static final float FAR_PLANE = 1000;

	Matrix4f projectionMatrix;
	StaticShader shader;

	
	public Renderer(StaticShader shader) {
		this.shader=shader;
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		shader.start();
		shader.loadPorjectionMatrix(projectionMatrix);
		shader.stop();
	}
/*
	public void render(Entity entity, StaticShader shader) {
		RawModel rawmodel = entity.getModel().getRawModel();
		GL30.glBindVertexArray(rawmodel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		Matrix4f transfromationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transfromationMatrix);
		shader.loadShineVariables(entity.getModel().getTexture().getShineDamper(),entity.getModel().getTexture().getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawmodel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	*/
	
	public void render(Map<TexturedModel ,List<Entity>> entities){
		for(TexturedModel model:entities.keySet()){
			prepaeTextured(model);
			List<Entity>batch = entities.get(model);
			for(Entity entity:batch){
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTextured();
		}
	}
	private void prepaeTextured(TexturedModel model){
		RawModel rawmodel = model.getRawModel();
		GL30.glBindVertexArray(rawmodel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture =model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(),texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}
	private void unbindTextured(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		
	}
	private void prepareInstance(Entity entity){
		Matrix4f transfromationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transfromationMatrix);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) WindowManager.getWindow("main").getWidth()
				/ (float) WindowManager.getWindow("main").getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;

	}
}
