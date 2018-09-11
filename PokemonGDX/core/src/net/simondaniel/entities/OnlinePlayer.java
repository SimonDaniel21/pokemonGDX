package net.simondaniel.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import net.simondaniel.entities.EntityInformation.OnlinePlayerInfo;
import net.simondaniel.game.client.DrawableObject;
import net.simondaniel.game.client.gfx.AnimationType;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;
import net.simondaniel.pokes.PokeDef;
import net.simondaniel.pokes.Pokemon;
import net.simondaniel.pokes.PokemonInstance;
import net.simondaniel.pokes.PokemonStats;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class OnlinePlayer extends Entity {
	
	private OnlinePlayerInfo info = new OnlinePlayerInfo();
	
	private String name;
	
	PokemonInstance poke;
	
	public TestDrawer drawer;
	
	public OnlinePlayer(String name, World w, PokeDef pdef) {
		this.name = name;
		BodyDef def = new BodyDef();
		def.type = BodyType.KinematicBody;
		this.setBody(w.createBody(def));
		CircleShape cs = new CircleShape();
		cs.setRadius(1);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = cs;
		body.createFixture(fdef);
		poke = new PokemonInstance(pdef);
	}
	
	public OnlinePlayer(World w, OnlinePlayerInfo info) {
		this.name = info.name;
		BodyDef def = new BodyDef();
		def.position.x = info.x;
		def.position.y = info.y;
		def.fixedRotation = true;
		def.type = BodyType.DynamicBody;
		this.setBody(w.createBody(def));
		CircleShape cs = new CircleShape();
		cs.setRadius(info.radius);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = cs;
		fdef.restitution = 0;
		body.createFixture(fdef);
		PokeDef pdef = new PokeDef();
		pdef.exp = 0;
		pdef.level = 0;
		pdef.pokemon = Pokemon.rayquaza;
		pdef.stats = new PokemonStats();
		poke = new PokemonInstance(pdef);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		body.setLinearVelocity(0, 0);
		if(dest != null) {
			float dist = body.getPosition().dst(dest);
			if(lastdist != -1 && lastdist - dist < 0.0001f) {
				dest = null;
				lastdist = -1;
				drawer.anim.haltAnimation(1);
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
	
	public void startWalk(float degrees) {
		
	}
	
	@Override
	public void moveTo(float x, float y) {
		super.moveTo(x, y);
		Vector2 v = dest.cpy().sub(body.getPosition());
		float angle = v.angle();
		System.out.println("start move angle: " + angle + " " + AnimationDirection.fromAngle(angle));
		drawer.anim.runAnimation(AnimationType.MOVEMENT, AnimationDirection.fromAngle(angle));
	}
	
	@Override
	public OnlinePlayerInfo getInfo() {
		
		System.out.println("BODYX " + body.getPosition().x);
		
		info.name = name;
		info.id = id;
		info.x = body.getPosition().x;
		info.y = body.getPosition().y;
		info.radius = 1;
		
		return info;
	}

	public void enableDraw() {
		drawer = new TestDrawer(this);
		setDrawer(drawer);
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public void setDrawer(DrawableObject<? extends Entity> draw) {
		super.setDrawer(draw);
		drawer =(TestDrawer) draw;
	}
}
