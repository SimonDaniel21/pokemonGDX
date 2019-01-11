package net.simondaniel.game.client.gfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import net.simondaniel.Assets;
import net.simondaniel.game.client.gfx.AnimationLayout.AnimationNotSupportedException;
import net.simondaniel.game.client.gfx.AnimationLayout.PokemonAnimationLayout;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;
import net.simondaniel.pokes.Pokemon;

public class PokemonAnimation{

	private Body body;
	float xOffA, yOffA = 7,
	scaleX = 1, scaleY = 1;
	AnimatedSprite animation;
	Sprite temp;
	AnimationLayout layout;

	public PokemonAnimation(Pokemon p, Body body) {

		this.body = body;
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

	private void updatePositions() {
		float x = body.getPosition().x;
		float y = body.getPosition().y;
		temp.setOriginBasedPosition(x, y);
		animation.setPosition(x + xOffA*scaleX, y + yOffA*scaleY);
		
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


}
