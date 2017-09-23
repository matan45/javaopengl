package Physics;

import maths.Vector3f;

public class PhysicsObject {
	Vector3f velocity;
	ICollider collider;

	public PhysicsObject(ICollider collider, Vector3f velocity) {

		this.velocity = velocity;
		this.collider = collider;

	}
	public void updatPosition(Vector3f Position){
		collider.updateCenter(Position);
	}
	
	public Vector3f getPosition(){
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

	public void Integrate(float delta) {
		Vector3f temp = new Vector3f();
		temp.x = velocity.x * delta;
		temp.y = velocity.y * delta;
		temp.z = velocity.z * delta;
		this.collider.updateCenter(Vector3f.add(collider.getCenter(), temp,null ));
	}

}
