package net.simondaniel.entities;

public abstract class EntityInformation {
	public int id;
	
	public static class OnlinePlayerInfo extends EntityInformation{
		public String name;
		public float x,y;
		public float radius;
	}

	
}