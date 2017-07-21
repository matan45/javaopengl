package game;

import java.util.ArrayList;
import java.util.List;

import aduio.AudioMaster;
import aduio.Source;
import entities.Camera;
import entities.Entity;
import entities.Light;
import maths.Vector3f;
import terrains.Terrain;
import texture.ModelTexture;
import texture.TexturedModel;
import utill.FPS;
import vbo.Loader;
import vbo.MasterRenderer;
import vbo.OBJLoader;

public class Game implements GameLogic {

	Loader loader = new Loader();

	MasterRenderer renderer;

	List<Entity> entitys = new ArrayList<>();

	Camera camera;
	Light light;

	Terrain terrain;

	int buffer;
	Source jungle,horror;
	float x=0;

	public void preupdate() {
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		buffer = AudioMaster.loadSound("Forest Birds");
		jungle = new Source(0,0,0);
		jungle.play(buffer);
		jungle.setVolume(0.3f);
		jungle.setLooping(true);
		
		buffer = AudioMaster.loadSound("horror");
		horror=new Source(1,3, 20);
		horror.play(buffer);
		horror.setLooping(true);
		
		renderer = new MasterRenderer();
		
		Entity dragon = new Entity(new TexturedModel(OBJLoader.loadObjModel("dragon", loader),
				new ModelTexture(loader.loadTexture("dragon"))), new Vector3f(30, 0, -35), 0, 0, 0, 0.3f);
		dragon.getModel().getTexture().setReflectivity(2);
		dragon.getModel().getTexture().setShineDamper(10);
		entitys.add(dragon);

		Entity grass = new Entity(new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture"))), new Vector3f(32, 0, -37), 0, 0, 0, 1);
		grass.getModel().getTexture().setHasTransparency(true);
		grass.getModel().getTexture().setUseFakeLighting(true);
		entitys.add(grass);

		Entity lowtree = new Entity(new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader),
				new ModelTexture(loader.loadTexture("lowPolyTree"))), new Vector3f(31, 0, -42), 0, 0, 0, 0.2f);
		lowtree.getModel().getTexture().setHasTransparency(true);
		lowtree.getModel().getTexture().setUseFakeLighting(true);
		entitys.add(lowtree);

		Entity tree = new Entity(
				new TexturedModel(OBJLoader.loadObjModel("tree", loader), new ModelTexture(loader.loadTexture("tree"))),
				new Vector3f(33, 0, -38), 0, 0, 0, 1.7f);
		tree.getModel().getTexture().setReflectivity(5);
		tree.getModel().getTexture().setShineDamper(20);
		entitys.add(tree);

		Entity fern = new Entity(
				new TexturedModel(OBJLoader.loadObjModel("fern", loader), new ModelTexture(loader.loadTexture("fern"))),
				new Vector3f(28, 0, -40), 0, 0, 0, 0.45f);
		fern.getModel().getTexture().setHasTransparency(true);
		fern.getModel().getTexture().setUseFakeLighting(true);
		entitys.add(fern);

		terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));

		camera = new Camera();
		light = new Light(new Vector3f(40, 60, -60), new Vector3f(1, 1, 1));

	}

	public void update() {
		FPS.update();
		
		
		renderer.render(light, camera);
		camera.move();

		renderer.processTerrain(terrain);
		for (Entity entity : entitys)
			renderer.processEntity(entity);

		entitys.get(2).increaseRotation(0, 1, 0);
		entitys.get(3).increaseRotation(0, -1, 0);
		// entity.increasePosition(0, 0, -0.001f);

		x -= 0.02f;
		horror.setPosition(x, 0, 2);

	}

	public void onclose() {
		renderer.cleanUp();
		loader.cleanUp();
		horror.delete();
		jungle.delete();
		AudioMaster.cleanUp();

	}

}
