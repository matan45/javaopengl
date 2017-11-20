package game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import Animated2D.Image2D;
import Animation.anim.AnimGameItem;
import Animation.anim.AnimatedEntity;
import Animation.graph.AnimationRenderer;
import aduio.AudioMaster;
import aduio.PlayList;
import aduio.Source;
import entities.AABB;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import flare.lensFlare.FlareManager;
import flare.lensFlare.FlareTexture;
import flare.sunRenderer.Sun;
import flare.sunRenderer.SunRenderer;
import flare.textures.Texture;
import fontRendering.TextMaster;
import gui3D.GuiRenderer3D;
import gui3D.GuiTexture3D;
import guis.GuiHandler;
import guis.GuiRenderer;
import guis.button.Button;
import guis.button.IButton;
import guis.windowgui.IWindowgui;
import guis.windowgui.WindowGui;
import input.Keyinput;
import input.Mouseinput;
import loaders.assimp.AnimMeshesLoader;
import maths.Vector2f;
import maths.Vector3f;
import maths.Vector4f;
import normalMappingObjConverter.NormalMappedObjLoader;
import objConverter.OBJFileLoader;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
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
import window.SceneManager;
import window.WindowManager;

public class Game implements GameLogic {

	Loader loader = new Loader();

	MasterRenderer renderer;
	GuiRenderer guiRenderer;
	GuiRenderer3D guiRenderer3d;

	WaterRenderer waterRenderer;
	WaterShader waterShader;
	WaterFrameBuffers buffers;

	AnimationRenderer animationRenderer;
	AnimatedEntity animatedEntity;

	FlareManager lensFlare;
	Sun theSun;
	SunRenderer sunRenderer;

	List<Entity> entitys = new ArrayList<>();
	List<Entity> normalMapentitys = new ArrayList<>();
	List<Light> lights = new ArrayList<>();
	List<Terrain> terrains = new ArrayList<>();
	List<WaterTile> waters = new ArrayList<>();
	List<Source> audios = new ArrayList<>();
	List<GuiTexture3D> gui3d = new ArrayList<>();

	PlayList s = PlayList.getInstance();

	Camera camera;

	Fbo multisampleFbo, outputFbo, outputFbo2;

	ParticleSystem particlesystem;

	MousePicker picker;

	Image2D im;

	public void preupdate() {
		// PlayList
		s.addsong("Cassiopea");
		s.addsong("Suns And Stars");
		s.addsong("Everdream");
		s.start();

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
		Light light = new Light(new Vector3f(1000000, 150000, -100000), new Vector3f(2f, 2f, 2f));
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
		lamp.setX(OBJLoader.getOBJLength().getX());
		lamp.setY(OBJLoader.getOBJLength().getY());
		lamp.setZ(OBJLoader.getOBJLength().getZ());
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

		Entity barrel = new Entity(new TexturedModel(OBJFileLoader.loadOBJ("barrel", loader),
				new ModelTexture(loader.loadTexture("barrel"))), new Vector3f(15, 0, -20), 0, 0, 0, 0.2f);
		barrel.getModel().getTexture().setShineDamper(10);
		barrel.getModel().getTexture().setReflectivity(0.5f);
		barrel.getModel().getTexture().setSpecularMap(loader.loadTexture("barrelS"));

		entitys.add(barrel);

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
		guiRenderer3d = new GuiRenderer3D(loader);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());

		// GUI
		Button button = new Button(loader.loadTexture("gui/Mortal komba"), new Vector2f(0.9f, 0.9f),
				new Vector2f(0.1f, 0.1f), new Vector2f(0, 0), true);
		button.setTransparent(true);
		button.setB(new IButton() {
			@Override
			public void onClick() {

			}

			@Override
			public void onHover() {
				button.setPosition(new Vector2f(0.8f, 0.8f));
				button.setScale(new Vector2f(0.2f, 0.2f));
				button.increaseRotation(new Vector2f(1, 0));
			}

			@Override
			public void stopHover() {
				button.setPosition(new Vector2f(0.9f, 0.9f));
				button.setScale(new Vector2f(0.1f, 0.1f));
				button.setRotation(new Vector2f(0, 0));
			}
		});
		// 3D GuiTexture
		GuiTexture3D test = new GuiTexture3D(loader.loadTexture("wolf"), new Vector3f(53, 10, -46),
				new Vector3f(10, 10, 0), new Vector3f());
		test.setBothsides(true);
		gui3d.add(test);

