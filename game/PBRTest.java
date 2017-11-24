package game;

import java.util.ArrayList;
import java.util.List;

import PBR.Material;
import PBR.PBREntity;
import PBR.PBRRebderer;
import Physics.PhysicsEngine;
import entities.Camera;
import entities.Entity;
import entities.Light;
import maths.Vector3f;
import maths.Vector4f;
import objConverter.OBJFileLoader;
import renderer.Loader;
import renderer.MasterRenderer;
import terrains.Terrain;
import texture.TerrainTexture;
import texture.TerrainTexturePack;

public class PBRTest implements GameLogic {
	Loader loader = new Loader();
	PhysicsEngine physicsEngine = new PhysicsEngine();

	List<Light> lights = new ArrayList<>();
	List<Terrain> terrains = new ArrayList<>();
	List<Entity> entitys = new ArrayList<>();
	List<Entity> normalMapentitys = new ArrayList<>();
	Camera camera;

	MasterRenderer renderer;
	
	PBRRebderer pbrebderer;
	PBREntity pbrentity;

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
		
		camera = new Camera(null);
		
		Light light = new Light(new Vector3f(1000000, 150000, -100000), new Vector3f(2f, 2f, 2f));
		lights.add(light);
		Light light2 = new Light(new Vector3f(21, 0, -40), new Vector3f(10, 0, 0), new Vector3f(1f, 0.1f, 0.002f));
		lights.add(light2);
		Light light3 = new Light(new Vector3f(50, 0, -30), new Vector3f(0, 10, 0), new Vector3f(1f, 0.1f, 0.002f));
		lights.add(light3);
		Light light4 = new Light(new Vector3f(39, 0, -45), new Vector3f(0, 0, 10), new Vector3f(1f, 0.1f, 0.002f));
		lights.add(light4);
		
		renderer = new MasterRenderer(loader, camera);
		
		pbrebderer=new PBRRebderer(renderer.getProjectionMatrix());
		Material material=new Material();
		material.setAlbedoMap(loader.loadTexture("rustediron2_basecolor"));
		material.setMetallicMap(loader.loadTexture("rustediron2_metallic"));
		material.setRoughnessMap(loader.loadTexture("rustediron2_roughness"));
		material.setNormalMap(loader.loadTexture("rustediron2_normal"));
		material.setAlbedo(new Vector3f(0.8f, 0.498039f, 0.196078f));
		pbrentity=new PBREntity(OBJFileLoader.loadOBJ("ball", loader), new Vector3f(30, 0, -30), new Vector3f(), new Vector3f(2, 2, 2), material);
		

	}

	@Override
	public void fixedupdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		camera.FirstPerson();

		renderer.renderScene(entitys, normalMapentitys, terrains, lights, camera, new Vector4f(0, -1, 0, 1000));
		pbrebderer.render(pbrentity, lights, camera);

	}

	@Override
	public void onclose() {
		renderer.cleanUp();
		loader.cleanUp();
		pbrebderer.cleanUp();

	}

}
