package Physics;

import maths.Vector3f;

public class AABB extends Collider {
	Vector3f minExtents; // bottom left corner
	Vector3f maxExtents; // top right corner
	Vector3f center;
	Vector3f halfwidths;
	

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

		// get closest point on box from sphere centre
		Vector3f xClosest = ClosestPointOnAABB(other.center);
		// find the separation
		Vector3f xDiff = new Vector3f();
		Vector3f.sub(other.center, xClosest, xDiff);
		// check if points are far enough
		float fDistSquared = xDiff.lengthSquared();

		if (fDistSquared > other.getRadius() * other.getRadius()) {
			return new IntersectData(false, 0);
		}

		float fDist = (float) Math.sqrt(fDistSquared);

		// collision depth
		float fDcoll = other.getRadius() - fDist;

		return new IntersectData(true, fDcoll);

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
		PositionAfterRotation(rotation);
	}

	private void PositionAfterRotation(Vector3f rotation) {

		Vector3f maxsub = new Vector3f();
		maxsub = Vector3f.sub(maxExtents, center, maxsub);
		Vector3f maxro = new Vector3f();
		maxro = maxsub.rotate(rotation);
		maxExtents = Vector3f.add(maxro, center, maxExtents);

		Vector3f minsub = new Vector3f();
		minsub = Vector3f.sub(minExtents, center, minsub);
		Vector3f minro = new Vector3f();
		minro = minsub.rotate(rotation);
		minExtents = Vector3f.add(minro, center, minExtents);

	}

	private Vector3f ClosestPointOnAABB(Vector3f Point) {
		Vector3f xClosestPoint = new Vector3f();
		xClosestPoint.x = (Point.x < this.minExtents.x) ? this.minExtents.x
				: (Point.x > this.maxExtents.x) ? this.maxExtents.x : Point.x;
		xClosestPoint.y = (Point.y < this.minExtents.y) ? this.minExtents.y
				: (Point.y > this.maxExtents.y) ? this.maxExtents.y : Point.y;
		xClosestPoint.z = (Point.z < this.minExtents.z) ? this.minExtents.z
				: (Point.z > this.maxExtents.z) ? this.maxExtents.z : Point.z;
		return xClosestPoint;
	}


}
