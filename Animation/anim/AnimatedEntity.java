package Animation.anim;

import maths.Vector3f;

public class AnimatedEntity {
	Vector3f translation;
	Vector3f scale;
	Vector3f rotation;
	AnimGameItem aim;

	public AnimatedEntity(Vector3f translation, Vector3f scale, Vector3f rotation, AnimGameItem aim) {
		super();
		this.translation = translation;
		this.scale = scale;
		this.rotation = rotation;
		this.aim = aim;
	}

	public Vector3f getTranslation() {
		return translation;
	}

	public void setTranslation(Vector3f translation) {
		this.translation = translation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public AnimGameItem getAim() {
		return aim;
	}

	public void setAim(AnimGameItem aim) {
		this.aim = aim;
	}

}
