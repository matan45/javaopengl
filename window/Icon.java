package window;

import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;

public class Icon {

	public static void setIcon(String path, long window) throws Exception {
		IntBuffer w = memAllocInt(1);
		IntBuffer h = memAllocInt(1);
		IntBuffer comp = memAllocInt(1);

		// Icons
		{
			ByteBuffer icon16;
			ByteBuffer icon32;
			try {
				icon16 = ioResourceToByteBuffer(path, 2048);
				icon32 = ioResourceToByteBuffer(path, 4096);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			try (GLFWImage.Buffer icons = GLFWImage.malloc(2)) {
				ByteBuffer pixels16 = STBImage.stbi_load_from_memory(icon16, w, h, comp, 4);
				icons.position(0).width(w.get(0)).height(h.get(0)).pixels(pixels16);

				ByteBuffer pixels32 = STBImage.stbi_load_from_memory(icon32, w, h, comp, 4);
				icons.position(1).width(w.get(0)).height(h.get(0)).pixels(pixels32);

				icons.position(0);
				glfwSetWindowIcon(window, icons);

				STBImage.stbi_image_free(pixels32);
				STBImage.stbi_image_free(pixels16);
			}
		}

		memFree(comp);
		memFree(h);
		memFree(w);

	}

	private static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
		ByteBuffer buffer;

		Path path = Paths.get(resource);
		if (Files.isReadable(path)) {
			try (SeekableByteChannel fc = Files.newByteChannel(path)) {
				buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
				while (fc.read(buffer) != -1)
					;
			}
		} else {
			try (InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
					ReadableByteChannel rbc = Channels.newChannel(source)) {
				buffer = BufferUtils.createByteBuffer(bufferSize);

				while (true) {
					int bytes = rbc.read(buffer);
					if (bytes == -1)
						break;
					if (buffer.remaining() == 0)
						buffer = resizeBuffer(buffer, buffer.capacity() * 2);
				}
			}
		}

		buffer.flip();
		return buffer;
	}

	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}

	public static void IconCursor(String path, long window) throws IOException {

		InputStream stream = new FileInputStream(path);

		BufferedImage image = ImageIO.read(stream);

		int width = image.getWidth();
		int height = image.getHeight();

		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);

		// convert image to RGBA format
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = pixels[y * width + x];

				buffer.put((byte) ((pixel >> 16) & 0xFF)); // red
				buffer.put((byte) ((pixel >> 8) & 0xFF)); // green
				buffer.put((byte) (pixel & 0xFF)); // blue
				buffer.put((byte) ((pixel >> 24) & 0xFF)); // alpha
			}
		}
		buffer.flip(); // this will flip the cursor image vertically

		// create a GLFWImage
		GLFWImage cursorImg = GLFWImage.create();
		cursorImg.width(width); // setup the images' width
		cursorImg.height(height); // setup the images' height
		cursorImg.pixels(buffer); // pass image data

		// create custom cursor and store its ID
		int hotspotX = 0;
		int hotspotY = 0;
		long cursorID = GLFW.glfwCreateCursor(cursorImg, hotspotX, hotspotY);

		// set current cursor
		GLFW.glfwSetCursor(window, cursorID);

	}

}
