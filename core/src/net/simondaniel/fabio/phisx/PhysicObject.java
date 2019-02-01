package net.simondaniel.fabio.phisx;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public interface PhysicObject {

	public void createFixture(FixtureDef def);
	
	public float getX();
	
	public float getY();
}
