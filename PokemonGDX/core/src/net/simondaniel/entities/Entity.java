package net.simondaniel.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import net.simondaniel.control.Controller;
import net.simondaniel.entities.EntityInformation.OnlinePlayerInfo;
import net.simondaniel.fabio.DrawObject;
import net.simondaniel.game.client.DrawableObject;
import net.simondaniel.game.server.GameObjectS;

public abstract class Entity{

	public int id;
	
	private boolean dead = true;
	
	
	// COMPONENTS all may be null
	protected Body body; 										// phyisc logic
	private DrawableObject<? extends Entity> draw;	// visuals
	private Controller<?> control; 					// i/o interfaces
	
	public Entity(EntityInformation info){
		this.id = info.id;
	}
	
	public Entity() {
		this.id = -1;
	}
	
	public void control() {
		control.control();
	}
	
	public void draw(SpriteBatch sb) {
		draw.render(sb);
	}
	
	public boolean isDrawable() {
		return (draw != null);
	}
	
	public void activate(int id) {
		this.id = id;
		dead = false;
	}
	
	public void setBody(Body b) {
		this.body = b;
	}
	
	public void setDrawer(DrawableObject<? extends Entity> draw) {
		this.draw = draw;
	}
	
	public abstract EntityInformation getInfo();

	public static Entity createFromInfo(World w, EntityInformation info) {
		
		if(info instanceof OnlinePlayerInfo) {
			OnlinePlayerInfo i = (OnlinePlayerInfo) info;
			return new OnlinePlayer(w, i);
		}
		return null;
	}
	
	public Body body() {
		return body;
	}
	
	public void update(float delta) {
		body.setLinearVelocity(0, 0);
		if(dest != null) {
			float dist = body.getPosition().dst(dest);
			if(lastdist != -1 && lastdist - dist < 0.0001f) {
				dest = null;
				lastdist = -1;
				return;
			}
			lastdist = dist;
			//System.out.println("trying to move to " + dest);
			Vector2 v = dest.cpy().sub(body.getPosition());
			v.nor();
			v.scl(4);
			body.setLinearVelocity(v);
		}
		
	}
	
	Vector2 dest;
	float lastdist = -1;
	
	public void moveTo(float x, float y) {
		dest = new Vector2(x, y);
		System.out.println("move to " + x + " . " + y);
	}
}