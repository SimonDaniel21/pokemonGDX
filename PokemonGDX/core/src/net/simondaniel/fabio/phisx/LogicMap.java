package net.simondaniel.fabio.phisx;

public class LogicMap {

	private final int[] collisionTiles;
	private final int width, height;
	
	public LogicMap(int width, int height) {
		this.width = width;
		this.height = height;
		this.collisionTiles = new int[width*height];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int[] getData() {
		return collisionTiles;
	}
}
