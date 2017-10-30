package game;

public interface GameLogic {
	//happen before the main loop
	void preupdate();
	//call fix time a frame food for physics
	void fixedupdate();
	//happen in the main loop
	void update();
	//happen after the main loop
	void onclose();
}
