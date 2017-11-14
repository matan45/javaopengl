package game;

import java.util.ArrayList;
import java.util.List;

import Animation.anim.AnimGameItem;
import Animation.anim.AnimatedEntity;
import Animation.graph.Renderer;
import loaders.assimp.AnimMeshesLoader;
import entities.Camera;
import entities.Entity;
import entities.Light;
import maths.Vector3f;
import maths.Vector4f;
import renderer.Loader;
import renderer.MasterRenderer;
import terrains.Terrain;
import texture.TerrainTexture;
import texture.TerrainTexturePack;

public class AnimationTest implements GameLogic {
	List<Light> lights = new ArrayList<>();
	List<Terrain> terrains = new ArrayList<>();
	List<Entity> entitys = new ArrayList<>();
	List<Entity> normalMapentitys = new ArrayList<>();
	Loader loader = new Loader();
	Camera camera;
	Renderer re;
	AnimatedEntity enim;

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
		
		camera = new Camera(null);
		Light light = new Light(new Vector3f(1000000, 150000, -100000), new Vector3f(2f, 2f, 2f));
		lights.add(light);
		
		renderer = new MasterRenderer(loader, camera);
		
		re=new Renderer(renderer.getProjectionMatrix());
		
		AnimGameItem aim=AnimMeshesLoader.loadAnimGameItem("src/resources/Animation/Running.dae","src/resources/Animation/Ganfaul_diffuse.png");
		enim=new AnimatedEntity(new Vector3f(10,-4,-10), new Vector3f(5f,5f,5f), new Vector3f(180,0,180), aim);
		enim.setX(AnimMeshesLoader.getOBJLength().x);
		enim.setY(AnimMeshesLoader.getOBJLength().y);
		enim.setZ(AnimMeshesLoader.getOBJLength().z);
		System.out.println(enim.getX()+" "+enim.getY()+" "+enim.getZ()+" ");

	}

	@Override
	public void fixedupdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		camera.FirstPerson();
		renderer.renderScene(entitys, normalMapentitys, terrains, lights, camera, new Vector4f(0, -1, 0, 1000));
		re.renderScene(camera, enim,lights.get(0).getColour());
		enim.getAim().getCurrentAnimation().nextFrame();

	}

	@Override
	public void onclose() {
		renderer.cleanUp();
		loader.cleanUp();
		re.close();
	}

}
