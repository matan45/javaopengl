package Physics;

import java.util.ArrayList;
import java.util.List;

import Physics.Collider.Layers;
import maths.Vector3f;

public class PhysicsEngine {
	List<PhysicsObject> objects;
	OnOverLaps collision;
	float gravite = 0.0f;

	public PhysicsEngine() {
		objects = new ArrayList<>();
	}
	

	public void setCollision(OnOverLaps collision) {
		this.collision = collision;
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
					if ((!objects.get(i).getVelocity().isZero() || !objects.get(j).getVelocity().isZero())
							&& objects.get(i).getCollider().getLayers() == Layers.Physics_Layer)
						if(collision != null){
							collision.OnCollision(objects.get(i), objects.get(j));
						}
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
		
		Vector3f momentum1 = Momentum(p1);
		Vector3f momentum2 = Momentum(p2);
		
		Vector3f vf = new Vector3f();
		Vector3f.sub(momentum1, momentum2, vf);
		vf.x = vf.x / totalmass;
		vf.y = vf.y / totalmass;
		vf.z = vf.z / totalmass;

		if (!p1.getVelocity().isZero() && !p2.getVelocity().isZero()) {
			MaxVelocity(p1, p2);
		} else {
			reflected(p1, p2, vf);
		}

		if (p1.getVelocity().isZero()) {
			p1.setVelocity(vf);
		} else if (p2.getVelocity().isZero()) {
			p2.setVelocity(vf);
		}

	}

	private void reflected(PhysicsObject p1, PhysicsObject p2, Vector3f direction) {
		Vector3f vf1 = new Vector3f(direction);
		Vector3f vf2 = new Vector3f(direction);
		vf1.normalise();
		vf2.normalise();
		p1.setVelocity(p1.getVelocity().reflected(vf1));
		p2.setVelocity(p2.getVelocity().reflected(vf2));
	}

	private void MaxVelocity(PhysicsObject p1, PhysicsObject p2) {
		Vector3f direction1 = new Vector3f(p1.getVelocity());
		Vector3f direction2 = new Vector3f(p2.getVelocity());
		direction1.normalise();
		direction2.normalise();
		if (direction1.equls(direction2)) {
			p1.setVelocity(p1.getVelocity().Max(p2.getVelocity()));
			p2.setVelocity(p1.getVelocity().Max(p2.getVelocity()));

		}
	}
	
	private Vector3f Momentum(PhysicsObject p){
		Vector3f momentum = new Vector3f();
		momentum.x = p.getVelocity().x * p.getMass();
		momentum.y = p.getVelocity().y * p.getMass();
		momentum.z = p.getVelocity().z * p.getMass();
		return momentum;
	}
	

}
