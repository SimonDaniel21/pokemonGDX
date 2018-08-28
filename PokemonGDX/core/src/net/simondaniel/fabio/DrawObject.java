package net.simondaniel.fabio;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface DrawObject {

	public void render(SpriteBatch sb);
	public void render(ShapeRenderer sr);
	
}
