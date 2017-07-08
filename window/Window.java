package window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import game.GameLogic;

public class Window extends MasterWindow {
	String windowkey;

	public Window(int sizeX, int sizeY, String title, String windowkey) {
		super(sizeX, sizeY, title);
		this.windowkey = windowkey;
		WindowManager.addWindow(windowkey, this);
	}

	public void run(GameLogic c) {

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		try {
			Icon.setIcon(icon, window);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		c.preupdate();

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (!GLFW.glfwWindowShouldClose(window)) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // clear
																				// the
																				// framebuffer

			c.update();

			GLFW.glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			GLFW.glfwPollEvents();
		}

		c.onclose();
		Windowclose();
	}

	private void Windowclose() {
		WindowManager.removeWindow(windowkey);
		super.close();
	}

}
