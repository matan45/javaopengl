package guis;

import input.MouseCursor;
import maths.Vector2f;

public abstract class Basicgui extends GuiTexture implements Igui {

	public Basicgui(int texture, Vector2f position, Vector2f scale, Vector2f rotation) {
		super(texture, position, scale, rotation);
		// TODO Auto-generated constructor stub
	}

	public abstract void update();
	
	public boolean IsCursorOn(){
		Vector2f location = super.getPosition();
		Vector2f scale = super.getScale();
		Vector2f mouseCoordinates = MouseCursor.getNormalizePosition();

		if (location.y + scale.y > mouseCoordinates.y && location.y - scale.y < mouseCoordinates.y
				&& location.x + scale.x > mouseCoordinates.x && location.x - scale.x < mouseCoordinates.x) 
			return true;
		
		
		return false;
	}

}
