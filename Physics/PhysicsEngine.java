package Physics;

import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine {
	List<PhysicsObject> objects;

	public PhysicsEngine() {
		objects = new ArrayList<>();
	}

	public void AddObject(PhysicsObject object) {
		objects.add(object);
	}

	public PhysicsObject getObject(int index) {
		return objects.get(index);
	}

	public void HandleCollisions() {
		for (int i = 0; i < objects.size(); i++) {
			for (int j = i + 1; j < objects.size(); j++) {
				IntersectData data = objects.get(i).getCollider().intersect(objects.get(j).getCollider());
				if (data.isDoesIntersect()) {
					System.out.println("Boom "+data.getDistance());
				}
			}
		}
	}

	public void Simulate(float delta) {
		for (PhysicsObject object : objects) {
			object.Integrate(delta);
		}
	}

}
