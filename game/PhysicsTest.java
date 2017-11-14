package game;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import Physics.AABB;
import Physics.Collider.Layers;
import Physics.OnOverLaps;
import Physics.PhysicsEngine;
import Physics.PhysicsObject;
import Physics.Sphere;
import entities.Camera;
import entities.Entity;
import entities.Light;
import input.Keyinput;
import maths.Vector3f;
import maths.Vector4f;
import objConverter.OBJFileLoader;
import renderer.Loader;
import renderer.MasterRenderer;
import terrains.Terrain;
import texture.ModelTexture;
import texture.TerrainTexture;
import texture.TerrainTexturePack;
import texture.TexturedModel;
import window.SceneManager;
import window.Window;

public class PhysicsTest implements GameLogic {
	Loader loader = new Loader();
	PhysicsEngine physicsEngine = new PhysicsEngine();

	List<Light> lights = new ArrayList<>();
	List<Terrain> terrains = new ArrayList<>();
	List<Entity> entitys = new ArrayList<>();
	List<Entity> normalMapentitys = new ArrayList<>();

	AABB b1, b2;

	Camera camera;

	MasterRenderer renderer;

	@Override
	public void preupdate() {
		
		// TERRAIN TEXTURE
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		// RANDOM HEIGHTS
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "map");
		terrains.add(terrain);

		Entity box = new Entity(
				new TexturedModel(OBJFileLoader.loadOBJ("lamp", loader), new ModelTexture(loader.loadTexture("lamp"))),
				new Vector3f(25, 0, -20), 0, 0, 0, 0.5f);
		box.setX(OBJFileLoader.getOBJLength().getX());
		box.setY(OBJFileLoader.getOBJLength().getY());
		box.setZ(OBJFileLoader.getOBJLength().getZ());
		entitys.add(box);

		Entity box2 = new Entity(
				new TexturedModel(OBJFileLoader.loadOBJ("box", loader), new ModelTexture(loader.loadTexture("box"))),
				new Vector3f(30, 0, -20), 0, 0, 0, 1f);
		box2.setX(OBJFileLoader.getOBJLength().getX());
		box2.setY(OBJFileLoader.getOBJLength().getY());
		box2.setZ(OBJFileLoader.getOBJLength().getZ());
		entitys.add(box2);

		b1 = new AABB(box.getPosition(), new Vector3f(box.getX() / 2, box.getY() / 2, box.getZ() / 2),
				Layers.Physics_Layer);

		b2 = new AABB(box2.getPosition(), new Vector3f(box2.getX() / 2, box2.getY() / 2, box2.getZ() / 2),
				Layers.Physics_Layer);

		PhysicsObject obj1 = new PhysicsObject(b1, new Vector3f(1.0f, 0.0f, 0.0f), 100);
		PhysicsObject obj2 = new PhysicsObject(b2, new Vector3f(0.0f, 0.0f, 0.0f), 100);

		physicsEngine.AddObject(obj1);
		physicsEngine.AddObject(obj2);

		Entity sphere = new Entity(
				new TexturedModel(OBJFileLoader.loadOBJ("ball", loader), new ModelTexture(loader.loadTexture("earth"))),
				new Vector3f(5, -7, -15), 0, 0, 0, 1f);
		sphere.setX(OBJFileLoader.getOBJLength().getX());
		sphere.setY(OBJFileLoader.getOBJLength().getY());
		sphere.setZ(OBJFileLoader.getOBJLength().getZ());
		entitys.add(sphere);

		Entity sphere2 = new Entity(
				new TexturedModel(OBJFileLoader.loadOBJ("ball", loader), new ModelTexture(loader.loadTexture("sun"))),
				new Vector3f(5, 5, -13), 0, 0, 0, 1f);
		sphere2.setX(OBJFileLoader.getOBJLength().getX());
		sphere2.setY(OBJFileLoader.getOBJLength().getY());
		sphere2.setZ(OBJFileLoader.getOBJLength().getZ());
		sphere2.getModel().getTexture().setUseFakeLighting(true);
		entitys.add(sphere2);

		Entity sphere3 = new Entity(
				new TexturedModel(OBJFileLoader.loadOBJ("ball", loader), new ModelTexture(loader.loadTexture("moon"))),
				new Vector3f(10, 0, -50), 0, 0, 0, 1f);
		sphere3.setX(OBJFileLoader.getOBJLength().getX());
		sphere3.setY(OBJFileLoader.getOBJLength().getY());
		sphere3.setZ(OBJFileLoader.getOBJLength().getZ());
		entitys.add(sphere3);

