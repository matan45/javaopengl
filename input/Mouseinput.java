package input;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Mouseinput extends GLFWMouseButtonCallback{
	private static int[] mouseButtonStates = new int[16];
    private static boolean[] activeMouseButtons = new boolean[16];
    private static long lastMouseNS = 0;
    private static long mouseDoubleClickPeriodNS = 1000000000 / 5; //5th of a second for double click.
    private static int NO_STATE = -1;


	@Override
	public void invoke(long window, int button, int action, int mods) {
		activeMouseButtons[button] = action != GLFW_RELEASE;
        mouseButtonStates[button] = action;

	}
	public static boolean mouseButtonDown(int button)
    {
        return activeMouseButtons[button];
    }

    public static boolean mouseButtonPressed(int button)
    {
        return mouseButtonStates[button] == GLFW_RELEASE;
    }

    public static boolean mouseButtonReleased(int button)
    {
        boolean flag = mouseButtonStates[button] == GLFW_RELEASE;

        if (flag)
            lastMouseNS = System.nanoTime();

        return flag;
    }

    public static boolean mouseButtonDoubleClicked(int button)
    {
        long last = lastMouseNS;
        boolean flag = mouseButtonReleased(button);

        long now = System.nanoTime();

        if (flag && now - last < mouseDoubleClickPeriodNS)
        {
            lastMouseNS = 0;
            return true;
        }

        return false;
    }
    public static void resetMouse()
    {
        for (int i = 0; i < mouseButtonStates.length; i++)
        {
            mouseButtonStates[i] = NO_STATE;
        }

        long now = System.nanoTime();

        if (now - lastMouseNS > mouseDoubleClickPeriodNS)
            lastMouseNS = 0;
    }


}
