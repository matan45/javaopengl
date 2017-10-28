package test;

import game.Videotest;
import window.MasterWindow;
import window.Window;

public class Run {

	public static void main(String[] args) {

		MasterWindow mainwin = new Window(800, 600, "Game Engine OpenGL", "main");
		mainwin.setRestore();
		mainwin.Keyinput(true);
		mainwin.Scrollinput(true);
		mainwin.Mouseinput(true);
		mainwin.Cursorinput(true);
		mainwin.run(new Videotest());

	}

}
