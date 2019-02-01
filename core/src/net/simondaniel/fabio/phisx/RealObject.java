package net.simondaniel.fabio.phisx;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class RealObject implements PhysicObject{

	public Body b2dBody;
	
	protected RealObject(Body body) {
		this.b2dBody = body;
	}


	@Override
	public void createFixture(FixtureDef def) {
		b2dBody.createFixture(def);
	}


	@Override
	public float getX() {
		return b2dBody.getPosition().x;
	}


	@Override
	public float getY() {
		return b2dBody.getPosition().y;
	}

}
