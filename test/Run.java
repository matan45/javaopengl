package test;

import aduio.PlayList;
import game.Game;
import video.MediaPanel;
import window.MasterWindow;
import window.Window;

public class Run {

	public static void main(String[] args) {
		/*
		MediaPanel.start("test.mp4");
		MediaPanel.WaitToEnd();
		*/
		PlayList s = PlayList.getInstance();
		s.start();
		Game game = new Game();
		MasterWindow mainwin = new Window(800, 600, "Game Engine OpenGL","main");
		mainwin.setRestore();
		mainwin.setMaximize();
		mainwin.run(game);
		s.closePlayList();
	}
	

}
