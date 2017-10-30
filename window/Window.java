package window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Window extends MasterWindow {
	String windowkey;
	static float deltaTime;
	int ticks = 0;
	int fixframe = 2;
	
	public Window(int sizeX, int sizeY, String title, String windowkey) {
		super(sizeX, sizeY, title);
		this.windowkey = windowkey;
		WindowManager.addWindow(windowkey, this);
	}

	public void run() {
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
		// anti aliasing
		GL11.glEnable(GL13.GL_MULTISAMPLE);
		
		SceneManager.frist();//for the first time

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		long time = System.nanoTime();

		while (!GLFW.glfwWindowShouldClose(window)) {
			// clear the frame buffer
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			if ((ticks % fixframe) == 0) {
				SceneManager.fixedupdate();
			}

			long t = System.nanoTime();
			deltaTime = (float) ((t - time) / 1000000000.0);
			time = t;

			ticks++;

			if (this.Width != prew || this.Height != preh) {
				GL11.glViewport(-1, 1, this.Width, this.Height);
				prew = this.Width;
				preh = this.Height;
			}

			SceneManager.update();

			GLFW.glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			GLFW.glfwPollEvents();
		}

		Windowclose();
	}

	private void Windowclose() {
		SceneManager.close();//for the final time
		WindowManager.removeWindow(windowkey);
		WindowManager.close();
		super.close();
	}

	public static float getDeltaTime() {
		return deltaTime;
	}

	public int getFixframe() {
		return fixframe;
	}

	public void setFixframe(int fixframe) {
		this.fixframe = fixframe;
	}

}
