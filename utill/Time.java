package utill;

import text.GUIText;

public class Time {

	static long timer = System.currentTimeMillis();
	static int updates = 0;
	static int frames = 0;
	static long lastFrameTime = 0;
	static float deltaTime = System.currentTimeMillis();
	static GUIText text;
	
	public static void fps() {

		deltaTime = (System.currentTimeMillis() - lastFrameTime) / 1000f;
		frames++;
		updates++;
		if (System.currentTimeMillis() - timer > 1000) {
			if(text==null)
				System.out.println(updates + " ups, " + frames + " fps");
			else {
				text.print(" ");
				text.print(updates + " ups, " + frames + " fps");
			}
			timer = System.currentTimeMillis();
			updates = 0;
			frames = 0;
		}
		
		lastFrameTime = System.currentTimeMillis();
	}

	public static float getDeltaTime() {
		return deltaTime;
	}

	public static void setText(GUIText text) {
		Time.text = text;
	}
	

}
