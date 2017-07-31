package game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

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
import guis.windowgui.IWindowgui;
import guis.windowgui.WindowGui;
import input.Keyinput;
import input.Mouseinput;
import maths.Vector2f;
import maths.Vector3f;
import maths.Vector4f;
import normalMappingObjConverter.NormalMappedObjLoader;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
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
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class Game implements GameLogic {

	Loader loader = new Loader();

	MasterRenderer renderer;
	GuiRenderer guiRenderer;

	WaterRenderer waterRenderer;
	WaterShader waterShader;
	WaterFrameBuffers buffers;

	List<Entity> entitys = new ArrayList<>();
	List<Entity> normalMapentitys = new ArrayList<>();
	List<Light> lights = new ArrayList<>();
	List<Terrain> terrains = new ArrayList<>();
	List<WaterTile> waters = new ArrayList<>();
	List<Source> audios = new ArrayList<>();
	GuiHandler guis = new GuiHandler();

	Camera camera;

	ParticleSystem particlesystem;

	MousePicker picker;

	public void preupdate() {
		// INIT OPENAL
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		Source jungle = new Source(0, 0, 0);
		jungle.setBuffer(AudioMaster.loadSound("Forest Birds"));
		jungle.play();
		jungle.setVolume(0.3f);
		jungle.setLooping(true);
		audios.add(jungle);

		Source Dragon = new Source(0, 0, 0);
		Dragon.setBuffer(AudioMaster.loadSound("Dragon Roaring"));
		audios.add(Dragon);

		// TERRAIN TEXTURE
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		// RANDOM HEIGHTS
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
		terrains.add(terrain);

		// UP TO 4 LIGHTS
		Light light = new Light(new Vector3f(1000000, 150000, -100000), new Vector3f(1f, 1f, 1f));
		lights.add(light);
		Light light2 = new Light(new Vector3f(21, 0, -40), new Vector3f(10, 0, 0), new Vector3f(1f, 0.1f, 0.002f));
		lights.add(light2);
		Light light3 = new Light(new Vector3f(50, 0, -30), new Vector3f(0, 10, 0), new Vector3f(1f, 0.1f, 0.002f));
		lights.add(light3);
		Light light4 = new Light(new Vector3f(39, 0, -45), new Vector3f(0, 0, 10), new Vector3f(1f, 0.1f, 0.002f));
		lights.add(light4);

		// INIT ENTITYS
		Entity dragon = new Entity(new TexturedModel(OBJLoader.loadObjModel("dragon", loader),
				new ModelTexture(loader.loadTexture("dragon"))), new Vector3f(30, 0, -35), 0, 0, 0, 0.3f);
		dragon.getModel().getTexture().setReflectivity(2);
		dragon.getModel().getTexture().setShineDamper(10);
		dragon.setX(OBJLoader.getOBJLength().getX());
		dragon.setY(OBJLoader.getOBJLength().getY());
		dragon.setZ(OBJLoader.getOBJLength().getZ());
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

		// TEXTURE ATLASES
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
		player.setX(OBJLoader.getOBJLength().getX());
		player.setY(OBJLoader.getOBJLength().getY());
		player.setZ(OBJLoader.getOBJLength().getZ());
		entitys.add(player);

		for (Entity entity : entitys) {
			float y = terrain.getHeightOfTerrain(entity.getPosition().x, entity.getPosition().z);
			entity.setPosition(new Vector3f(entity.getPosition().x, y, entity.getPosition().z));
		}
		// need here for y position
		lamp.addLight(light2);
		lamp2.addLight(light3);
		lamp3.addLight(light4);

		// 3D PLAYER CAMERA
		camera = new Camera((Player) player);

		// INIT RENDERER
		renderer = new MasterRenderer(loader, camera);
		guiRenderer = new GuiRenderer(loader);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());

		// GUI
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

		WindowGui win = new WindowGui(loader.loadTexture("windowgui"), new Vector2f(-0.7f, -0.7f),
				new Vector2f(0.3f, 0.3f), new Vector2f(0, 0));
		win.setTransparent(false);
		win.setWindow(new IWindowgui() {

			@Override
			public void whileDrag() {

			}
		});
		guis.addgui(win);

		// TEXT
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadTexture("candara"), new File("src/resources/fonts/candara.fnt"));
		GUIText text = new GUIText(1.5f, font, new Vector2f(-0.07f, 0f), 0.3f, true);
		text.setColour(1f, 1f, 1f);
		text.setOutlinecolour(1, 0, 0);
		Time.setText(text);

		// RAYCAST
		picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

		// WATER
		waterShader = new WaterShader("water.vs", "water.frag");
		buffers = new WaterFrameBuffers();
		waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
		waters.add(new WaterTile(75, -84, -4f));

		// NORMAL MAP
		TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("Oildrum", loader),
				new ModelTexture(loader.loadTexture("oildrum")));
		barrelModel.getTexture().setShineDamper(10);
		barrelModel.getTexture().setReflectivity(1f);
		barrelModel.getTexture().setNormalMap(loader.loadTexture("oildrum_normal"));
		normalMapentitys.add(new Entity(barrelModel, new Vector3f(20, 5, -20), 0, 0, 0, 2f));

		for (Entity nentity : normalMapentitys) {
			float y = terrain.getHeightOfTerrain(nentity.getPosition().x, nentity.getPosition().z);
			nentity.setPosition(new Vector3f(nentity.getPosition().x, y, nentity.getPosition().z));
		}

		// PARTICLE EFFECTS
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("fire"), 8, true);
		particlesystem = new ParticleSystem(particleTexture, 100, 10, 0.3f, 5f, 1.5f);
		particlesystem.setDirection(new Vector3f(0, 3, 0), 0.05f);
		particlesystem.randomizeRotation();

	}

	public void update() {
		Time.fps();
		camera.Person3D();
		picker.update();

		renderer.renderShadowMap(entitys, lights.get(0));

		ParticleMaster.update(camera);

		watercode();
		renderer.renderScene(entitys, normalMapentitys, terrains, lights, camera, new Vector4f(0, -1, 0, 1000));
		waterRenderer.render(waters, camera, lights.get(0));

		Vector3f terrainPoint = picker.getCurrentTerrainPoint();
		if (terrainPoint != null && Mouseinput.mouseButtonDoubleClicked(GLFW.GLFW_MOUSE_BUTTON_1)) {
			System.out.println(terrainPoint);
			if (AABB.insidepicker(entitys.get(0), terrainPoint))
				System.out.println("inside");

		}

		ParticleMaster.renderParticles(camera);

		guiRenderer.render(guis.getGuis());
		guis.update();
		TextMaster.render();

		entitys.get(2).increaseRotation(0, 1, 0);
		entitys.get(3).increaseRotation(0, -1, 0);
		normalMapentitys.get(0).increaseRotation(0, 1, 0);
		((Player) entitys.get(11)).move(terrains.get(0));

		if (AABB.collides(entitys.get(11), entitys.get(0))) {
			if (!audios.get(1).isPlaying()) {
				audios.get(1).setPosition(entitys.get(0).getPosition());
				audios.get(1).play();
			}
			particlesystem.generateParticles(new Vector3f(entitys.get(11).getPosition()));
		}

		Mouseinput.resetMouse();
		Keyinput.resetKeyboard();

	}

	public void onclose() {
		TextMaster.cleanUp();
		ParticleMaster.cleanup();
		buffers.cleanUp();
		waterShader.cleanUp();
		renderer.cleanUp();
		guiRenderer.cleanUp();
		loader.cleanUp();
		AudioMaster.cleanUp();
		for (Source s : audios)
			s.delete();

	}

	private void watercode() {
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		buffers.bindReflectionFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		float distance = 2 * (camera.getPosition().y - waters.get(0).getHeight());
		camera.getPosition().y -= distance;
		camera.invertPitch();
		renderer.renderScene(entitys, normalMapentitys, terrains, lights, camera,
				new Vector4f(0, -1, 0, -waters.get(0).getHeight() + 1));
		camera.getPosition().y += distance;
		camera.invertPitch();
		buffers.bindRefractionFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		renderer.renderScene(entitys, normalMapentitys, terrains, lights, camera,
				new Vector4f(0, -1, 0, waters.get(0).getHeight()));
		buffers.unbindCurrentFrameBuffer();
	}

}
