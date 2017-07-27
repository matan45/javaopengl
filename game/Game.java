package game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import aduio.AudioMaster;
import aduio.Source;
import entities.AABB;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontRendering.TextMaster;
import guis.GuiHandler;
import guis.GuiRenderer;
import guis.button.Button;
import guis.button.IButton;
import input.Keyinput;
import input.Mouseinput;
import maths.Vector2f;
import maths.Vector3f;
import renderer.Loader;
import renderer.MasterRenderer;
import renderer.OBJLoader;
import terrains.Terrain;
import text.FontType;
import text.GUIText;
import texture.ModelTexture;
import texture.TerrainTexture;
import texture.TerrainTexturePack;
import texture.TexturedModel;
import utill.MousePicker;
import utill.Time;

public class Game implements GameLogic {

	Loader loader = new Loader();

	MasterRenderer renderer;
	GuiRenderer guiRenderer;

	List<Entity> entitys = new ArrayList<>();
	List<Light> lights = new ArrayList<>();
	GuiHandler guis = new GuiHandler();

	Camera camera;
	Terrain terrain;
	MousePicker picker;

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

		renderer = new MasterRenderer(loader);
		guiRenderer = new GuiRenderer(loader);

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");

		// up to 4 lights
		Light light = new Light(new Vector3f(0, 10000, -7000), new Vector3f(0.5f, 0.5f, 0.5f));
		lights.add(light);
		Light light2 = new Light(new Vector3f(21, 0, -40), new Vector3f(10, 0, 0), new Vector3f(1f, 0.1f, 0.002f));
		lights.add(light2);
		Light light3 = new Light(new Vector3f(50, 0, -30), new Vector3f(0, 10, 0), new Vector3f(1f, 0.1f, 0.002f));
		lights.add(light3);
		Light light4 = new Light(new Vector3f(39, 0, -45), new Vector3f(0, 0, 10), new Vector3f(1f, 0.1f, 0.002f));
		lights.add(light4);

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
				new Vector3f(33, 0, -38), 0, 0, 0, 1.7f);
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

		Entity lamp = new Entity(
				new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp"))),
				new Vector3f(21, 0, -40), 0, 0, 0, 0.4f);
		lamp.getModel().getTexture().setReflectivity(1f);
		lamp.getModel().getTexture().setShineDamper(5f);
		lamp.addLight(light);
		entitys.add(lamp);

		Entity lamp2 = new Entity(
				new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp"))),
				new Vector3f(50, 0, -30), 0, 0, 0, 0.4f);
		lamp2.getModel().getTexture().setReflectivity(1f);
		lamp2.getModel().getTexture().setShineDamper(5f);
		entitys.add(lamp2);

		Entity lamp3 = new Entity(
				new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp"))),
				new Vector3f(35, 0, -52), 0, 0, 0, 0.4f);
		lamp3.getModel().getTexture().setReflectivity(1f);
		lamp3.getModel().getTexture().setShineDamper(5f);
		entitys.add(lamp3);

		Entity player = new Player(
				new TexturedModel(OBJLoader.loadObjModel("player", loader),
						new ModelTexture(loader.loadTexture("playerTexture"))),
				new Vector3f(30, 0, -20), 0, 0, 0, 0.3f);
		entitys.add(player);

		for (Entity entity : entitys) {
			float y = terrain.getHeightOfTerrain(entity.getPosition().x, entity.getPosition().z);
			entity.setPosition(new Vector3f(entity.getPosition().x, y, entity.getPosition().z));
		}
		// need here for y position
		lamp.addLight(light2);
		lamp2.addLight(light3);
		lamp3.addLight(light4);

		camera = new Camera((Player) player);

		Button gui = new Button(loader.loadTexture("Mortal komba"), new Vector2f(0.9f, 0.9f), new Vector2f(0.1f, 0.1f),
				new Vector2f(0, 0));
		gui.setTransparent(true);
		gui.setB(new IButton() {
			@Override
			public void onClick() {
				guis.removegui(0);
			}

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
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadTexture("font"), new File("src/resources/fonts/font.fnt"));
		GUIText text = new GUIText(1.5f, font, new Vector2f(-0.05f, 0f), 0.3f, true);
		text.setColour(1f, 1f, 1f);
		Time.setText(text);

		picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

	}

	public void update() {
		Time.fps();

		renderer.render(lights, camera);
		camera.Person3D();

		renderer.processTerrain(terrain);
		picker.update();

		for (Entity entity : entitys)
			renderer.processEntity(entity);

		Vector3f terrainPoint = picker.getCurrentTerrainPoint();
		if (terrainPoint != null && Mouseinput.mouseButtonDoubleClicked(GLFW.GLFW_MOUSE_BUTTON_1)) {
			entitys.get(10).setPosition(terrainPoint);
			entitys.get(10).addLight(lights.get(3));
		}

		guiRenderer.render(guis.getGuis());
		TextMaster.render();

		guis.update();

		entitys.get(2).increaseRotation(0, 1, 0);
		entitys.get(3).increaseRotation(0, -1, 0);
		((Player) entitys.get(11)).move(terrain);

		if (AABB.collides(entitys.get(11), entitys.get(0))) {
			if (!Dragon.isPlaying()) {
				Dragon.setPosition(entitys.get(0).getPosition());
				Dragon.play(buffer);
			}
		}

		Mouseinput.resetMouse();
		Keyinput.resetKeyboard();

	}

	public void onclose() {
		TextMaster.cleanUp();
		renderer.cleanUp();
		guiRenderer.cleanUp();
		loader.cleanUp();
		Dragon.delete();
		jungle.delete();
		AudioMaster.cleanUp();

	}

}
