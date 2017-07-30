package utill;

import text.GUIText;

public class Time {

	static long timer = System.currentTimeMillis();
	static int updates = 0;
	static int frames = 0;
	static GUIText text;

	public static void fps() {

		frames++;
		updates++;
		if (System.currentTimeMillis() - timer > 1000) {
			text.print("");
			text.print(updates + " ups " + frames + " fps");

			timer = System.currentTimeMillis();
			updates = 0;
			frames = 0;
		}

	}

	public static void setText(GUIText text) {
		Time.text = text;
	}

}
