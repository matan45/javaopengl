package vbo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Camera;
import entities.Entity;
import entities.Light;
import shader.StaticShader;
import texture.TexturedModel;

public class MasterRenderer {
	StaticShader shader;
	Renderer renderer;

	Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();

	public MasterRenderer(StaticShader shader) {
		this.shader = shader;
		renderer = new Renderer(shader);
	}

	public void render(Light sun, Camera camera) {
		shader.start();
		shader.loadLight(sun);
		shader.loadViewnMatrix(camera);
		renderer.render(entities);

		shader.stop();
		entities.clear();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

}
