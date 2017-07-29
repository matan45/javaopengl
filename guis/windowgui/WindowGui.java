package guis.windowgui;

import org.lwjgl.glfw.GLFW;

import guis.Basicgui;
import input.MouseCursor;
import input.Mouseinput;
import maths.Vector2f;

public class WindowGui extends Basicgui {

	IWindowgui window;
	int count=0;
	Vector2f position = new Vector2f();
	Vector2f preposition;

	public WindowGui(int texture, Vector2f position, Vector2f scale, Vector2f rotation) {
		super(texture, position, scale, rotation);
		// TODO Auto-generated constructor stub
	}

	private void ClickAndDrag() {
		if (super.IsCursorOn()) {
			if (Mouseinput.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)){
				position=MouseCursor.getNormalizePosition();
				window.whileDrag();
				preposition=new Vector2f(position.x, position.y);
				this.setPosition(preposition);
			}
		}

			


	}

	@Override
	public void update() {
		ClickAndDrag();

	}

	public void setWindow(IWindowgui window) {
		this.window = window;
	}

}
