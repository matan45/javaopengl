package game;

import video.MediaPanel;
import window.SceneManager;
import window.WindowManager;

public class IntoVideo implements GameLogic {

	@Override
	public void preupdate() {
		WindowManager.getWindow("main").setHide();
		MediaPanel.start("test.mp4");
		
	}

	@Override
	public void update() {
		if(MediaPanel.isDone()){
			WindowManager.getWindow("main").setRestore();
			SceneManager.changeScene(new Game());
		}
		
	}

	@Override
	public void onclose() {
		
	}

	@Override
	public void fixedupdate() {
		// TODO Auto-generated method stub
		
	}

}
