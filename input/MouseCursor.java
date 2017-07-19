package input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import maths.Vector2f;
import window.MasterWindow;
import window.WindowManager;

public class MouseCursor extends  GLFWCursorPosCallback {
	static double xposition;
	static double yposition;
	static Vector2f normalize=new Vector2f();
	@Override
	public void invoke(long window, double xpos, double ypos) {
		xposition=xpos;
		yposition=ypos;
		
	}
	public static double getXposition() {
		return xposition;
	}
	public static double getYposition() {
		return yposition;
	}
	
	public static Vector2f getNormalizePosition(){
		MasterWindow win=WindowManager.getWindow("main");
		normalize.x = (float)(-1.0 + 2.0 * xposition / win.getWidth()); 
		normalize.y =(float)( 1.0 - 2.0 * yposition / win.getHeight()); 
		return normalize;
	}
}
