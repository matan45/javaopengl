package renderer;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import entities.Camera;
import entities.Entity;
import entities.Light;
import maths.Matrix4f;
import maths.Vector4f;
import normalMappingRenderer.NormalMappingRenderer;
import shader.StaticShader;
import shader.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import texture.TexturedModel;
import window.WindowManager;

public class MasterRenderer {
	static final float FOV = 70;
	static final float NEAR_PLANE = 0.1f;
	static final float FAR_PLANE = 1000;

	public static final float RED = 0.65f;
	public static final float GREEN = 0.76f;
	public static final float BLUE = 0.94f;
	
	NormalMappingRenderer normalMapRenderer;

	Matrix4f projectionMatrix;
	SkyboxRenderer skyboxRenderer;

	StaticShader shader = new StaticShader("entity.vs", "entity.frag");
	EntityRenderer renderer;

	TerrainRenderer terrainRenderer;
	TerrainShader terrainShader = new TerrainShader("terrain.vs", "terrain.frag");

	Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	Map<TexturedModel, List<Entity>> normalMapentities = new HashMap<TexturedModel, List<Entity>>();
	List<Terrain> terrains = new ArrayList<Terrain>();

	public MasterRenderer(Loader loader) {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		normalMapRenderer=new NormalMappingRenderer(projectionMatrix);
	}

	public void renderScene(List<Entity> entities,List<Entity> normalEntities, List<Terrain> terrains, List<Light> lights, Camera camera,Vector4f clipPlane) {
		for (Terrain terrain : terrains)
			processTerrain(terrain);

		for (Entity entity : entities)
			processEntity(entity);
		for (Entity entity : normalEntities)
			processNormalMapEntity(entity);

		render(lights, camera,clipPlane);
	}

	public void prepare() {
		// Set the clear color
		glClearColor(RED, GREEN, BLUE, 1.0f);
		glEnable(GL_DEPTH_TEST);
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	private void render(List<Light> lights, Camera camera,Vector4f clipPlane) {
		prepare();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		normalMapRenderer.render(normalMapentities, clipPlane, lights, camera);
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		terrains.clear();
		entities.clear();
		normalMapentities.clear();

	}

	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
		normalMapRenderer.cleanUp();
	}

	private void processEntity(Entity entity) {
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
	
	private void processNormalMapEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = normalMapentities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			normalMapentities.put(entityModel, newBatch);
		}
	}

	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
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

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

}
