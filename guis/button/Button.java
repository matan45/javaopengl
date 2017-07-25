package guis.button;

import org.lwjgl.glfw.GLFW;

import guis.Basicgui;
import input.Mouseinput;
import maths.Vector2f;


public class Button extends Basicgui {
	IButton b;
	boolean ishover = false;

	public Button(int texture, Vector2f position, Vector2f scale, Vector2f rotation) {
		super(texture, position, scale, rotation);
		// TODO Auto-generated constructor stub
	}

	public void setB(IButton b) {
		this.b = b;
	}

	public void update() {
		onclick();
		onhover();
	}

	private void onclick() {
		if (Mouseinput.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1) && ishover)
			b.onClick();
	}

	private void onhover() {

		if (super.IsCursorOn()) {
			b.onHover();
			ishover = true;
		} else {
			b.stopHover();
			ishover = false;
		}

	}

}
