package window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import game.GameLogic;

public class Window extends MasterWindow {
	String windowkey;
	static float deltaTime;

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
		int prew = this.Width, preh = this.Height;

		try {
			Icon.setIcon(icon, window);
			Icon.IconCursor(cursor, window);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//anti aliasing
		GL11.glEnable(GL13.GL_MULTISAMPLE);

		c.preupdate();

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		long time = System.nanoTime();

		while (!GLFW.glfwWindowShouldClose(window)) {
			// clear the frame buffer
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			long  t  = System.nanoTime();
			deltaTime = (float)((t - time) / 1000000000.0);
            time = t;


			if (this.Width != prew || this.Height != preh) {
				GL11.glViewport(-1, 1, this.Width, this.Height);
				prew = this.Width;
				preh = this.Height;
			}

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

	public static float getDeltaTime() {
		return deltaTime;
	}

	

}
