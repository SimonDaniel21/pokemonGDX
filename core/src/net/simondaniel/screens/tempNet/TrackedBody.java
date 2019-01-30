package net.simondaniel.screens.tempNet;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class TrackedBody {
	
	int trackID;
	
	Body b;

	boolean inactive;

	Vector2 lastPos;


	public TrackedBody(Body b, int trackID) {
		this.b = b;
		inactive = true;
		lastPos = new Vector2(b.getPosition());
	}

	public void sync(float delta) {
		Vector2 newPos = b.getPosition();
		if(!lastPos.equals(newPos)) {
			System.err.println("body moved to " + newPos);
			lastPos.set(newPos);
		}
		else {
			System.out.println("body didnt move");
		}
	}

}
