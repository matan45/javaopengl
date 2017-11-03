package video;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
	static EmbeddedMediaPlayer mediaPlayer;
	static JFrame frame;
	static boolean running = false;
	static MediaPlayerFactory mediaPlayerFactory;

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
		frame.setLocation(300, 50);
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		ImageIcon img = new ImageIcon(icon);
		frame.setIconImage(img.getImage());

		Canvas c = new Canvas();

		c.setBackground(Color.BLACK);
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(c, BorderLayout.CENTER);
		Label text = new Label("PASS F TO SKIP...");
		text.setBackground(Color.BLACK);
		text.setFont(new Font("Serif", Font.ITALIC|Font.BOLD, 40));
		text.setForeground(Color.WHITE);
		p.add(text, BorderLayout.SOUTH);
		frame.add(p, BorderLayout.CENTER);

		mediaPlayerFactory = new MediaPlayerFactory();

		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(frame));
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));
		mediaPlayer.setAspectRatio("8:3");

		// mediaPlayer.toggleFullScreen();

		mediaPlayer.setEnableMouseInputHandling(false);

		mediaPlayer.setEnableKeyInputHandling(true);

		mediaPlayer.prepareMedia(location + filename);

		mediaPlayer.play();

		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == 'f' || e.getKeyChar() == 'F') {
					mediaPlayer.stop();
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});

	}

	public static boolean isDone() {
		if (mediaPlayer == null) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!mediaPlayer.isPlaying()) {
			frame.dispose();
			mediaPlayer.release();
			mediaPlayerFactory.release();
			running = false;
		}
		return !running;
	}

}