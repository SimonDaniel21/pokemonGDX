package net.simondaniel.game.client.gfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import net.simondaniel.Assets;
import net.simondaniel.fabio.phisx.PhysicObject;
import net.simondaniel.fabio.phisx.PredictedBody;
import net.simondaniel.game.client.gfx.AnimationLayout.AnimationNotSupportedException;
import net.simondaniel.game.client.gfx.AnimationLayout.PokemonAnimationLayout;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;
import net.simondaniel.pokes.Pokemon;
import net.simondaniel.screens.tempNet.NetworkedWorld;

public class PokemonAnimation{

	private PredictedBody body;
	float xOffA, yOffA = 7,
	scaleX = 1, scaleY = 1;
	AnimatedSprite animation;
	Sprite temp;
	AnimationLayout layout;

	public PokemonAnimation(Pokemon p) {

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
		if(body == null) return;
		float x = body.x() * NetworkedWorld.PIXELS_PER_METER;
		float y = body.y() * NetworkedWorld.PIXELS_PER_METER;
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

	public void attachTo(PredictedBody body) {
		this.body = body;
	}

}
