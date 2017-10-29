package Animated2D;

import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_info_from_memory;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

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
	int i = 0;
	GuiTexture frame;
	GuiRenderer guiRenderer;
	boolean loop;
	boolean Done;
	List<GuiTexture> frames = new ArrayList<>();
	List<Integer> textures = new ArrayList<Integer>();

	public Image2D(String folderpath, int numberofFrame, double delay, GuiRenderer guiRenderer, Vector2f position,
			Vector2f scale, Vector2f rotation, boolean loop) {
		this.loop = loop;
		this.delay = delay;
		this.guiRenderer = guiRenderer;
		Done = false;

		for (int i = 0; i < numberofFrame; i++) {
			frames.add(new GuiTexture(loadTexture(folderpath + i), position, scale, rotation));
		}

	}

	public void start(double deltatime) {
		i++;

		try {

			if (i < frames.size()) {
				frame = frames.get(i);
				Thread.sleep((long) (delay * deltatime));
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		guiRenderer.render(frame);
		if (i == frames.size() && !loop) {
			cleanup();
			Done = true;
		}
		if (i == frames.size() && loop) {
			i = 0;
		}
	}

	private int loadTexture(String fileName) {
		ByteBuffer imageBuffer;
		ByteBuffer image;
		try {
			imageBuffer = IOUtil.ioResourceToByteBuffer(fileName + ".png", 8 * 1024);
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
		guiRenderer.cleanUp();
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
