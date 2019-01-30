package net.simondaniel.game.client.battle;

import com.badlogic.gdx.math.MathUtils;

public class Position{
	public int x, y;
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public float distance() {
		return distance(0,0);
	}
	
	public float distance(Position otherPos) {
		return distance(otherPos.x, otherPos.y);
	}
	
	public float distance(int x, int y) {
		int dx = x - this.x;
		int dy = y - this.y;
		return (float) Math.sqrt(dx*dx + dy*dy);
	}

	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public float angle() {
		float angle = (float)Math.atan2(y, x) * MathUtils.radiansToDegrees;
		if (angle < 0) angle += 360;
		return angle;
	}
	
	
	public float angleTo(int x, int y) {
		int dx = x - this.x;
		int dy = y - this.y;
		float angle = (float)Math.atan2(dy, dx) * MathUtils.radiansToDegrees;
		if (angle < 0) angle += 360;
		return angle;
	}

	public float angleTo(Position dest) {
		return angleTo(dest.x, dest.y);
	}
	
	public float angleFrom(int x, int y) {
		int dx = this.x - x;
		int dy = this.y - y;
		float angle = (float)Math.atan2(dy, dx) * MathUtils.radiansToDegrees;
		if (angle < 0) angle += 360;
		return angle;
	}
	
	public Position to(float direction, int length) {
		int xn = 0, yn = 0;
		
		return new Position(xn, yn);
	}
	
}