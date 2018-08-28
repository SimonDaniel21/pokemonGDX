package net.simondaniel.game.client.battle;

import net.simondaniel.game.client.GameObject;
import net.simondaniel.screens.MainMenuScreen;

public abstract class BattleEntity implements GameObject{
	
	protected int x, y;
	private boolean alive = true;
	
	public BattleEntity(){
		this(0,0);
	}
	
	public BattleEntity(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int getX(){
		return x;
	}
	@Override
	public int getY(){
		return y;
	}
	
	public float getScreenX(){
		return 0;
	}
	
	public float getScreenY(){
		return 0;
	}
	
	public void kill(){
		alive = false;
	}
	
	public boolean isAlive() {
		return alive;
	}
}