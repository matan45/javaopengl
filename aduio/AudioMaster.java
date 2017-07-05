package aduio;

import static org.lwjgl.openal.EXTThreadLocalContext.alcSetThreadContext;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.newdawn.slick.openal.WaveData;

public class AudioMaster {
	private static List<Integer> buffers = new ArrayList<>();
	static long device;
	static long context;
	static String loction="resources/audio/";

	public static void init() {
		device = ALC10.alcOpenDevice((ByteBuffer) null);
		if (device == 0) {
			throw new IllegalStateException("Failed to open the default device.");
		}

		ALCCapabilities deviceCaps = ALC.createCapabilities(device);

		context = ALC10.alcCreateContext(device, (IntBuffer) null);
		alcSetThreadContext(context);
		AL.createCapabilities(deviceCaps);
	}

	public static int loadSound(String file) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData waveFile = WaveData.create(loction+file);
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}
	
	public static void setListenerData(){
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}

	public static void cleanUp() {
		for (int buffer : buffers)
			AL10.alDeleteBuffers(buffer);
		ALC10.alcMakeContextCurrent(NULL);
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);

	}
}
