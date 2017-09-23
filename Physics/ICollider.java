package Physics;

import Physics.Collider.ColliderType;
import maths.Vector3f;

public interface ICollider {
	public Vector3f getCenter();
	public ColliderType getType();
	public IntersectData intersect(ICollider iCollider);
	public void updateCenter(Vector3f center);
}
