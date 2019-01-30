package net.simondaniel.control;

import com.badlogic.ashley.core.Entity;

public abstract class PlayerController<T extends Control> extends Controller<T>{

	public PlayerController(T control, Entity e) {
		super(control);
	}
	
	
	
}
