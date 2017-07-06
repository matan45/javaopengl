package test;
import aduio.Music;
import aduio.PlayList;
import game.Game;
import window.Window;


public class Run {

	public static void main(String[] args) {
		
		PlayList s=new PlayList();
		s.start();
		Game test=new Game();
		Window mainwin=new Window(800, 600, "Game Engine OpenGL");
		mainwin.run(test);
		Music.close();
		s.closePlayList();
	}

}
