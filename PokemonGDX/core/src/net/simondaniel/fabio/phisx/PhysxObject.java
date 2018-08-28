package net.simondaniel.fabio.phisx;

import java.awt.Point;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class PhysxObject {
	
	private final PhysxWorld world;
	
	private boolean applyGravity;

	public PhysxObject(PhysxWorld world, Vector2 pos) {
		vel = new Vector2();
		this.pos = pos;
//		this.box = box;
//		box.updatePosition(pos);
		applyGravity = false;
		this.world = world;
		world.addObject(this);
	}
	
	public Vector2 pos;
	public Vector2 vel;
	//public Hitbox box;
	
	public void drawDebug(ShapeRenderer sr) {
		//box.drawOutline(sr);
		sr.rectLine(pos.x, pos.y, pos.x + vel.x*1, pos.y + vel.y*1, 2, Color.GRAY, Color.GREEN);
	}
	
	public void update(float delta) {
		pos.mulAdd(vel, delta);
		
		applyGravity = true;
		for(PhysxObject o : world.getObjects()) {
//			if (box.collides(o.box)) {
//				System.out.println("COLLISION");
//				stopFalling();
			}
		}
		
//		if(applyGravity)
//			applyGravity(delta);
	
	
	private void stopFalling(){
		applyGravity = false;
		vel.y = 0;
	}
	
	private void applyGravity(float delta) {
		vel.y += world.gravity * delta;
		vel.y = Math.max(world.maxFallspeed, vel.y);
	}
	
	public void setGravity(boolean g) {
		applyGravity = g;
	}
}
