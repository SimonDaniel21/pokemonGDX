package net.simondaniel.screens.tempNet;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class NetworkedWorld {
	
	float timeStep = 1/60f;
	
	float syncTimer = 0,  delay = 1.5f;

	World b2dWorld;
	List<TrackedBody> bodies;
	
	public NetworkedWorld(World b2dWorld) {
		this.b2dWorld = b2dWorld;
		bodies = new ArrayList<TrackedBody>();
	}

	public void addBody(Body b, int trackID) {
		bodies.add(new TrackedBody(b, trackID));
		World w;
		
	}
	
	public Body createTrackedBody(BodyDef def, int trackID) {
		Body b = b2dWorld.createBody(def);
		bodies.add(new TrackedBody(b, trackID));
		return b;
	}
	
	public Body createBody(BodyDef def) {
		return b2dWorld.createBody(def);
	}
	
	public void syncPositions(float delta) {
		System.out.println("syncing " + delta + " - " + bodies.size());
		for(TrackedBody tb : bodies) {
			tb.sync(delta);
		}
	}

	public void step() {
		b2dWorld.step(timeStep, 8, 3);
		syncTimer += timeStep;
		
		//System.out.println("stepping " + syncTimer + ","  + timeStep);
		if(syncTimer >= delay) {
			syncPositions(delay);
			syncTimer -= delay;
		}
	}

	
}
