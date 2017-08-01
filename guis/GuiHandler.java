package guis;

import java.util.ArrayList;
import java.util.List;

public class GuiHandler {
	//need to do hash map
	static List<Basicgui> guis = new ArrayList<>();
	static List<GuiTexture> textruegui = new ArrayList<>();


	public static void update() {
		for (int i = 0; i < guis.size(); i++)
			guis.get(i).update();
	}

	public static void addgui(Basicgui gui) {
		guis.add(gui);
		textruegui.add(gui);
	}

	public static void removegui(int index) {
		guis.remove(index);
		textruegui.remove(index);
	}

	public static List<GuiTexture> getGuis() {
		return textruegui;
	}

	public static int getsize() {
		return guis.size();
	}

	public static void cleanup() {
		textruegui.clear();
		guis.clear();

	}
}
