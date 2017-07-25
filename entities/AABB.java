package entities;

import maths.Vector3f;

//need to check it
public class AABB {
	static final float MAX = 2.8f;

	public static boolean collides(Entity a, Entity b) {
		Vector3f ea = a.getPosition();
		Vector3f eb = b.getPosition();
		float offset = MAX - (a.getScale() + b.getScale());
		//System.out.println(offset);
		if (Math.abs(ea.x - eb.x) < (a.getScale() + b.getScale()) * offset)
			if (Math.abs(ea.y - eb.y) < (a.getScale() + b.getScale()) * offset)
				if (Math.abs(ea.z - eb.z) < (a.getScale() + b.getScale()) * offset)
					return true;

		return false;
	}

	// check if point is inside entity
	public static boolean inside(Entity entity, Vector3f point) {
		Vector3f a = entity.getPosition();
		if (Math.abs(a.x - point.x) < entity.getScale())
			if (Math.abs(a.y - point.y) < entity.getScale())
				if (Math.abs(a.z - point.z) < entity.getScale())
					return true;

		return false;
	}

}
