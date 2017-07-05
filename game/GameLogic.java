package game;

public interface GameLogic {
	//happen before the main loop
	void preupdate();
	//happen in the main loop
	void update();
	//happen after the main loop
	void onclose();
}
