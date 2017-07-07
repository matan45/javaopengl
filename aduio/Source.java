package aduio;

import org.lwjgl.openal.AL10;

public class Source {
	private int sourceID;

	public Source(int RollFactor,int ReferenceD,int MaxDistance) {
		sourceID = AL10.alGenSources();
		//the distance from the source will be 1
		AL10.alSourcef(sourceID, AL10.AL_ROLLOFF_FACTOR, RollFactor);
		//the speed the sound decrees 
		AL10.alSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE, ReferenceD);
		//the max distance for the sound to stop
		AL10.alSourcef(sourceID, AL10.AL_MAX_DISTANCE, MaxDistance);
	}

	public void play(int buffer) {
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
	
	public void pause(){
		AL10.alSourcePause(sourceID);
	}
	
	public void continuePlaying(){
		AL10.alSourcePlay(sourceID);
	}
	public void stop(){
		AL10.alSourceStop(sourceID);
	}
	
	public void setLooping(boolean loop){
		AL10.alSourcei(sourceID,AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	public boolean isPlaying(){
		return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE)==AL10.AL_PLAYING;
	}

	public void setVolume(float volume) {
		AL10.alSourcef(sourceID, AL10.AL_GAIN, volume);
	}

	public void setPitch(float pitch) {
		AL10.alSourcef(sourceID, AL10.AL_POSITION, pitch);
	}

	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
	}

}
