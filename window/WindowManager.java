package window;

import java.util.HashMap;

public class WindowManager {
	static HashMap<String, MasterWindow> manager = new HashMap<>();

	public static void addWindow(String key, Window win) {
		if (manager.get(key) == null)
			manager.put(key, win);
		else {
			System.out.println("there is already window whit the keyid");
		}
	}

	public static MasterWindow getWindow(String key) {
		if (manager.get(key) == null) {
			System.out.println("no window whit that key");
			return null;
		}
		return manager.get(key);
	}

	public static void removeWindow(String key) {
		if (manager.get(key) == null) {
			System.out.println("no window whit that key");
			return;
		}
		manager.remove(key);

	}

	public static void replaceWindow(String key, Window win) {
		manager.replace(key, win);
	}

}
