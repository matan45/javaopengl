package game;

import java.util.ArrayList;
import java.util.List;

import Physics.AABB;
import Physics.PhysicsEngine;
import Physics.PhysicsObject;
import Physics.Sphere;
import entities.Camera;
import entities.Entity;
import entities.Light;
import input.Keyinput;
import input.Mouseinput;
import maths.Vector3f;
import maths.Vector4f;
import renderer.Loader;
import renderer.MasterRenderer;
import renderer.OBJLoader;
import terrains.Terrain;
import texture.ModelTexture;
import texture.TerrainTexture;
import texture.TerrainTexturePack;
import texture.TexturedModel;
import window.Window;

public class PhysicsTest implements GameLogic {
	Loader loader = new Loader();
	PhysicsEngine physicsEngine = new PhysicsEngine();

	List<Light> lights = new ArrayList<>();
	List<Terrain> terrains = new ArrayList<>();
	List<Entity> entitys = new ArrayList<>();
	List<Entity> normalMapentitys = new ArrayList<>();

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
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "flat");
		terrains.add(terrain);

		Light light = new Light(new Vector3f(1000000, 150000, -100000), new Vector3f(1f, 1f, 1f));
		lights.add(light);

		Entity box = new Entity(
				new TexturedModel(OBJLoader.loadObjModel("box", loader), new ModelTexture(loader.loadTexture("box"))),
				new Vector3f(5, -5, -10), 0, 0, 0, 1f);
		box.setX(OBJLoader.getOBJLength().getX());
		box.setY(OBJLoader.getOBJLength().getY());
		box.setZ(OBJLoader.getOBJLength().getZ());
		entitys.add(box);

		Entity box2 = new Entity(
				new TexturedModel(OBJLoader.loadObjModel("box", loader), new ModelTexture(loader.loadTexture("box"))),
				new Vector3f(10, -5, -10), 0, 0, 0, 1f);
		box2.setX(OBJLoader.getOBJLength().getX());
		box2.setY(OBJLoader.getOBJLength().getY());
		box2.setZ(OBJLoader.getOBJLength().getZ());
		entitys.add(box2);

		PhysicsObject obj1 = new PhysicsObject(
				new AABB(box.getPosition(), new Vector3f(box.getX() / 2, box.getY() / 2, box.getZ() / 2)),
				new Vector3f(0.0f, 0.0f, 0.0f));
		PhysicsObject obj2 = new PhysicsObject(
				new AABB(box2.getPosition(), new Vector3f(box.getX() / 2, box.getY() / 2, box.getZ() / 2)),
				new Vector3f(0.0f, 0.0f, 0.0f));
		physicsEngine.AddObject(obj1);
		physicsEngine.AddObject(obj2);

		Entity sphere = new Entity(new TexturedModel(OBJLoader.loadObjModel("earth", loader),
				new ModelTexture(loader.loadTexture("earth"))), new Vector3f(10, 5, -20), 0, 0, 0, 0.01f);
		sphere.setX(OBJLoader.getOBJLength().getX());
		sphere.setY(OBJLoader.getOBJLength().getY());
		sphere.setZ(OBJLoader.getOBJLength().getZ());
		entitys.add(sphere);

		Entity sphere2 = new Entity(new TexturedModel(OBJLoader.loadObjModel("earth", loader),
				new ModelTexture(loader.loadTexture("earth"))), new Vector3f(10, -5, -20), 0, 0, 0, 0.01f);
		sphere2.setX(OBJLoader.getOBJLength().getX());
		sphere2.setY(OBJLoader.getOBJLength().getY());
		sphere2.setZ(OBJLoader.getOBJLength().getZ());
		entitys.add(sphere2);

		PhysicsObject obj3 = new PhysicsObject(new Sphere(sphere.getPosition(), sphere.getY() / 2),
				new Vector3f(0.0f, 0.0f, 0.0f));
		PhysicsObject obj4 = new PhysicsObject(new Sphere(sphere2.getPosition(), sphere2.getY() / 2),
				new Vector3f(0.0f, 0.0f, 0.0f));
		physicsEngine.AddObject(obj3);
		physicsEngine.AddObject(obj4);

		camera = new Camera(null);

		renderer = new MasterRenderer(loader, camera);

	}

	@Override
	public void update() {
		camera.FirstPerson();
		renderer.renderScene(entitys, normalMapentitys, terrains, lights, camera, new Vector4f(0, -1, 0, 1000));

		physicsEngine.Simulate(Window.getDeltaTime());
		entitys.get(0).setPosition(physicsEngine.getObject(0).getPosition());
		entitys.get(1).setPosition(physicsEngine.getObject(1).getPosition());
		physicsEngine.HandleCollisions();

		Mouseinput.resetMouse();
		Keyinput.resetKeyboard();
	}

	@Override
	public void onclose() {
		renderer.cleanUp();
		loader.cleanUp();
	}

}