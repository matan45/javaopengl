package game;

import guis.GuiRenderer;
import maths.Vector2f;
import openglvideo.Video;
import renderer.Loader;
import window.WindowManager;

public class Videotest implements GameLogic {
	Loader loader = new Loader();
	GuiRenderer guiRenderer;
	Video v;

	@Override
	public void preupdate() {
		guiRenderer = new GuiRenderer(loader);
		// good for short mp4
		v = new Video("src/resources/video/test/test.mp4", "src/resources/video/test/test.mp3", guiRenderer,
				new Vector2f(), new Vector2f(1, 0.7f), new Vector2f());

	}

	@Override
	public void update() {
		
		v.start();
		if (v.isDone()) {
			WindowManager.getWindow("main").run(new Game());
		}

	}

	@Override
	public void onclose() {
		System.out.println("hit");// not call
		// need to do level manager like the window
	}

}
