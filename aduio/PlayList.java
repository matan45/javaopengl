package aduio;

public class PlayList implements Runnable {

	volatile boolean running;
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


	private void waiting() {	
		try {
			Thread.sleep(Music.AudioLengthMilliseconds());
		} catch (InterruptedException e) {
			closePlayList();
		}
		
	}

	public void closePlayList() {
		running = false;
		Music.close();
		t.interrupt();
		
	}

	public static PlayList getInstance() {
		return play;
	}

	public void start() {
		running = true;
		t = new Thread(this);
		t.start();
	}
}
