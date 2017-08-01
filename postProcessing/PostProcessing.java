package postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import bloom.BrightFilter;
import bloom.CombineFilter;
import gaussianBlur.HorizontalBlur;
import gaussianBlur.VerticalBlur;
import renderer.Loader;
import renderer.RawModel;
import window.WindowManager;

public class PostProcessing {

	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static RawModel quad;
	private static ContrastChanger contrastChanger;
	private static BrightFilter brightFilter;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static CombineFilter combineFilter;
	

	public static void init(Loader loader) {
		quad = loader.loadToVAO(POSITIONS, 2);
		contrastChanger = new ContrastChanger();
		brightFilter = new BrightFilter(WindowManager.getWindow("main").getWidth()/2, WindowManager.getWindow("main").getHeight()/2);
		hBlur=new HorizontalBlur(WindowManager.getWindow("main").getWidth()/5, WindowManager.getWindow("main").getHeight()/5);
		vBlur=new VerticalBlur(WindowManager.getWindow("main").getWidth()/5, WindowManager.getWindow("main").getHeight()/5);
		combineFilter = new CombineFilter();
	}

	public static void doPostProcessing(int colourTexture) {
		start();
		brightFilter.render(colourTexture);
		hBlur.render(brightFilter.getOutputTexture());
		vBlur.render(hBlur.getOutputTexture());
		combineFilter.render(colourTexture, vBlur.getOutputTexture());
		end();
	}

	public static void cleanUp() {
		contrastChanger.cleanUp();
		brightFilter.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
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
