package actions.pokemon;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import actions.Action;

public class LinInterpolateVecAction implements Action{
	
	private Vector2 v;
	private float xStart, yStart, xDest, yDest,
		elapsedTime, maxTime;
	
	private boolean finished = false;

	public LinInterpolateVecAction(Vector2 v, float xDest, float yDest, float speed) {
		if(speed <= 0)
			throw new IllegalArgumentException("speed is not allowed to be 0 or negative");
		this.v = v;
		this.xStart = v.x;
		this.yStart = v.y;
		this.xDest = xDest;
		this.yDest = yDest;
		float x2 = (xDest - xStart)*(xDest - xStart);
		float y2 = (yDest - yStart)*(yDest - yStart);
		float dist = (float) Math.sqrt(x2 + y2);
		maxTime = dist / speed;
	}

	@Override
	public void update(float delta) {
		elapsedTime += delta;
		float a =  elapsedTime / maxTime;
		//System.out.println(a);
		float x = Interpolation.linear.apply(xStart, xDest, a);
		float y = Interpolation.linear.apply(yStart, yDest, a);
		v.set(x, y);
		finished = a >= 1;
		if(finished) {
			v.set(xDest, yDest);
		}
	}

	@Override
	public boolean isFinished() {
		return finished;
	}
}
