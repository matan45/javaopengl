package window;

import game.GameLogic;

public class SceneManager {
	static GameLogic currScene;

	public static void init(GameLogic fristScene) {
		currScene = fristScene;
	}
	public static void frist() {
		currScene.preupdate();
	}

	public static void fixedupdate() {
		currScene.fixedupdate();
	}

	public static void update() {
		currScene.update();
	}

	public static void changeScene(GameLogic newScene) {
		if (currScene != null)
			currScene.onclose();
		newScene.preupdate();
		currScene = newScene;
	}
	
	public static void close(){
		currScene.onclose();
	}

}
