package test;

import aduio.PlayList;
import game.PhysicsTest;
import video.MediaPanel;
import window.MasterWindow;
import window.Window;

public class Run {

	public static void main(String[] args) {

		MediaPanel.start("test.mp4");
		MediaPanel.WaitToEnd();

		PlayList s = PlayList.getInstance();
		s.start();

		MasterWindow mainwin = new Window(800, 600, "Game Engine OpenGL", "main");
		mainwin.setRestore();
		mainwin.Keyinput(true);
		mainwin.Scrollinput(true);
		mainwin.Mouseinput(true);
		mainwin.Cursorinput(true);
		mainwin.run(new PhysicsTest());
		s.closePlayList();

		
	
	}

}
