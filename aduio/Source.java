package aduio;

import org.lwjgl.openal.AL10;

import maths.Vector3f;

public class Source {
	private int sourceID;
	private int buffer;

	public Source(int RollFactor, int ReferenceD, int MaxDistance) {
		sourceID = AL10.alGenSources();
		// the distance from the source will be 1
		AL10.alSourcef(sourceID, AL10.AL_ROLLOFF_FACTOR, RollFactor);
		// the speed the sound decrees
		AL10.alSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE, ReferenceD);
		// the max distance for the sound to stop
		AL10.alSourcef(sourceID, AL10.AL_MAX_DISTANCE, MaxDistance);
	}

	public void setBuffer(int buffer) {
		this.buffer = buffer;
	}

	public void play() {
		stop();
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceID);
	}

	public void delete() {
		stop();
		AL10.alDeleteSources(sourceID);
	}

	public void setVelocity(float x, float y, float z) {
		AL10.alSource3f(sourceID, AL10.AL_VELOCITY, x, y, z);
	}

	public void pause() {
		AL10.alSourcePause(sourceID);
	}

	public void continuePlaying() {
		AL10.alSourcePlay(sourceID);
	}

	public void stop() {
		AL10.alSourceStop(sourceID);
	}

	public void setLooping(boolean loop) {
		AL10.alSourcei(sourceID, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}

	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	public void setVolume(float volume) {
		AL10.alSourcef(sourceID, AL10.AL_GAIN, volume);
	}

	public void setPitch(float pitch) {
		AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
	}

	public void setPosition(Vector3f position) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, position.x, position.y, position.z);
	}
	
	public void setOrientation(Vector3f at, Vector3f up) {
        float[] data = new float[6];
        data[0] = at.x;
        data[1] = at.y;
        data[2] = at.z;
        data[3] = up.x;
        data[4] = up.y;
        data[5] = up.z;
        AL10.alListenerfv(AL10.AL_ORIENTATION, data);
    }

}
