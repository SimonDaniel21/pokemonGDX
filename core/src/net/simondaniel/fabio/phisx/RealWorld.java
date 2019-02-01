package net.simondaniel.fabio.phisx;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class RealWorld extends PhysicsWorld{

	Box2DDebugRenderer debugRenderer;
	World b2dWorld;
	
	public RealWorld() {
		b2dWorld = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
	}
	
	@Override
	public void update(float delta) {
		
	}

	@Override
	public void addObject(PhysicObject o) {
		
	}

	@Override
	public void removeObject(PhysicObject o) {
		
	}

	BodyDef def = new BodyDef();
	@Override
	public PhysicObject createPhysicsObject(float x, float y) {
		
		
		def.position.set(x, y);
		def.type = BodyType.DynamicBody;
		RealObject o = new RealObject(b2dWorld.createBody(def));

		return o;
	}

	@Override
	public void renderDebug(Matrix4 projMatrix) {
		debugRenderer.render(b2dWorld, projMatrix);
	}

}
