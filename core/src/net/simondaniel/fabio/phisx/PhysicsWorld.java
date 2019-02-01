package net.simondaniel.fabio.phisx;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Matrix4;

public abstract class PhysicsWorld {
	
	//private List<PhysicObject> objects;
	
	public PhysicsWorld() {
		//objects = new ArrayList<PhysicObject>();
	}
	
	public abstract void update(float delta);
	
	public abstract void addObject(PhysicObject o);

	public abstract void removeObject(PhysicObject o);

	public abstract PhysicObject createPhysicsObject(float x, float y);
	
	public abstract void renderDebug(Matrix4 projMatrix);
}
