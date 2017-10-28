package utill;

public class MeasureTime {
	static long starttime = 0;
	static long endtime = 0;

	public static void starttime() {
		starttime = (System.currentTimeMillis() / 1000) % 3600;
	}

	public static void endtime() {
		endtime = (System.currentTimeMillis() / 1000) % 3600;
		System.out.println((3600 + endtime - starttime) % 3600);
		reset();
	}

	private static void reset() {
		starttime = 0;
		endtime = 0;
	}
}
