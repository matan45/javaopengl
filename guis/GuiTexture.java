package guis;

import maths.Vector2f;

public class GuiTexture {
	int texture;
	Vector2f position;
	Vector2f scale;
	Vector2f rotation;
	boolean transparent;

	public GuiTexture(int texture, Vector2f position, Vector2f scale, Vector2f rotation) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
	}

	public int getTexture() {
		return texture;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

	public Vector2f getRotation() {
		return rotation;
	}

	public void setRotation(Vector2f rotation) {
		this.rotation = rotation;
	}

	public void increaseRotation(Vector2f rotation) {
		this.rotation.x += rotation.x;
		this.rotation.y += rotation.y;
	}

	public void increasePosition(Vector2f position) {
		this.position.x += position.x;
		this.position.y += position.y;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

}
