package Physics;

import maths.Vector3f;

public class PhysicsObject {
	Vector3f velocity;
	ICollider collider;
	IntersectData data;
	float mass = 0;
	float friction = 0.001f;

	public PhysicsObject(ICollider collider, Vector3f velocity, float mass) {
		this.velocity = velocity;
		this.collider = collider;
		this.mass = mass;
	}

	public float getFriction() {
		return friction;
	}

	public void setFriction(float friction) {
		this.friction = friction;
	}

	public float getMass() {
		return mass;
	}

	public void updatPosition(Vector3f Position) {
		collider.updateCenter(Position);
	}

	public Vector3f getPosition() {
		return collider.getCenter();
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}

	public ICollider getCollider() {
		return collider;
	}

	public IntersectData getData() {
		return data;
	}

	public void setData(IntersectData data) {
		this.data = data;
	}

	public void Integrate(float delta) {
		Vector3f temp = new Vector3f();
		temp.x = velocity.x * delta;
		temp.y = velocity.y * delta;
		temp.z = velocity.z * delta;
		velocity.x = SlowDown(velocity.x);
		velocity.y = SlowDown(velocity.y);
		velocity.z = SlowDown(velocity.z);
		this.collider.updateCenter(Vector3f.add(collider.getCenter(), temp, null));
	}

	private float SlowDown(float speed) {
		float newspeed = 0;
		if (speed > 0) {
			newspeed = speed - friction;
		} else if (speed < 0) {
			newspeed = speed + friction;
		}
		if ((newspeed < 0.05 && newspeed > 0) || (newspeed > -0.05 && newspeed < 0)) {
			return 0;
		}
		return newspeed;

	}

}
