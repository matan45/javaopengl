package aduio;

public class PlayList implements Runnable {

	volatile boolean running = true;
	Thread t;
	static PlayList play = new PlayList();

	@Override
	public void run() {
		while (running) {
			Music.create("Cassiopea.wav", false);
			waiting();
			Music.create("Suns_And_Stars.wav", false);
			waiting();
			Music.create("Everdream.wav", false);
			waiting();
		}

	}

	@SuppressWarnings("static-access")
	private void waiting() {
		try {
			t.sleep(Music.AudioLengthMilliseconds());
		} catch (InterruptedException e) {}
	}

	public void closePlayList() {
		running = false;
		t.interrupt();
	}

	public static PlayList getInstance() {
		return play;
	}

	public void start() {
		t = new Thread(this);
		t.start();
	}
}
