package Physics;

import maths.Vector3f;

public class Sphere extends Collider {

	Vector3f center;
	float radius;

	public Sphere(Vector3f center, float radius, Layers layer) {
		super(ColliderType.TYPE_SPHERE, layer);
		this.center = center;
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	public IntersectData IntersectSphere(Sphere other) {
		Vector3f direction = new Vector3f();
		float radiusDistance = radius + other.radius;
		Vector3f.sub(other.getCenter(), this.center, direction);
		float centerDistance = direction.length();

		float distance = centerDistance - radiusDistance;

		return new IntersectData(distance < 0, distance);
	}

	@Override
	public Vector3f getCenter() {
		return center;
	}

	@Override
	public void updateCenter(Vector3f center) {
		this.center = center;

	}

}
