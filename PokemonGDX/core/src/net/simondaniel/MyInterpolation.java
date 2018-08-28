package net.simondaniel;

public class MyInterpolation {

	public static int linear(float percentage, int start, int end) {
		percentage = Math.max(0.0f, percentage);
		if(percentage >= 1) return end;
		
		int delta = end - start;
		
		return start + Math.round(delta * percentage);
	}
	
	public static float linear(float percentage, float start, float end) {
		percentage = Math.max(0.0f, percentage);
		if(percentage >= 1) return end;
		
		float delta = end - start;
		
		return start + delta * percentage;
	}
}
