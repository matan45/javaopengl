package openglvideo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import guis.GuiRenderer;
import guis.GuiTexture;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import maths.Vector2f;

public class Video {
	Player playMP3;
	GuiRenderer guiRenderer;
	List<Integer> textures = new ArrayList<Integer>();
	double frameRate = 0;
	List<GuiTexture> frames = new ArrayList<>();
	int i = 0;
	GuiTexture frame;
	boolean Done = false;

	public Video(String mp4, String mp3, GuiRenderer guiRenderer, Vector2f position, Vector2f scale,
			Vector2f rotation) {
		this.guiRenderer = guiRenderer;
		File file = new File(mp4);

		try {
			if (!mp3.isEmpty()) {
				FileInputStream fis;
				fis = new FileInputStream(new File(mp3));
				playMP3 = new Player(fis);
			}
			FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(file));
			Picture picture;

			while (null != (picture = grab.getNativeFrame())) {
				BufferedImage frame = AWTUtil.toBufferedImage(picture);
				ByteBuffer bb = convertImageData(frame);
				frames.add(new GuiTexture(loadTexture(bb, picture), position, scale, rotation));
			}

			SeekableByteChannel bc = NIOUtils.readableChannel(file);
			MP4Demuxer dm = MP4Demuxer.createMP4Demuxer(bc);
			DemuxerTrack vt = dm.getVideoTrack();
			// frame rate
			int numberOfFrames = vt.getMeta().getTotalFrames();
			double totalDuration = vt.getMeta().getTotalDuration();
			frameRate = numberOfFrames / totalDuration;

		} catch (IOException | JCodecException | JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private ByteBuffer convertImageData(BufferedImage image) {

		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 3);

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
				buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
				buffer.put((byte) (pixel & 0xFF)); // Blue component
			}
		}

		buffer.flip();
		return buffer;

	}

	private int loadTexture(ByteBuffer imageBuffer, Picture picture) {

		int id = GL11.glGenTextures();
		textures.add(id);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, picture.getWidth(), picture.getHeight(), 0, GL11.GL_RGB,
				GL11.GL_UNSIGNED_BYTE, imageBuffer);

		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

		return id;
	}

	public void start() {
		i++;
		if (i == 1) {

			new Thread(new Runnable() {

				@Override
				public void run() {

					try {
						if (playMP3 != null)
							playMP3.play();
					} catch (JavaLayerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}).start();
		}

		try {
			if (i < frames.size()) {
				frame = frames.get(i);
				Thread.sleep((long) (frameRate * 1.318));
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		guiRenderer.render(frame);
		if (i == frames.size()) {
			cleanup();
			Done = true;
		}
	}

	private void cleanup() {
		guiRenderer.cleanUp();
		for (int texture : textures) {
			GL11.glDeleteTextures(texture);
		}

	}

	public boolean isDone() {
		return Done;
	}

}
