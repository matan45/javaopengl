package aduio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Music {
	static String loc = "src/resources/audio/";
	static Clip clip;
	static AudioInputStream audioIn;
	static int lastframe;
	static FloatControl Control;

	public static void create(String file, boolean loop) {
		try {
			audioIn = AudioSystem.getAudioInputStream(new File(loc + file + ".wav"));
			try {
				clip = AudioSystem.getClip();
				clip.open(audioIn);
				Control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				clip.start();
				if (loop)
					clip.loop(Clip.LOOP_CONTINUOUSLY);

			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean isDone() {
		return (clip.getFrameLength() == clip.getFramePosition());
	}

	public static long AudioLengthMilliseconds() {
		return clip.getMicrosecondLength() / 1000;
	}

	public static void pause() {
		lastframe = clip.getFramePosition();
		clip.stop();
	}

	public static void continuePlay() {
		clip.setFramePosition(lastframe);
		clip.start();
	}

	public static void close() {
		clip.stop();
		clip.close();
	}

	public static void mute() {
		Control.setValue(-80.0f);
	}

	public static void unmute() {
		Control.setValue(5.0f);
	}

	public static Clip getClip() {
		return clip;
	}
	

}
