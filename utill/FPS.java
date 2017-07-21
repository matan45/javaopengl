package utill;

public class FPS {

	static long timer = System.currentTimeMillis();
	static int updates = 0;
	static int frames = 0;

	public static void update() {
		frames++;
		updates++;
		if (System.currentTimeMillis() - timer > 1000) {
			System.out.println(updates + " ups, " + frames + " fps");
			timer = System.currentTimeMillis();
			updates = 0;
			frames = 0;
		}
	}

}
