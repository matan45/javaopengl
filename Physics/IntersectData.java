package Physics;

public class IntersectData {
	boolean doesIntersect;
	float distance;

	public IntersectData(boolean doesIntersect, float distance) {
		this.doesIntersect = doesIntersect;
		this.distance = distance;
	}

	public boolean isDoesIntersect() {
		return doesIntersect;
	}

	public float getDistance() {
		return distance;
	}

}
