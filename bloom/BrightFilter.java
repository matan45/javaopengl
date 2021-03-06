package bloom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import postProcessing.ImageRenderer;

public class BrightFilter {

	private ImageRenderer renderer;
	private BrightFilterShader shader;

	public BrightFilter(int width, int height) {
		shader = new BrightFilterShader("simple.vs", "brightFilter.frag");
		renderer = new ImageRenderer(width, height);
		shader.start();
		shader.loadshades(0);
		shader.scale(0);
		shader.stop();
	}

	public void render(int texture) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}

	public int getOutputTexture() {
		return renderer.getOutputTexture();
	}

	public void cleanUp() {
		renderer.cleanUp();
		shader.cleanUp();
	}

}
