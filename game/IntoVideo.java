package game;

import video.MediaPanel;
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
			WindowManager.getWindow("main").run(new Videotest());
		}
		
	}

	@Override
	public void onclose() {
		// TODO Auto-generated method stub
		
	}

}
