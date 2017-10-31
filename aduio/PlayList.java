package aduio;

import java.util.ArrayList;
import java.util.List;

public class PlayList implements Runnable {

	volatile boolean running;
	Thread t;
	static PlayList play = new PlayList();
	List<String> songlist = new ArrayList<>();

	@Override
	public void run() {
		int index=-1;
		Music.create("Silence", false);//needed for init the clip
		Music.Play();
		while (running) {
			if(Music.isDone()){
				index++;
				Music.create(songlist.get(index), false);
				Music.Play();
				if(songlist.size()==index)
					index=-1;
			}

		}
	}


	public void closePlayList() {
		running = false;
		Music.close();
		songlist.clear();
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
	
	public void addsong(String song){
		songlist.add(song);
	}
	
}
