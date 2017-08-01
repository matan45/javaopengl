package guis.windowgui;

import org.lwjgl.glfw.GLFW;

import guis.Basicgui;
import guis.GuiHandler;
import guis.button.Button;
import guis.button.IButton;
import input.MouseCursor;
import input.Mouseinput;
import maths.Vector2f;

public class WindowGui extends Basicgui implements IButton {

	IWindowgui window;
	Vector2f position = new Vector2f();
	Vector2f preposition;
	Button close;
	boolean canbedarg;

	public WindowGui(int texture, Vector2f position, Vector2f scale, Vector2f rotation, Button close) {
		super(texture, position, scale, rotation);
		this.close = close;
		this.close.setPosition(new Vector2f((position.x + scale.x)-close.getScale().x, (position.y + scale.y)-close.getScale().y));
		this.close.setB(this);
		GuiHandler.addgui(this);
		GuiHandler.addgui(this.close);
	}

	private void ClickAndDrag() {
		if (super.IsCursorOn()&&canbedarg) {
			if (Mouseinput.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
				position = MouseCursor.getNormalizePosition();
				window.whileDrag();
				preposition = new Vector2f(position.x, position.y);
				this.setPosition(preposition);
				close.setPosition(new Vector2f((preposition.x + this.getScale().x)-close.getScale().x, (preposition.y + this.getScale().y)-close.getScale().y));

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

	@Override
	public void onClick() {
		window.onclose();
		GuiHandler.removegui(2);
		GuiHandler.removegui(1);
	}

	@Override
	public void onHover() {
		canbedarg=false;

	}

	@Override
	public void stopHover() {
		canbedarg=true;

	}

}
