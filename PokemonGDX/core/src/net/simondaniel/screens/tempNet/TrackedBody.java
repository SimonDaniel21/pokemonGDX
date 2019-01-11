package net.simondaniel.screens.tempNet;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class TrackedBody {
	Body b;

	boolean inactive;

	Vector2 lastPos;

	float timer = 0,  delay = 0.5f;

	public TrackedBody(Body b) {
		this.b = b;
		inactive = true;
		lastPos = new Vector2(b.getPosition());
	}

	public void sync(float delta) {
		Vector2 newPos = b.getPosition();
		//System.out.println(newPos);

		if (inactive) {
			if (!lastPos.equals(newPos)) {
				inactive = false;
			}
			else {
				lastPos.set(newPos);
				timer = 0;
			}
			
		} else {
			timer += delta;
			if(timer >= delay) {
				inactive = true;
				notify(newPos, timer);
				timer -= delay;
				lastPos.set(newPos);
			}
		}
	}
	
	private void notify(Vector2 newPos, float duration) {
		System.err.println("moving" + newPos + "  for " + duration);
	}
}
