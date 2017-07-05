package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import game.Game;
import window.Window;

public class Run {

	public static void main(String[] args) {
		new Thread(()->{
			try {
				FileInputStream fileInputStream = new FileInputStream("src/resources/audio/556.mp3");
				Player player = new Player(fileInputStream);
				System.out.println("Song is playing...");
				player.play();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JavaLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}).start();
		
		
		Game test=new Game();
		Window mainwin=new Window(800, 600, "Game Engine OpenGL");
		mainwin.run(test);

		
	}

}
