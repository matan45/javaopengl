package utill;

import text.GUIText;

public class Time {

	static long timer = System.currentTimeMillis();
	static int frames = 0;
	static GUIText text;

	public static void fps() {

		frames++;
		if (System.currentTimeMillis() - timer > 1000) {
			text.print(" ");
			text.print(frames + " fps");

			timer = System.currentTimeMillis();
			frames = 0;
			
		}

	}

	public static void setText(GUIText text) {
		Time.text = text;
	}

}
