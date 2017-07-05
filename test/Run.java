package test;

import game.Game;
import window.Window;

public class Run {

	public static void main(String[] args) {
		
		Game test=new Game();
		Window mainwin=new Window(800, 600, "Game Engine OpenGL");
		mainwin.run(test);
	}

}