		PhysicsObject obj3 = new PhysicsObject(
				new Sphere(sphere.getPosition(), sphere.getY() / 2, Layers.Physics_Layer),
				new Vector3f(0.0f, 0.0f, 0.0f), 100);
		PhysicsObject obj4 = new PhysicsObject(
				new Sphere(sphere2.getPosition(), sphere2.getY() / 2, Layers.Physics_Layer),
				new Vector3f(0.0f, 0.0f, 0.0f), 100);
		PhysicsObject obj5 = new PhysicsObject(
				new Sphere(sphere3.getPosition(), sphere3.getY() / 2, Layers.Physics_Layer),
				new Vector3f(0.0f, 0.0f, 2.0f), 100);
		physicsEngine.AddObject(obj3);
		physicsEngine.AddObject(obj4);
		physicsEngine.AddObject(obj5);

		Entity sphere4 = new Entity(new TexturedModel(OBJFileLoader.loadOBJ("earth", loader),
				new ModelTexture(loader.loadTexture("earth"))), b1.getMinExtents(), 0, 0, 0, 0.001f);
		sphere2.setX(OBJFileLoader.getOBJLength().getX());
		sphere2.setY(OBJFileLoader.getOBJLength().getY());
		sphere2.setZ(OBJFileLoader.getOBJLength().getZ());
		entitys.add(sphere4);

		Entity sphere5 = new Entity(new TexturedModel(OBJFileLoader.loadOBJ("earth", loader),
				new ModelTexture(loader.loadTexture("earth"))), b1.getMaxExtents(), 0, 0, 0, 0.001f);
		sphere2.setX(OBJFileLoader.getOBJLength().getX());
		sphere2.setY(OBJFileLoader.getOBJLength().getY());
		sphere2.setZ(OBJFileLoader.getOBJLength().getZ());
		entitys.add(sphere5);

		Entity sphere6 = new Entity(new TexturedModel(OBJFileLoader.loadOBJ("earth", loader),
				new ModelTexture(loader.loadTexture("earth"))), b2.getMinExtents(), 0, 0, 0, 0.001f);
		sphere2.setX(OBJFileLoader.getOBJLength().getX());
		sphere2.setY(OBJFileLoader.getOBJLength().getY());
		sphere2.setZ(OBJFileLoader.getOBJLength().getZ());
		entitys.add(sphere6);

		Entity sphere7 = new Entity(new TexturedModel(OBJFileLoader.loadOBJ("earth", loader),
				new ModelTexture(loader.loadTexture("earth"))), b2.getMaxExtents(), 0, 0, 0, 0.001f);
		sphere2.setX(OBJFileLoader.getOBJLength().getX());
		sphere2.setY(OBJFileLoader.getOBJLength().getY());
		sphere2.setZ(OBJFileLoader.getOBJLength().getZ());
		entitys.add(sphere7);

		camera = new Camera(null);

		Light light = new Light(new Vector3f(sphere2.getPosition().x, sphere2.getY() / 2 + sphere2.getPosition().y,
				sphere2.getPosition().z), new Vector3f(2f, 2f, 2f));
		Light light2 = new Light(sphere2.getPosition(), new Vector3f(2f, 2f, 2f));
		lights.add(light);
		lights.add(light2);

		renderer = new MasterRenderer(loader, camera);

		physicsEngine.setCollision(new OnOverLaps() {

			@Override
			public void OnCollision(PhysicsObject p1, PhysicsObject p2) {

			}
		});

	}

	@Override
	public void update() {
		camera.FirstPerson();

		renderer.renderScene(entitys, normalMapentitys, terrains, lights, camera, new Vector4f(0, -1, 0, 1000));
		if(Keyinput.keyDown(GLFW.GLFW_KEY_K)){
			SceneManager.changeScene(new Game());
		}
		
	}

	@Override
	public void fixedupdate() {
		physicsEngine.Simulate(Window.getDeltaTime());
		entitys.get(0).setPosition(physicsEngine.getObject(0).getPosition());
		entitys.get(1).setPosition(physicsEngine.getObject(1).getPosition());
		entitys.get(2).setPosition(physicsEngine.getObject(2).getPosition());
		entitys.get(3).setPosition(physicsEngine.getObject(3).getPosition());
		entitys.get(4).setPosition(physicsEngine.getObject(4).getPosition());
		physicsEngine.HandleCollisions();
		b1.setRotation(new Vector3f(entitys.get(0).getRotX(), entitys.get(0).getRotY(), entitys.get(0).getRotZ()));
		b2.setRotation(new Vector3f(entitys.get(1).getRotX(), entitys.get(1).getRotY(), entitys.get(1).getRotZ()));
		entitys.get(3).increaseRotation(0, 0.2f, 0);

	}

	@Override
	public void onclose() {
		renderer.cleanUp();
		loader.cleanUp();
	}

}
