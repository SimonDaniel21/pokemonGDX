package net.simondaniel.screens.tempNet;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class NetworkedWorld {
	
	float timeStep = 1/60;

	World b2dWorld;
	List<TrackedBody> bodies;
	
	public NetworkedWorld(World b2dWorld) {
		this.b2dWorld = b2dWorld;
		bodies = new ArrayList<TrackedBody>();
	}

	public void addBody(Body b) {
		bodies.add(new TrackedBody(b));
		World w;
		
	}
	
	public Body createBody(BodyDef def) {
		Body b = b2dWorld.createBody(def);
		bodies.add(new TrackedBody(b));
		return b;
	}
	
	public void syncPositions(float delta) {
		for(TrackedBody tb : bodies) {
			tb.sync(delta);
		}
	}

	public void step() {
		b2dWorld.step(timeStep, 8, 3);
		syncPositions(timeStep);
	}
}
