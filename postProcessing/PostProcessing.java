package postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import bloom.CombineFilter;
import gaussianBlur.HorizontalBlur;
import gaussianBlur.VerticalBlur;
import renderer.Loader;
import renderer.RawModel;
import window.WindowManager;

public class PostProcessing {

	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static RawModel quad;
	// filters
	// private static ContrastChanger contrastChanger;
	// private static BrightFilter brightFilter;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static HorizontalBlur hBlur2;
	private static VerticalBlur vBlur2;
	private static CombineFilter combineFilter;

	public static void init(Loader loader) {
		quad = loader.loadToVAO(POSITIONS, 2);
		hBlur = new HorizontalBlur(WindowManager.getWindow("main").getWidth() / 4,
				WindowManager.getWindow("main").getHeight() / 4);
		vBlur = new VerticalBlur(WindowManager.getWindow("main").getWidth() / 4,
				WindowManager.getWindow("main").getHeight() / 4);
		hBlur2 = new HorizontalBlur(WindowManager.getWindow("main").getWidth() / 2,
				WindowManager.getWindow("main").getHeight() / 2);
		vBlur2 = new VerticalBlur(WindowManager.getWindow("main").getWidth() / 2,
				WindowManager.getWindow("main").getHeight() / 2);
		combineFilter = new CombineFilter();
	}

	public static void doPostProcessing(int colourTexture, int brightTexture) {
		start();
		hBlur2.render(brightTexture);
		vBlur2.render(hBlur2.getOutputTexture());
		hBlur2.render(vBlur2.getOutputTexture());
		vBlur2.render(hBlur2.getOutputTexture());
		combineFilter.render(colourTexture, vBlur2.getOutputTexture());
		end();
	}

	public static void cleanUp() {
		hBlur.cleanUp();
		vBlur.cleanUp();
		hBlur2.cleanUp();
		vBlur2.cleanUp();
		combineFilter.cleanUp();

	}

	private static void start() {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}
