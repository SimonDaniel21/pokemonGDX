package net.simondaniel.screens;

public abstract class RealGame {
	
	public abstract void receivePacketFrom(Object packet, Object sender);
	
	//prediction interpolization and local events take place here!
	public abstract void update(float delta);
	
	public abstract void render(float delta);
}
