package guis;

import java.util.ArrayList;
import java.util.List;

public class GuiHandler {
	List<Basicgui> guis = new ArrayList<>();
	List<GuiTexture> textruegui = new ArrayList<>();

	public void update() {
		for (Basicgui gui : guis)
			gui.update();
	}

	public void addgui(Basicgui gui) {
		guis.add(gui);
		textruegui.add(gui);
	}

	public void removegui(int index) {
		guis.remove(index);
		textruegui.remove(index);
	}

	public List<GuiTexture> getGuis() {
		return textruegui;
	}

	public void cleanup() {
		textruegui.clear();
		guis.clear();
	}
}
