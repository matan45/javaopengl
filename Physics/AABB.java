package Physics;

import maths.Vector3f;

public class AABB extends Collider {
	Vector3f minExtents; // bottom left corner
	Vector3f maxExtents; // top right corner
	Vector3f center;
	Vector3f halfwidths;
	Vector3f rotation;


	public AABB(Vector3f center, Vector3f halfwidths, Layers layer) {
		super(ColliderType.TYPE_AABB, layer);
		this.halfwidths = halfwidths;
		updateCenter(center);

	}

	public Vector3f getMinExtents() {
		return minExtents;
	}

	public Vector3f getMaxExtents() {
		return maxExtents;
	}

	public IntersectData IntersectAABB(AABB other) {
		if ((this.maxExtents.x >= other.getMinExtents().x && this.minExtents.x <= other.getMaxExtents().x
				&& this.maxExtents.y >= other.getMinExtents().y && this.minExtents.y <= other.getMaxExtents().y
				&& this.maxExtents.z >= other.getMinExtents().z && this.minExtents.z <= other.getMaxExtents().z)) {

			Vector3f distances1 = new Vector3f();
			Vector3f.sub(other.getMinExtents(), this.maxExtents, distances1);

			Vector3f distances2 = new Vector3f();
			Vector3f.sub(this.minExtents, other.getMaxExtents(), distances2);

			Vector3f distances = distances1.Max(distances2);

			float maxDistance = distances.MaxValue();
			return new IntersectData(true, maxDistance);
		}
		return new IntersectData(false, 0);
	}

	@Override
	public Vector3f getCenter() {
		return center;
	}

	public IntersectData IntersectSphere(Sphere other) {
		Vector3f distances1 = new Vector3f();
		Vector3f.sub(other.getCenter(), this.maxExtents, distances1);

		Vector3f distances2 = new Vector3f();
		Vector3f.sub(this.minExtents, other.getCenter(), distances2);

		Vector3f distances = distances1.Max(distances2);
		float distanceX = Math.abs(distances.x);
		float distanceY = Math.abs(distances.y);
		float distanceZ = Math.abs(distances.z);
		if (distanceX <= other.getRadius() && distanceY <= other.getRadius() && distanceZ <= other.getRadius()) {
			return new IntersectData(true, distanceX - other.getRadius());
		}
		return new IntersectData(false, 0);
	}

	@Override
	public void updateCenter(Vector3f center) {

		this.center = center;
		this.maxExtents = Vector3f.add(this.center, this.halfwidths, this.maxExtents);
		this.minExtents = Vector3f.sub(this.center, this.halfwidths, this.minExtents);
		this.maxExtents.y = this.maxExtents.y + this.halfwidths.y;
		this.minExtents.y = this.minExtents.y + this.halfwidths.y;
		

	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
		PositionAfterRotation();
	}

	private void PositionAfterRotation() {

		Vector3f maxsub = new Vector3f();
		maxsub = Vector3f.sub(maxExtents, center, maxsub);
		Vector3f maxro = new Vector3f();
		maxro = maxsub.rotate(this.rotation);
		maxExtents = Vector3f.add(maxro, center, maxExtents);

		Vector3f minsub = new Vector3f();
		minsub = Vector3f.sub(minExtents, center, minsub);
		Vector3f minro = new Vector3f();
		minro = minsub.rotate(this.rotation);
		minExtents = Vector3f.add(minro, center, minExtents);

	}

}
