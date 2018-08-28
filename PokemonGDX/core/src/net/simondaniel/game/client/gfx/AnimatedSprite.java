package net.simondaniel.game.client.gfx;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * erweitert ein Sprite um Animationen
 * @author simon
 *
 */
public class AnimatedSprite extends Sprite {
	
	float elapsedTime;
	Animation<TextureRegion> currentAnimation;
	Animation<TextureRegion>[] animations;

	private boolean halted;
	private int haltAt = -1;

	@SuppressWarnings("unchecked")
	public AnimatedSprite(TextureAtlas textureAtlas, String[] animNames) {
		
		this.animations = new Animation[animNames.length];

		for (int i = 0; i < animNames.length; i++) {
			Animation<TextureRegion> animation = new Animation<TextureRegion>(1f / 3f, textureAtlas.findRegions(animNames[i]));
			animation.setPlayMode(PlayMode.LOOP_PINGPONG);
			animation.setFrameDuration(0.3f);
			this.animations[i] = animation;
		}
		currentAnimation = this.animations[0];
		this.setRegion(currentAnimation.getKeyFrames()[0]);
		this.setSize(getRegionWidth(), getRegionHeight());
		this.setOriginCenter();
	}

	public void update(float delta) {
		if (!halted) {
			elapsedTime += delta;
			this.setRegion(currentAnimation.getKeyFrame(elapsedTime));
		
			if (currentAnimation.getKeyFrameIndex(elapsedTime) == haltAt) {
				halted = true;
			}
		}

	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x - getOriginX(), y - getOriginY());
	}
	
	public void setPosition(Vector2 position) {
		setPosition(position.x, position.y);
	}
	
	public void drawCentered(SpriteBatch sb) {
		sb.draw(getTexture(), getX() - getOriginX(), getY() - getOriginY(),
			    getOriginX(), getOriginY(), getWidth(), getHeight(), 1.0f, 1.0f,
			    0, getRegionX(), getRegionY(),
			    getRegionWidth(), getRegionHeight(), false, false
			);
	}

	public void runAnimation(int i) {
		if (i < 0 || i >= animations.length)
			return;
		elapsedTime = 0;
		haltAt = -1;
		halted = false;
		currentAnimation = animations[i];
	}

	public void haltAnimation() {
		halted = true;
	}

	public void haltAnimation(int frame) {
		haltAt = frame;
	}

	public void drawOutline(ShapeRenderer sr) {
		
		sr.rect(getX(), getY(), getWidth(), getHeight());
	}
}