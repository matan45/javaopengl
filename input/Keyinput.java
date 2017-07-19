package input;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class Keyinput extends GLFWKeyCallback {

	private static boolean[] keys = new boolean[1024];
	private static int[] keyStates = new int[1024];
	private static int NO_STATE = -1;

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action != GLFW_RELEASE;
		keyStates[key] = action;

	}

	public static boolean keyDown(int key) {
		return keys[key];
	}

	public static boolean keyPressed(int key) {
		return keyStates[key] == GLFW_PRESS;
	}

	public static boolean keyReleased(int key) {
		return keyStates[key] == GLFW_RELEASE;
	}
	
	public static void resetKeyboard()
    {
        for (int i = 0; i < keyStates.length; i++)
        {
            keyStates[i] = NO_STATE;
        }
    }

}
