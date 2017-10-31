package Animated2D;

import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_info_from_memory;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import guis.GuiRenderer;
import guis.GuiTexture;
import maths.Vector2f;
import utill.IOUtil;

//need to test
public class Image2D {
	double delay = 0;
	int index = 0;
	int tick = 0;
	GuiTexture frame;
	GuiRenderer guiRenderer;
	boolean loop;
	boolean Done;
	List<GuiTexture> frames = new ArrayList<>();
	List<Integer> textures = new ArrayList<Integer>();

	// folder path that contain images files only
	public Image2D(String folderpath, double delay, GuiRenderer guiRenderer, Vector2f position, Vector2f scale,
			Vector2f rotation, boolean loop) {
		this.loop = loop;
		this.delay = delay;
		this.guiRenderer = guiRenderer;
		Done = false;
		File folder = new File(folderpath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile())
				frames.add(
						new GuiTexture(loadTexture(folderpath + listOfFiles[i].getName()), position, scale, rotation));
		}
		frame = frames.get(0);

	}

	public void start() {
		if (!Done) {
			tick++;

			if ((tick % delay == 0) && (index < frames.size())) {
				frame = frames.get(index);
				index++;
				tick = 0;
			}

			guiRenderer.render(frame);

			if (index == frames.size() && !loop) {
				cleanup();
				Done = true;
			}
			if (index == frames.size() && loop) {
				index = 0;
			}
		}
	}

	private int loadTexture(String fileName) {
		ByteBuffer imageBuffer;
		ByteBuffer image;
		try {
			imageBuffer = IOUtil.ioResourceToByteBuffer(fileName, 8 * 1024);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);

		// Use info to read image metadata without decoding the entire image.
		// We don't need this for this demo, just testing the API.
		if (!stbi_info_from_memory(imageBuffer, w, h, comp)) {
			throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());
		}

		// Decode the image
		image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
		if (image == null) {
			throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
		}

		int id = GL11.glGenTextures();
		textures.add(id);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);

		if (comp.get(0) == 3) {
			if ((w.get(0) & 3) != 0) {
				GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 2 - (w.get(0) & 1));
			}
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, w.get(0), h.get(0), 0, GL11.GL_RGB,
					GL11.GL_UNSIGNED_BYTE, image);
		} else {
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w.get(0), h.get(0), 0, GL11.GL_RGBA,
					GL11.GL_UNSIGNED_BYTE, image);
		}
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.5f);

		float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, amount);

		return id;
	}

	private void cleanup() {
		for (int texture : textures) {
			GL11.glDeleteTextures(texture);
		}

	}

	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public boolean isDone() {
		return Done;
	}

}
