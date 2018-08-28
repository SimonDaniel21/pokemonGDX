package net.simondaniel.game.server;

import net.simondaniel.game.client.GameObject;

public abstract class EntityS implements GameObjectS{
	
	protected int x, y;
	
	@Override
	public int getX(){
		return x;
	}
	@Override
	public int getY(){
		return y;
	}
	
	public void move() {
		
	}
}
