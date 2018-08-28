package net.simondaniel.control;

import com.badlogic.gdx.math.Vector2;

import net.simondaniel.entities.Entity;
import net.simondaniel.network.Network_interface;

public abstract class PokemonControl extends Control{

	protected Network_interface net;
	
	public PokemonControl(Entity e, Network_interface net) {
		super(e);
		this.net = net;
	}
	public abstract void walkTo(Vector2 dest);
	public abstract void skill1();
	public abstract void skill2();
}
