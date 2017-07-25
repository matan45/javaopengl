package game;

import java.util.ArrayList;
import java.util.List;

import aduio.AudioMaster;
import aduio.Source;
import entities.AABB;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiHandler;
import guis.GuiRenderer;
import guis.button.Button;
import guis.button.IButton;
import input.Mouseinput;
import maths.Vector2f;
import maths.Vector3f;
import renderer.Loader;
import renderer.MasterRenderer;
import renderer.OBJLoader;
import terrains.Terrain;
import texture.ModelTexture;
import texture.TerrainTexture;
import texture.TerrainTexturePack;
import texture.TexturedModel;
import utill.Time;

public class Game implements GameLogic {

	Loader loader = new Loader();

	MasterRenderer renderer;
	GuiRenderer guiRenderer;

	List<Entity> entitys = new ArrayList<>();
	GuiHandler guis = new GuiHandler();

	Camera camera;
	Light light;

	Terrain terrain;

	int buffer;
	Source jungle, Dragon;

	public void preupdate() {
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		buffer = AudioMaster.loadSound("Forest Birds");
		jungle = new Source(0, 0, 0);
		jungle.play(buffer);
		jungle.setVolume(0.3f);
		jungle.setLooping(true);

		buffer = AudioMaster.loadSound("Dragon Roaring");
		Dragon = new Source(0, 0, 0);

		renderer = new MasterRenderer();
		guiRenderer = new GuiRenderer(loader);

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");

		Entity dragon = new Entity(new TexturedModel(OBJLoader.loadObjModel("dragon", loader),
				new ModelTexture(loader.loadTexture("dragon"))), new Vector3f(30, 0, -35), 0, 0, 0, 0.3f);
		dragon.getModel().getTexture().setReflectivity(2);
		dragon.getModel().getTexture().setShineDamper(10);
		entitys.add(dragon);

		Entity grass = new Entity(new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture"))), new Vector3f(32, 0, -37), 0, 0, 0, 1f);
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
				new Vector3f(33, 0, -38), 0, 0, 0, 1.5f);
		tree.getModel().getTexture().setReflectivity(5);
		tree.getModel().getTexture().setShineDamper(20);
		entitys.add(tree);

		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		TexturedModel Mfern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernTextureAtlas);

		Entity fern = new Entity(Mfern, 0, new Vector3f(25, 0, -35), 0, 0, 0, 0.45f);
		fern.getModel().getTexture().setHasTransparency(true);
		fern.getModel().getTexture().setUseFakeLighting(true);
		entitys.add(fern);

		Entity fern2 = new Entity(Mfern, 1, new Vector3f(30, 0, -45), 0, 0, 0, 0.45f);
		fern.getModel().getTexture().setHasTransparency(true);
		fern.getModel().getTexture().setUseFakeLighting(true);
		entitys.add(fern2);

		Entity fern3 = new Entity(Mfern, 2, new Vector3f(35, 0, -50), 0, 0, 0, 0.45f);
		fern.getModel().getTexture().setHasTransparency(true);
		fern.getModel().getTexture().setUseFakeLighting(true);
		entitys.add(fern3);

		Entity fern4 = new Entity(Mfern, 3, new Vector3f(20, 0, -40), 0, 0, 0, 0.45f);
		fern.getModel().getTexture().setHasTransparency(true);
		fern.getModel().getTexture().setUseFakeLighting(true);
		entitys.add(fern4);

		Entity player = new Player(
				new TexturedModel(OBJLoader.loadObjModel("player", loader),
						new ModelTexture(loader.loadTexture("playerTexture"))),
				new Vector3f(30, 0, -20), 0, 0, 0, 0.3f);
		entitys.add(player);

		for (Entity entity : entitys) {
			float y = terrain.getHeightOfTerrain(entity.getPosition().x, entity.getPosition().z);
			entity.setPosition(new Vector3f(entity.getPosition().x, y, entity.getPosition().z));
		}

		camera = new Camera((Player) player);
		light = new Light(new Vector3f(40, 60, -60), new Vector3f(1, 1, 1));

		Button gui = new Button(loader.loadTexture("Mortal komba"), new Vector2f(0.9f, 0.9f), new Vector2f(0.1f, 0.1f),
				new Vector2f(0, 0));
		gui.setTransparent(true);
		gui.setB(new IButton() {
			@Override
			public void onClick() {}
			@Override
			public void onHover() {
				gui.setPosition(new Vector2f(0.8f, 0.8f));
				gui.setScale(new Vector2f(0.2f, 0.2f));
				gui.increaseRotation(new Vector2f(1, 0));
			}
			@Override
			public void stopHover() {
				gui.setPosition(new Vector2f(0.9f, 0.9f));
				gui.setScale(new Vector2f(0.1f, 0.1f));
				gui.setRotation(new Vector2f(0, 0));
			}
		});
		guis.addgui(gui);
		

	}

	public void update() {
		Time.fps();

		renderer.render(light, camera);
		camera.Person3D();
		guis.update();

		renderer.processTerrain(terrain);

		for (Entity entity : entitys)
			renderer.processEntity(entity);
		guiRenderer.render(guis.getGuis());

		entitys.get(2).increaseRotation(0, 1, 0);
		entitys.get(3).increaseRotation(0, -1, 0);
		((Player) entitys.get(8)).move(terrain);

		if (AABB.collides(entitys.get(8), entitys.get(0))) {
			if (!Dragon.isPlaying()) {
				Dragon.setPitch(10f);
				Dragon.play(buffer);
			}
		}

		Mouseinput.resetMouse();
	}

	public void onclose() {
		renderer.cleanUp();
		guiRenderer.cleanUp();
		loader.cleanUp();
		Dragon.delete();
		jungle.delete();
		AudioMaster.cleanUp();

	}

}
