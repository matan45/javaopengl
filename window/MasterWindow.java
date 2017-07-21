package window;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_DEBUG_CONTEXT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import game.GameLogic;
import input.Keyinput;
import input.MouseCursor;
import input.MouseScroll;
import input.Mouseinput;

public abstract class MasterWindow {
	// The window handle
	long window;
	int Width, Height;
	String title;

	// input
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseCallback;
	private GLFWCursorPosCallback mousePosition;
	private GLFWScrollCallback mouseScroll;

	static String icon = "src/resources/icons/main_icon.png";
	static String cursor="src/resources/icons/Cursor.png";
	
	public MasterWindow(int sizeX, int sizeY, String title) {

		this.Width = sizeX;
		this.Height = sizeY;
		this.title = title;
		init();

	}

	protected void close() {
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
		// already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE); // the window will stay hidden
													// after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be
													// resizable
		
		//full screen glfwCreateWindow(Width, Height, title, glfwGetPrimaryMonitor(), NULL);
		// Create the window
		window = glfwCreateWindow(Width, Height, title, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		
		//init input
		keyCallback = new Keyinput();
		mouseCallback = new Mouseinput();
		mousePosition = new MouseCursor();
		mouseScroll= new MouseScroll();
		
		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);

		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}

	public abstract void run(GameLogic c);

	public void setHide() {
		GLFW.glfwHideWindow(window);
	}

	public void setRestore() {
		GLFW.glfwRestoreWindow(window);
	}

	public void setShow() {
		GLFW.glfwShowWindow(window);
	}

	public void setMaximize() {
		GLFW.glfwMaximizeWindow(window);
	}

	public long getWindow() {
		return window;
	}

	public int getWidth() {
		return Width;
	}

	public int getHeight() {
		return Height;
	}

	public void Cursormodes(int mode) {
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, mode);
	}

	public void Keyinput(boolean value) {
		if (value)
			GLFW.glfwSetKeyCallback(window, keyCallback);
		else
			GLFW.glfwSetKeyCallback(window, null);
	}

	public void Mouseinput(boolean value) {
		if (value)
			GLFW.glfwSetMouseButtonCallback(window, mouseCallback);
		else
			GLFW.glfwSetMouseButtonCallback(window, null);
	}

	public void Cursorinput(boolean value) {
		if (value)
			GLFW.glfwSetCursorPosCallback(window, mousePosition);
		else
			GLFW.glfwSetCursorPosCallback(window, null);
	}
	
	public void Scrollinput(boolean value) {
		if (value)
			GLFW.glfwSetScrollCallback(window, mouseScroll);
		else
			GLFW.glfwSetScrollCallback(window, null);
	}

}
