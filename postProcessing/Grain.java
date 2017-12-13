package postProcessing;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Grain {
	private ImageRenderer renderer;
	GrainShader shader;
	public Grain(int width, int height) {
		shader=new GrainShader("contrast.vs", "Filmgrain.frag");
		shader.start();
		shader.connectTextureUnits();
		shader.strength(25);
		shader.stop();
		renderer = new ImageRenderer(width,height);
	}
	
	public void render(int texture){
		shader.start();
		shader.time((float)GLFW.glfwGetTime());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public void cleanUp() {
		renderer.cleanUp();
		shader.cleanUp();
	}
	public int getOutputTexture(){
		return renderer.getOutputTexture();
	}
}
