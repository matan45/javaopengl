package Physics;

import java.util.ArrayList;
import java.util.List;

import maths.Vector3f;

public class PhysicsEngine {
	List<PhysicsObject> objects;
	float gravite = 0.0f;

	public PhysicsEngine() {
		objects = new ArrayList<>();
	}

	public void AddObject(PhysicsObject object) {
		objects.add(object);
	}

	public PhysicsObject getObject(int index) {
		return objects.get(index);
	}

	public PhysicsObject RemoveObject(int index) {
		return objects.remove(index);
	}

	public float getGravite() {
		return gravite;
	}

	public void setGravite(float gravite) {
		this.gravite = gravite;
	}

	public void HandleCollisions() {
		for (int i = 0; i < objects.size(); i++) {
			for (int j = i + 1; j < objects.size(); j++) {
				IntersectData data = objects.get(i).getCollider().intersect(objects.get(j).getCollider());
				if (data.isDoesIntersect()) {
					objects.get(i).setData(data);
					objects.get(j).setData(data);
					CollisionResponse(objects.get(i), objects.get(j));

				} else {
					objects.get(i).setData(data);
					objects.get(j).setData(data);

				}

			}
		}
	}

	public void Simulate(float delta) {
		for (PhysicsObject object : objects) {
			object.Integrate(delta);
		}
	}

	private void CollisionResponse(PhysicsObject p1, PhysicsObject p2) {
		float totalmass = p1.getMass() + p2.getMass();
		Vector3f momentum1 = new Vector3f();
		momentum1.x = p1.getVelocity().x * p1.getMass();
		momentum1.y = p1.getVelocity().y * p1.getMass();
		momentum1.z = p1.getVelocity().z * p1.getMass();
		Vector3f momentum2 = new Vector3f();
		momentum2.x = p2.getVelocity().x * p2.getMass();
		momentum2.y = p2.getVelocity().y * p2.getMass();
		momentum2.z = p2.getVelocity().z * p2.getMass();
		Vector3f vf = new Vector3f();
		Vector3f.sub(momentum1, momentum2, vf);
		vf.x = vf.x / totalmass;
		vf.y = vf.y / totalmass;
		vf.z = vf.z / totalmass;
		vf.normalise();
		Vector3f vf1=new Vector3f(vf); 
		Vector3f vf2=new Vector3f(vf); 
		p1.setVelocity(p1.getVelocity().reflected(vf1));
		p2.setVelocity(p2.getVelocity().reflected(vf2));
	}

}
