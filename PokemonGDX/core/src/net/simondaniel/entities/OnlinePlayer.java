package net.simondaniel.entities;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import net.simondaniel.entities.EntityInformation.OnlinePlayerInfo;
import net.simondaniel.pokes.PokeDef;
import net.simondaniel.pokes.PokemonInstance;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class OnlinePlayer extends Entity {
	
	private OnlinePlayerInfo info = new OnlinePlayerInfo();
	
	String name;
	
	PokemonInstance poke;
	
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
		def.fixedRotation = true;
		def.type = BodyType.DynamicBody;
		this.setBody(w.createBody(def));
		CircleShape cs = new CircleShape();
		cs.setRadius(info.radius);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = cs;
		fdef.restitution = 0;
		body.createFixture(fdef);
		//poke = new PokemonInstance();
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
	}
	
	@Override
	public OnlinePlayerInfo getInfo() {
		
		info.name = name;
		info.id = id;
		info.x = body.getPosition().x;
		info.y = body.getPosition().y;
		info.radius = 1;
		
		return info;
	}

	public void enableDraw() {
		setDrawer(new TestDrawer(this));
	}
}
