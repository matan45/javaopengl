package Physics;

import Physics.Collider.ColliderType;
import Physics.Collider.Layers;
import maths.Vector3f;

public interface ICollider {
	public Vector3f getCenter();
	public ColliderType getType();
	public Layers getLayers();
	public IntersectData intersect(ICollider iCollider);
	public void updateCenter(Vector3f center);
	
}
