package input;

import org.lwjgl.glfw.GLFWScrollCallback;

public class MouseScroll extends GLFWScrollCallback {
	static Scroll s;

	@Override
	public void invoke(long window, double xoffset, double yoffset) {
		if (yoffset > 0)
			s.ScrollUp(yoffset);
		if (yoffset < 0)
			s.ScrollDown(yoffset);

	}

	public static void setS(Scroll s) {
		MouseScroll.s = s;
	}

	public interface Scroll {
		public void ScrollUp(double speed);

		public void ScrollDown(double speed);
	}

}
