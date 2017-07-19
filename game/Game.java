package game;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import aduio.AudioMaster;
import aduio.Source;
import entities.Camera;
import entities.Entity;
import entities.Light;
import input.Mouseinput;
import maths.Vector3f;
import shader.StaticShader;
import texture.LoadTexture;
import texture.ModelTexture;
import texture.TexturedModel;
import vbo.Loader;
import vbo.MasterRenderer;
import vbo.OBJLoader;

public class Game implements GameLogic {

	Loader loader = new Loader();

	MasterRenderer renderer;
	StaticShader shader;
	
	Entity entity;
	Camera camera;
	Light light;
	LoadTexture tex;

	int buffer;
	Source source;
	float x = 0;

	  

	public void preupdate() {
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		buffer = AudioMaster.loadSound("cartoon001.wav");
		source = new Source(1, 3, 20);
		source.play(buffer);
		source.setLooping(true);
		
		shader = new StaticShader("test.vs", "test.frag");
		renderer = new MasterRenderer(shader);
		
		tex=new LoadTexture("stallTexture");
		ModelTexture mtex=new ModelTexture(tex.getId());
		mtex.setReflectivity(2);
		mtex.setShineDamper(10);
		TexturedModel tmodel= new TexturedModel(OBJLoader.loadObjModel("dragon", loader), mtex);
		
		entity = new Entity(tmodel, new Vector3f(0, 0, -10), 0, 0, 0, 1);
		camera = new Camera();
		light=new Light(new Vector3f(0,0,-5),new Vector3f(0,0,1));

	}

	public void update() {
		shader.start();
		camera.move();

		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_DEPTH_TEST);

		renderer.processEntity(entity);
		entity.increaseRotation(1, 1, 0);
		entity.increasePosition(0, 0, -0.001f);
		renderer.render(light, camera);

		x -= 0.02f;
		source.setPosition(x, 0, 2);

		shader.stop();
		Mouseinput.resetMouse();
	}

	public void onclose() {
		renderer.cleanUp();
		shader.cleanUp();
		loader.cleanUp();
		source.delete();
		AudioMaster.cleanUp();
		tex.cleanUp();

	}

}
