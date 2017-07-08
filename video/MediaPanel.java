package video;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;

public class MediaPanel {
	static String filename = "";
	static String location = "src/resources/video/";
	static String icon = "src/resources/icons/Video.png";
	static JFrame frame;
	static volatile long size;
	static volatile boolean running;

	public static void start(String file) {
		filename = file;
		running = true;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				chargerLibrairie();
				new MediaPanel();
			}
		});
	}

	static void chargerLibrairie() {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "src/resources/external library/VLC");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		LibXUtil.initialise();
	}


	private MediaPanel() {
		frame = new JFrame("VIDEO");
		frame.setLocation(100, 100);
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		ImageIcon img = new ImageIcon(icon);
		frame.setIconImage(img.getImage());

		Canvas c = new Canvas();

		c.setBackground(Color.black);
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());

		p.add(c, BorderLayout.CENTER);
		frame.add(p, BorderLayout.CENTER);

		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

		EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(frame));
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));

		mediaPlayer.toggleFullScreen();

		mediaPlayer.setEnableMouseInputHandling(false);

		mediaPlayer.setEnableKeyInputHandling(true);

		mediaPlayer.prepareMedia(location + filename);

		mediaPlayer.play();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		size = mediaPlayer.getLength();

	}

	public static void WaitToEnd() {

		try {
			while (size == 0) {
				Thread.sleep(100);
			}
			Thread.sleep(size);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		frame.dispose();

	}

}