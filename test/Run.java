package test;

import aduio.PlayList;
import game.Game;
import video.MediaPanel;
import window.Window;

public class Run {

	public static void main(String[] args) {
		
		MediaPanel.start("open video.mp4");
		MediaPanel.WaitToEnd();
		
		PlayList s = PlayList.getInstance();
		s.start();
		Game game = new Game();
		Window mainwin = new Window(800, 600, "Game Engine OpenGL");
		mainwin.run(game);
		s.closePlayList();
	}
	

}
