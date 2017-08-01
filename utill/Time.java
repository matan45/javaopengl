package utill;

import maths.Vector2f;
import text.GUIText;

public class Time {

	static long timer = System.currentTimeMillis();
	static int frames = 0;
	static boolean Btime = true;
	static GUIText text;
	static Vector2f time = new Vector2f();

	public static void fps() {

		frames++;
		if (System.currentTimeMillis() - timer > 1000) {
			text.print("");
			text.print(frames + " fps");

			if (Btime) {
				time.y++;
				if (time.y == 60) {
					time.y = 0;
					time.x++;
				}
				if (time.x == 60)
					time.x = 0;
			}

			timer = System.currentTimeMillis();
			frames = 0;
		}

	}

	public static Vector2f getGameTime() {
		return time;
	}

	public static void setText(GUIText text) {
		Time.text = text;
	}

	public static void setTime(boolean stagetime) {
		Btime = stagetime;
	}

}
