package game;

import java.util.ArrayList;
import java.util.List;

import entities.Camera;
import entities.Entity;
import entities.Light;
import flare.lensFlare.FlareManager;
import flare.lensFlare.FlareTexture;
import flare.sunRenderer.Sun;
import flare.sunRenderer.SunRenderer;
import flare.textures.Texture;
import maths.Vector3f;
import maths.Vector4f;
import renderer.Loader;
import renderer.MasterRenderer;
import terrains.Terrain;
import texture.TerrainTexture;
import texture.TerrainTexturePack;

public class LensFlareTest implements GameLogic {
	List<Light> lights = new ArrayList<>();
	List<Terrain> terrains = new ArrayList<>();
	List<Entity> entitys = new ArrayList<>();
	List<Entity> normalMapentitys = new ArrayList<>();
	Loader loader = new Loader();
	Camera camera;
	FlareManager lensFlare;
	Sun theSun;
	MasterRenderer renderer;
	SunRenderer sunRenderer;

	@Override
	public void preupdate() {
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
		Vector3f lightDir = new Vector3f(0.55f, -0.34f, 1);
		theSun = new Sun(sun, 55);

		theSun.setDirection(lightDir.x, lightDir.y, lightDir.z);

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

		camera = new Camera(null);
		renderer = new MasterRenderer(loader, camera);
		sunRenderer = new SunRenderer();

	}

	@Override
	public void fixedupdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		camera.FirstPerson();
		lensFlare.render(camera, renderer.getProjectionMatrix(),
				theSun.getWorldPosition(camera.getPosition()));//have to be 2 time in ordered to work
		renderer.renderScene(entitys, normalMapentitys, terrains, lights, camera, new Vector4f(0, -1, 0, 1000));
		sunRenderer.render(theSun, camera.getPosition(), camera,
				renderer.getProjectionMatrix());
		lensFlare.render(camera, renderer.getProjectionMatrix(),
				theSun.getWorldPosition(camera.getPosition()));
		

	}

	@Override
	public void onclose() {
		renderer.cleanUp();
		loader.cleanUp();
		lensFlare.cleanUp();
		sunRenderer.cleanUp();

	}

}