		WindowGui win = new WindowGui(loader.loadTexture("gui/windowgui"), new Vector2f(-0.7f, -0.7f),
				new Vector2f(0.3f, 0.3f), new Vector2f(0, 0), new Button(loader.loadTexture("gui/xxx"),
						new Vector2f(0.9f, 0.9f), new Vector2f(0.05f, 0.05f), new Vector2f(0, 0), false));
		win.setTransparent(false);
		win.setWindow(new IWindowgui() {

			@Override
			public void whileDrag() {

			}

			@Override
			public void onclose() {

			}
		});

		// TEXT
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadTexture("candara"), new File("src/resources/fonts/candara.fnt"));
		GUIText text = new GUIText(1.5f, font, new Vector2f(0.01f, 0f), 0.3f, false);
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
		normalMapentitys.add(new Entity(barrelModel, new Vector3f(20, 5, -20), 0, 0, 0, 50f));

		TexturedModel Zombie = new TexturedModel(NormalMappedObjLoader.loadOBJ("test", loader),
				new ModelTexture(loader.loadTexture("parasiteZombie_diffuse")));
		Zombie.getTexture().setNormalMap(loader.loadTexture("parasiteZombie_normal"));
		Entity zob = new Entity(Zombie, new Vector3f(20, 5, -15), 0, 0, 0, 2f);
		normalMapentitys.add(zob);

		for (Entity nentity : normalMapentitys) {
			float y = terrain.getHeightOfTerrain(nentity.getPosition().x, nentity.getPosition().z);
			nentity.setPosition(new Vector3f(nentity.getPosition().x, y, nentity.getPosition().z));
		}

		// PARTICLE EFFECTS
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("fire"), 8, true);
		particlesystem = new ParticleSystem(particleTexture, 50, 5, 0f, 1f, 10f);
		particlesystem.setDirection(new Vector3f(-1, -0.5f, 0), 0.05f);
		particlesystem.randomizeRotation();

		// POST PROCESSING
		multisampleFbo = new Fbo(WindowManager.getWindow("main").getWidth(),
				WindowManager.getWindow("main").getHeight());
		outputFbo = new Fbo(WindowManager.getWindow("main").getWidth(), WindowManager.getWindow("main").getHeight(),
				Fbo.DEPTH_TEXTURE);
		outputFbo2 = new Fbo(WindowManager.getWindow("main").getWidth(), WindowManager.getWindow("main").getHeight(),
				Fbo.DEPTH_TEXTURE);
		PostProcessing.init(loader);

		// 2D Image Animation
		im = new Image2D("src/resources/2DAnimation/", 5, guiRenderer, new Vector2f(0.8f, -0.8f),
				new Vector2f(0.2f, 0.2f), new Vector2f(), false);

		// 3D Model Animation
		animationRenderer = new AnimationRenderer(renderer.getProjectionMatrix());

		AnimGameItem aim = AnimMeshesLoader.loadAnimGameItem("src/resources/Animation/Running.dae",
				"src/resources/Animation/Ganfaul_diffuse.png");
		animatedEntity = new AnimatedEntity(new Vector3f(10, 0, -10), new Vector3f(2f, 2f, 2f),
				new Vector3f(180, 0, 180), aim);

		// loading textures for lens flare
		Texture texture1 = Texture.newTexture("src/resources/lensFlare/tex1.png").normalMipMap().create();
		Texture texture2 = Texture.newTexture("src/resources/lensFlare/tex2.png").normalMipMap().create();
		Texture texture3 = Texture.newTexture("src/resources/lensFlare/tex3.png").normalMipMap().create();
		Texture texture4 = Texture.newTexture("src/resources/lensFlare/tex4.png").normalMipMap().create();
		Texture texture5 = Texture.newTexture("src/resources/lensFlare/tex5.png").normalMipMap().create();
		Texture texture6 = Texture.newTexture("src/resources/lensFlare/tex6.png").normalMipMap().create();
		Texture texture7 = Texture.newTexture("src/resources/lensFlare/tex7.png").normalMipMap().create();
		Texture texture8 = Texture.newTexture("src/resources/lensFlare/tex8.png").normalMipMap().create();
		Texture texture9 = Texture.newTexture("src/resources/lensFlare/tex9.png").normalMipMap().create();
		Texture sun = Texture.newTexture("src/resources/lensFlare/sun.png").normalMipMap().create();
		// set up lens flare
		lensFlare = new FlareManager(0.16f, new FlareTexture(texture6, 0.5f), new FlareTexture(texture4, 0.23f),
				new FlareTexture(texture2, 0.1f), new FlareTexture(texture7, 0.05f), new FlareTexture(texture1, 0.02f),
				new FlareTexture(texture3, 0.06f), new FlareTexture(texture9, 0.12f), new FlareTexture(texture5, 0.07f),
				new FlareTexture(texture1, 0.012f), new FlareTexture(texture7, 0.2f), new FlareTexture(texture9, 0.1f),
				new FlareTexture(texture3, 0.07f), new FlareTexture(texture5, 0.3f), new FlareTexture(texture4, 0.4f),
				new FlareTexture(texture8, 0.6f));

		// init sun and set sun direction
		sunRenderer = new SunRenderer();
		Vector3f lightDir = new Vector3f(0.55f, -0.34f, 1);
		theSun = new Sun(sun, 55);

		theSun.setDirection(lightDir.x, lightDir.y, lightDir.z);
	}

	public void update() {
		Time.fps();
		camera.Person3D();
		picker.update();

		renderer.renderShadowMap(entitys, lights.get(0));

		ParticleMaster.update(camera);

		watercode();
		multisampleFbo.bindFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		// need to be here else it will not render properly
		animationRenderer.renderScene(camera, animatedEntity, theSun.getLightDirection());
		animatedEntity.getAim().getCurrentAnimation().nextFrame();
		renderer.renderScene(entitys, normalMapentitys, terrains, lights, camera, new Vector4f(0, -1, 0, 1000));
		sunRenderer.render(theSun, camera.getPosition(), camera,
				renderer.getProjectionMatrix());
		guiRenderer3d.render(gui3d, renderer.getProjectionMatrix(), camera);
		waterRenderer.render(waters, camera, lights.get(0));
		ParticleMaster.renderParticles(camera);
		multisampleFbo.unbindFrameBuffer();
		multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, outputFbo);
		multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, outputFbo2);
		PostProcessing.doPostProcessing(outputFbo.getColourTexture(), outputFbo2.getColourTexture());
		
		lensFlare.render(camera, renderer.getProjectionMatrix(),
				theSun.getWorldPosition(camera.getPosition()));
		
		Vector3f terrainPoint = picker.getCurrentTerrainPoint();
		if (terrainPoint != null && Mouseinput.mouseButtonDoubleClicked(GLFW.GLFW_MOUSE_BUTTON_1)) {
			System.out.println(terrainPoint);
			if (AABB.insidepicker(entitys.get(0), terrainPoint))
				System.out.println("inside");

		}

		guiRenderer.render(GuiHandler.getGuis());
		im.start();
		GuiHandler.update();
		TextMaster.render();

		entitys.get(2).increaseRotation(0, 1, 0);
		entitys.get(3).increaseRotation(0, -1, 0);
		entitys.get(12).increaseRotation(0, 1, 0);
		normalMapentitys.get(0).increaseRotation(0, 1, 0);
		((Player) entitys.get(11)).move(terrains.get(0));
		((Player) entitys.get(11)).colliding(entitys.get(0));

		if (AABB.collides(entitys.get(11), entitys.get(0))) {
			if (!audios.get(1).isPlaying()) {
				audios.get(1).setPosition(entitys.get(0).getPosition());
				audios.get(1).play();
			}
			Vector3f temp = entitys.get(11).getPosition();
			particlesystem.generateParticles(new Vector3f(temp.x - 2, temp.y + 2, temp.z));
		}
		

	}

	public void onclose() {
		s.closePlayList();
		TextMaster.cleanUp();
		ParticleMaster.cleanup();
		multisampleFbo.cleanUp();
		outputFbo.cleanUp();
		outputFbo2.cleanUp();
		PostProcessing.cleanUp();
		buffers.cleanUp();
		waterShader.cleanUp();
		renderer.cleanUp();
		guiRenderer.cleanUp();
		guiRenderer3d.cleanUp();
		loader.cleanUp();
		AudioMaster.cleanUp();
		GuiHandler.cleanup();
		lensFlare.cleanUp();
		sunRenderer.cleanUp();
		animationRenderer.close();
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
		multisampleFbo.unbindFrameBuffer();
		camera.getPosition().y += distance;
		camera.invertPitch();
		buffers.bindRefractionFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		renderer.renderScene(entitys, normalMapentitys, terrains, lights, camera,
				new Vector4f(0, -1, 0, waters.get(0).getHeight()));
		buffers.unbindCurrentFrameBuffer();
	}

	@Override
	public void fixedupdate() {

		if (Keyinput.keyDown(GLFW.GLFW_KEY_L)) {
			SceneManager.changeScene(new PhysicsTest());
		}

	}

}
