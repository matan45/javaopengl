package game;

import static org.lwjgl.opengl.GL11.glClearColor;

import org.lwjgl.opengl.GL11;

import aduio.AudioMaster;
import aduio.Source;
import entities.Camera;
import entities.Entity;
import maths.Vector3f;
import shader.StaticShader;
import texture.Texture;
import vbo.Loader;
import vbo.RawModel;
import vbo.Renderer;

public class Game implements GameLogic {

	Loader loader;
	Renderer renderer;
	RawModel model;
	StaticShader shader;
	Entity entity;
	Camera camera;

	int buffer;
	Source source;
	float x = 0;

	Texture tex;

	float[] vertices = { -0.5f, 0.5f, 0, -0.5f, -0.5f, 0, 0.5f, -0.5f, 0, 0.5f, 0.5f, 0,

			-0.5f, 0.5f, 1, -0.5f, -0.5f, 1, 0.5f, -0.5f, 1, 0.5f, 0.5f, 1,

			0.5f, 0.5f, 0, 0.5f, -0.5f, 0, 0.5f, -0.5f, 1, 0.5f, 0.5f, 1,

			-0.5f, 0.5f, 0, -0.5f, -0.5f, 0, -0.5f, -0.5f, 1, -0.5f, 0.5f, 1,

			-0.5f, 0.5f, 1, -0.5f, 0.5f, 0, 0.5f, 0.5f, 0, 0.5f, 0.5f, 1,

			-0.5f, -0.5f, 1, -0.5f, -0.5f, 0, 0.5f, -0.5f, 0, 0.5f, -0.5f, 1

	};

	int[] indices = { 0, 1, 3, 3, 1, 2, 4, 5, 7, 7, 5, 6, 8, 9, 11, 11, 9, 10, 12, 13, 15, 15, 13, 14, 16, 17, 19, 19,
			17, 18, 20, 21, 23, 23, 21, 22

	};

	float[] textureCoords = {

			0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1,
			1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0

	};

	public void preupdate() {
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		buffer = AudioMaster.loadSound("cartoon001.wav");
		source = new Source(1, 3, 20);
		source.play(buffer);

		shader = new StaticShader("test.vs", "test.frag");
		loader = new Loader();
		renderer = new Renderer(shader);
		model = loader.loadToVao(vertices, textureCoords, indices);
		tex = new Texture("photos");
		entity = new Entity(tex, new Vector3f(0, 0, -1), 0, 0, 0, 1, model);
		camera = new Camera();

	}

	public void update() {
		shader.start();
		camera.move();
		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		tex.bind();
		entity.increaseRotation(1, 1, 0);
		entity.increasePosition(0, 0, -0.001f);
		renderer.render(entity, shader);
		shader.loadViewnMatrix(camera);
		x -= 0.02f;
		source.setPosition(x, 0, 2);

		shader.stop();

	}

	public void onclose() {
		shader.cleanUp();
		loader.cleanUp();
		source.delete();
		AudioMaster.cleanUp();
		tex.cleanUp();

	}

}
