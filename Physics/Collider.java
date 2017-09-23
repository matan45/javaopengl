package Physics;

public abstract class Collider implements ICollider {

	public enum ColliderType {
		TYPE_SPHERE, TYPE_AABB
	}

	ColliderType type;
	
	
	public Collider(ColliderType type) {
		this.type = type;
	}

	public ColliderType getType() {
		return type;
	}
	

	public IntersectData intersect(ICollider other) {
		if (this.getType() == ColliderType.TYPE_SPHERE && other.getType() == ColliderType.TYPE_SPHERE) {
			Sphere self = (Sphere) this;
			return self.IntersectSphere((Sphere) other);
		} else if (this.getType() == ColliderType.TYPE_AABB && other.getType() == ColliderType.TYPE_AABB) {
			AABB self = (AABB) this;
			return self.IntersectAABB((AABB) other);
		} else if ((this.getType() == ColliderType.TYPE_AABB && other.getType() == ColliderType.TYPE_SPHERE)
				|| (this.getType() == ColliderType.TYPE_SPHERE && other.getType() == ColliderType.TYPE_AABB)) {
			//TODO implement
		}
		return new IntersectData(false, 0);
	}
}
