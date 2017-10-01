package game;

import java.util.ArrayList;
import java.util.List;

import Physics.AABB;
import Physics.OnOverLaps;
import Physics.Collider.Layers;
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
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "flat");
		terrains.add(terrain);

		Light light = new Light(new Vector3f(1000, 1500, -1000), new Vector3f(2f, 2f, 2f));
		lights.add(light);

		Entity box = new Entity(
				new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("box"))),
				new Vector3f(25, 0, -20), -50, 14, 180, 0.5f);
		box.setX(OBJLoader.getOBJLength().getX());
		box.setY(OBJLoader.getOBJLength().getY());
		box.setZ(OBJLoader.getOBJLength().getZ());
		entitys.add(box);

		Entity box2 = new Entity(
				new TexturedModel(OBJLoader.loadObjModel("box", loader), new ModelTexture(loader.loadTexture("box"))),
				new Vector3f(30, 0, -20), 0, 0, 0, 1f);
		box2.setX(OBJLoader.getOBJLength().getX());
		box2.setY(OBJLoader.getOBJLength().getY());
		box2.setZ(OBJLoader.getOBJLength().getZ());
		entitys.add(box2);

		b1 = new AABB(box.getPosition(), new Vector3f(box.getX() / 2, box.getY() / 2, box.getZ() / 2),
				Layers.Physics_Layer);

		b2 = new AABB(box2.getPosition(), new Vector3f(box2.getX() / 2, box2.getY() / 2, box2.getZ() / 2),
				Layers.Physics_Layer);

		PhysicsObject obj1 = new PhysicsObject(b1, new Vector3f(3.0f, 0.0f, 0.0f), 100);
		PhysicsObject obj2 = new PhysicsObject(b2, new Vector3f(1.0f, 0.0f, 0.0f), 100);

		physicsEngine.AddObject(obj1);
		physicsEngine.AddObject(obj2);

		Entity sphere = new Entity(new TexturedModel(OBJLoader.loadObjModel("ball", loader),
				new ModelTexture(loader.loadTexture("earth"))), new Vector3f(5, 5, -20), 0, 0, 0, 1f);
		sphere.setX(OBJLoader.getOBJLength().getX());
		sphere.setY(OBJLoader.getOBJLength().getY());
		sphere.setZ(OBJLoader.getOBJLength().getZ());
		entitys.add(sphere);

		Entity sphere2 = new Entity(new TexturedModel(OBJLoader.loadObjModel("ball", loader),
				new ModelTexture(loader.loadTexture("earth"))), new Vector3f(5, -5, -20), 0, 0, 0, 1f);
		sphere2.setX(OBJLoader.getOBJLength().getX());
		sphere2.setY(OBJLoader.getOBJLength().getY());
		sphere2.setZ(OBJLoader.getOBJLength().getZ());
		entitys.add(sphere2);

		PhysicsObject obj3 = new PhysicsObject(
				new Sphere(sphere.getPosition(), sphere.getY() / 2, Layers.Physics_Layer),
				new Vector3f(0.0f, -1.0f, 0.0f), 100);
		PhysicsObject obj4 = new PhysicsObject(
				new Sphere(sphere2.getPosition(), sphere2.getY() / 2, Layers.Physics_Layer),
				new Vector3f(0.0f, 0.0f, 0.0f), 10);
		physicsEngine.AddObject(obj3);
		physicsEngine.AddObject(obj4);

		Entity sphere4 = new Entity(new TexturedModel(OBJLoader.loadObjModel("earth", loader),
				new ModelTexture(loader.loadTexture("earth"))), b1.getMinExtents(), 0, 0, 0, 0.001f);
		sphere2.setX(OBJLoader.getOBJLength().getX());
		sphere2.setY(OBJLoader.getOBJLength().getY());
		sphere2.setZ(OBJLoader.getOBJLength().getZ());
		entitys.add(sphere4);

		Entity sphere5 = new Entity(new TexturedModel(OBJLoader.loadObjModel("earth", loader),
				new ModelTexture(loader.loadTexture("earth"))), b1.getMaxExtents(), 0, 0, 0, 0.001f);
		sphere2.setX(OBJLoader.getOBJLength().getX());
		sphere2.setY(OBJLoader.getOBJLength().getY());
		sphere2.setZ(OBJLoader.getOBJLength().getZ());
		entitys.add(sphere5);

		Entity sphere6 = new Entity(new TexturedModel(OBJLoader.loadObjModel("earth", loader),
				new ModelTexture(loader.loadTexture("earth"))), b2.getMinExtents(), 0, 0, 0, 0.001f);
		sphere2.setX(OBJLoader.getOBJLength().getX());
		sphere2.setY(OBJLoader.getOBJLength().getY());
		sphere2.setZ(OBJLoader.getOBJLength().getZ());
		entitys.add(sphere6);

		Entity sphere7 = new Entity(new TexturedModel(OBJLoader.loadObjModel("earth", loader),
				new ModelTexture(loader.loadTexture("earth"))), b2.getMaxExtents(), 0, 0, 0, 0.001f);
		sphere2.setX(OBJLoader.getOBJLength().getX());
		sphere2.setY(OBJLoader.getOBJLength().getY());
		sphere2.setZ(OBJLoader.getOBJLength().getZ());
		entitys.add(sphere7);

		camera = new Camera(null);

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

		physicsEngine.Simulate(Window.getDeltaTime());
		entitys.get(0).setPosition(physicsEngine.getObject(0).getPosition());
		entitys.get(1).setPosition(physicsEngine.getObject(1).getPosition());
		entitys.get(2).setPosition(physicsEngine.getObject(2).getPosition());
		entitys.get(3).setPosition(physicsEngine.getObject(3).getPosition());
		physicsEngine.HandleCollisions();
		b1.setRotation(new Vector3f(entitys.get(0).getRotX(), entitys.get(0).getRotY(), entitys.get(0).getRotZ()));
		b2.setRotation(new Vector3f(entitys.get(1).getRotX(), entitys.get(1).getRotY(), entitys.get(1).getRotZ()));
		Mouseinput.resetMouse();
		Keyinput.resetKeyboard();
	}

	@Override
	public void onclose() {
		renderer.cleanUp();
		loader.cleanUp();
	}

}
