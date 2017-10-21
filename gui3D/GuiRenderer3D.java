package gui3D;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entities.Camera;
import maths.Maths;
import maths.Matrix4f;
import renderer.Loader;
import renderer.RawModel;

public class GuiRenderer3D {
	private final RawModel quad;
	private GuiShader3D shader;

	public GuiRenderer3D(Loader loader) {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = loader.loadToVAO(positions, 2);
		shader = new GuiShader3D();
	}

	public void render(List<GuiTexture3D> guis,Matrix4f ProjectionMatrix,Camera camera) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);

		for (GuiTexture3D gui : guis) {
			if (gui.isTransparent())
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			else
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale(), gui.getRotation());
			shader.loadTransformation(matrix);
			shader.loadPorjectionMatrix(ProjectionMatrix);
			shader.loadViewMatrix(camera);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}
	
}
