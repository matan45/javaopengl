package game;

import static org.lwjgl.opengl.GL11.glClearColor;

import org.lwjgl.opengl.GL11;

import aduio.AudioMaster;
import aduio.Source;
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
	
	int buffer;
	Source source;
	float x = 0;
	
	Texture tex;

	float[] vertices = { 
			-0.5f, 0.5f, 0f, // V0
			-0.5f, -0.5f, 0f, // V1
			0.5f, -0.5f, 0f, // V2
			0.5f, 0.5f, 0f, // V4

	};
	int[] indices = { 
			0, 1, 3, // Top left triangle (V0,V1,V3)
			3, 1, 2 // Bottom right triangle (V3,V1,V2)
	};
	float[] textureCoords={
			0,0,
			0,1,
			1,1,
			1,0
	};
	public void preupdate() {
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		buffer = AudioMaster.loadSound("cartoon001.wav");
		source = new Source(1, 3, 20);
		source.play(buffer);
		
		shader = new StaticShader("tt.vs", "tt.frag");
		loader = new Loader();
		renderer = new Renderer();
		model = loader.loadToVao(vertices,textureCoords, indices);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		tex=new Texture("photos");



	}

	public void update() {
		shader.start();
		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		tex.bind();
		renderer.render(model,tex.getId());

		x -= 0.02f;
		source.setPosition(x, 0, 2);

		shader.stop();

	}

	public void onclose() {
		shader.cleanUp();
		loader.cleanUp();
		source.delete();
		AudioMaster.cleanUp();

	}

}
