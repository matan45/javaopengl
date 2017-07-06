package aduio;

public class PlayList implements Runnable {

	volatile boolean running=true;
	Thread t;
	
	@Override
	public void run() {
		Music.create("556.wav", false);
		waiting();
		Music.create("667.wav", false);
		
	}
	
	private void waiting(){
		while(!Music.isDone()){
			if(!running)
				break;
		}
	}
	
	public void closePlayList(){
		running=false;
		t.interrupt();
	}
	
	public void start(){
		t=new Thread(this);
		t.start();
	}
}
