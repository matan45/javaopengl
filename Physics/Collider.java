package Physics;

public abstract class Collider implements ICollider {

	public enum ColliderType {
		TYPE_SPHERE, TYPE_AABB
	}

	public enum Layers {
		Physics_Layer, TriggerBox_Layer
	}

	ColliderType type;
	Layers layer;

	public Collider(ColliderType type, Layers layer) {
		this.type = type;
		this.layer = layer;
	}

	public Layers getLayers() {
		return layer;
	}

	public ColliderType getType() {
		return type;
	}

	public IntersectData intersect(ICollider other) {
		if (this.getLayers() == other.getLayers()) {
			if (this.getType() == ColliderType.TYPE_SPHERE && other.getType() == ColliderType.TYPE_SPHERE) {
				Sphere self = (Sphere) this;
				return self.IntersectSphere((Sphere) other);
			} else if (this.getType() == ColliderType.TYPE_AABB && other.getType() == ColliderType.TYPE_AABB) {
				AABB self = (AABB) this;
				return self.IntersectAABB((AABB) other);
			} else if (this.getType() == ColliderType.TYPE_AABB && other.getType() == ColliderType.TYPE_SPHERE) {
				AABB self = (AABB) this;
				return self.IntersectSphere((Sphere) other);
			} else if (this.getType() == ColliderType.TYPE_SPHERE && other.getType() == ColliderType.TYPE_AABB) {
				AABB self = (AABB) other;
				return self.IntersectSphere((Sphere) this);
			}
		}
		return new IntersectData(false, 0);
	}
}
