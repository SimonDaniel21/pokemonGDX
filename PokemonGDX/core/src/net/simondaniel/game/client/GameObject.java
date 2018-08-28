package net.simondaniel.game.client;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameObject {
	public void render(SpriteBatch sb);
	public void update(float delta);
	public int getX();
	public int getY();
	public float getScreenX();
	public float getScreenY();
}
