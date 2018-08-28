package net.simondaniel.game.client;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.simondaniel.entities.Entity;

public abstract class DrawableObject<T extends Entity> {

		public abstract void render(SpriteBatch sb);

}
