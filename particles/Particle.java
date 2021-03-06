package particles;

import entities.Camera;
import entities.Player;
import maths.Vector2f;
import maths.Vector3f;
import window.Window;

public class Particle {
	Vector3f position;
	Vector3f velocity;
	float gravityEffect;
	float lifeLength;
	float rotation;
	float scale;

	ParticleTexture texture;

	Vector2f texOffset1 = new Vector2f();
	Vector2f texOffset2 = new Vector2f();
	float blend;

	float elapsedTime = 0;
	float distance;
	
	Vector3f change=new Vector3f();

	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect,
			float lifeLength, float rotation, float scale) {

		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
	}

	public float getDistance() {
		return distance;
	}

	public Vector2f getTexOffset1() {
		return texOffset1;
	}

	public Vector2f getTexOffset2() {
		return texOffset2;
	}

	public float getBlend() {
		return blend;
	}

	public ParticleTexture getTexture() {
		return texture;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public boolean update(Camera camera) {
		velocity.y += Player.GRAVITY * gravityEffect * Window.getDeltaTime();
		change = new Vector3f(velocity);
		change.scale(Window.getDeltaTime());
		Vector3f.add(change, position, position);
		distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
		updateTextureCoordInfo();
		elapsedTime += Window.getDeltaTime();
		return elapsedTime < lifeLength;

	}

	private void updateTextureCoordInfo() {
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		blend = atlasProgression % 1;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);

	}

	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumberOfRows();
		int row = index / texture.getNumberOfRows();
		offset.x = (float) column / texture.getNumberOfRows();
		offset.y = (float) row / texture.getNumberOfRows();
	}

}
