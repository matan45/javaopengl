package Physics;

import maths.Vector3f;

public class AABB extends Collider {
	Vector3f minExtents; // bottom left corner
	Vector3f maxExtents; // top right corner
	Vector3f center;
	Vector3f halfwidths;

	public AABB(Vector3f center, Vector3f halfwidths) {
		super(ColliderType.TYPE_AABB);
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
		if ((this.maxExtents.x > other.getMinExtents().x && this.minExtents.x < other.getMaxExtents().x
				&& this.maxExtents.y > other.getMinExtents().y && this.minExtents.y < other.getMaxExtents().y
				&& this.maxExtents.z > other.getMinExtents().z && this.minExtents.z < other.getMaxExtents().z)) {

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

	@Override
	public void updateCenter(Vector3f center) {
		this.center = center;
		this.maxExtents = Vector3f.add(this.center, halfwidths, this.maxExtents);
		this.minExtents = Vector3f.sub(this.center, halfwidths, this.minExtents);

	}

}