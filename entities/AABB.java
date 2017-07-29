package entities;

import maths.Vector3f;

//need to check it
public class AABB {
	static final float DIVISION = 2.5f;

	public static boolean collides(Entity a, Entity b) {
		Vector3f ea = a.getPosition();
		Vector3f eb = b.getPosition();
		if (Math.abs(ea.x - eb.x) < (a.getX() + b.getX()) / DIVISION)
			if (Math.abs(ea.y - eb.y) < (a.getY() + b.getY()) / DIVISION)
				if (Math.abs(ea.z - eb.z) < (a.getZ() + b.getZ()) / DIVISION)
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

	public static boolean insidepicker(Entity entity, Vector3f point) {
		Vector3f a = entity.getPosition();
		if (Math.abs(a.x - point.x) <= entity.getX() / DIVISION)
			if (Math.abs(a.z - point.z) <= entity.getZ() / DIVISION)
				return true;

		return false;
	}

}
