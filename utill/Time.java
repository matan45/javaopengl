package utill;

public class Time {

	static long timer = System.currentTimeMillis();
	static int updates = 0;
	static int frames = 0;
	static long lastFrameTime = 0;
	static float deltaTime = System.currentTimeMillis();

	public static void fps() {
		deltaTime = (System.currentTimeMillis() - lastFrameTime) / 1000f;
		frames++;
		updates++;
		if (System.currentTimeMillis() - timer > 1000) {
			System.out.println(updates + " ups, " + frames + " fps");
			timer = System.currentTimeMillis();
			updates = 0;
			frames = 0;
		}
		
		lastFrameTime = System.currentTimeMillis();
	}

	public static float getDeltaTime() {
		return deltaTime;
	}

}
