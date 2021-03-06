package postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Fxaa {
	private ImageRenderer renderer;
	FxaaShader shader;
	
	public Fxaa(int width, int height) {
		shader=new FxaaShader("contrast.vs", "fxaa.frag");
		shader.start();
		shader.connectTextureUnits();
		shader.loadResolution(width, height);
		shader.stop();
		renderer = new ImageRenderer(width,height);
	}
	
	public void render(int texture){
		shader.start();
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
