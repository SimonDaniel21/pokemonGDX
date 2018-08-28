package net.simondaniel.fabio.phisx;

import java.util.ArrayList;
import java.util.List;

public class PhysxWorld {
	
	public float gravity = -200f; // pixel accelerated per second
	public float maxFallspeed = -150; //pixelPerSecond
	
	private List<PhysxObject> objects;

	public PhysxWorld() {
		objects = new ArrayList<PhysxObject>();
	}
	
	public void addObject(PhysxObject physxObject) {
		objects.add(physxObject);
	}
	
	public void update(float delta) {
		for(PhysxObject o : objects) {
			o.update(delta);
		}
	}

	public List<PhysxObject> getObjects() {
		
		return objects;
	}
}
