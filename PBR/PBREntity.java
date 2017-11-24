package PBR;

import maths.Vector3f;
import renderer.RawModel;

public class PBREntity {
	private RawModel model;
	private Vector3f position;
	Vector3f scale;
	Vector3f rotation;
	Material material;

	public PBREntity(RawModel model, Vector3f position, Vector3f rotation, Vector3f scale, Material material) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.material = material;
	}

	

	public RawModel getModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
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

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

}
