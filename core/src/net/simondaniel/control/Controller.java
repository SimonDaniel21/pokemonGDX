package net.simondaniel.control;

import com.badlogic.ashley.core.Entity;

import net.simondaniel.network.server.Response.MoveToS;

public abstract class Controller<T extends Control>{

	protected T controll_ifc;
	public Controller(T control){
		this.controll_ifc = control;
	}
	
	public abstract void control();
	
	public void process(Entity e) {
	}

	public abstract void handle(MoveToS p);

}
