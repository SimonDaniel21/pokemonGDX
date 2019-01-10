package net.simondaniel.game.client.gfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import net.simondaniel.Assets;
import net.simondaniel.game.client.gfx.AnimationLayout.AnimationNotSupportedException;
import net.simondaniel.game.client.gfx.AnimationLayout.PokemonAnimationLayout;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;
import net.simondaniel.pokes.Pokemon;

public class PokemonAnimation{

	private Vector2 pos;
	float xOffA, yOffA = 7,
	scaleX = 1, scaleY = 1;
	AnimatedSprite animation;
	Sprite temp;
	AnimationLayout layout;

	public PokemonAnimation(Pokemon p, Vector2 pos) {

		this.pos = pos;
		animation = new AnimatedSprite(Assets.getPokeAtlas(p));
		layout = AnimationLayout.getLayoutFromIndex(p.layout);
		temp = new Sprite(new Texture("gfx/underglow_orig.png"));

		temp.setOriginCenter();
	}

	public void update(float delta) {
		animation.update(delta);
		updatePositions();
	}
	
	public void draw(Batch batch) {
		temp.draw(batch);
		animation.draw(batch);
	}
	
	public void runAnimation(AnimationType t, AnimationDirection d) {

		try {
			animation.runAnimation(layout.getAnimationName(t, d));
		} catch (AnimationNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public void setPosition(float x, float y) {
		pos.x = x;
		pos.y = y;
		updatePositions();
	}
	
	private void updatePositions() {
		temp.setOriginBasedPosition(pos.x, pos.y);
		animation.setPosition(pos.x + xOffA*scaleX, pos.y + yOffA*scaleY);
		
	}
	
	public void setScale(float scaleXY) {
		scaleX = scaleXY;
		scaleY = scaleXY;
		updateScales();
		updatePositions();
	}
	
	private void updateScales() {
		temp.setScale(scaleX, scaleY);
		animation.setScale(scaleX, scaleY);
		
	}

	public void moveTo(float worldX, float worldY) {
		setPosition(worldX, worldY);
	}

	public float getX() {
		return pos.x;
	}
	
	public float getY() {
		return pos.y;
	}

	public void move(float xd, float yd) {
		setPosition(pos.x + xd, pos.y + yd);
	}
}
