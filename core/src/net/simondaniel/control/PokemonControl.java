package net.simondaniel.control;

import com.badlogic.gdx.math.Vector2;

import net.simondaniel.entities.Entity;

public abstract class PokemonControl extends Control{


	public PokemonControl(Entity e) {
		super(e);
		//this.net = net;
	}
	public abstract void walkTo(Vector2 dest);
	public abstract void skill1();
	public abstract void skill2();
}
