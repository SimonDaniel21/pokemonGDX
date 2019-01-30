package net.simondaniel.game.client.gfx;

import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * erweitert ein Sprite um Animationen
 * @author simon
 *
 */
public class AnimatedSprite extends Sprite {
	
	float elapsedTime;
	Animation<TextureRegion> currentAnimation;
	HashMap<String, Animation<TextureRegion>> animations;

	private boolean halted;
	private int haltAt = -1;

	@SuppressWarnings("unchecked")
	public AnimatedSprite(TextureAtlas textureAtlas) {
		
		ArrayList<String> animNames = new ArrayList<String>();
		
		int uniformWidth = -1, uniformHeight = -1;
		
		for(AtlasRegion r : textureAtlas.getRegions()) {
			
			if(uniformWidth == -1 && uniformHeight == -1) {
				uniformWidth = r.getRegionWidth();
				uniformHeight = r.getRegionHeight();
			}
			
			if(uniformWidth != r.getRegionWidth() || uniformHeight != r.getRegionHeight()) {
				System.err.println("AN ANIMATED SPRITE LOADED WITH FRAMES OF NOT UNIFORM SIZE!\n"
						+ "should be " + uniformWidth + "x" + uniformHeight);
			}
			
			if(!animNames.contains(r.name))
				animNames.add(r.name);
		}

		animations = new HashMap<String, Animation<TextureRegion>>();

		for (String s : animNames) {
			Animation<TextureRegion> animation = new Animation<TextureRegion>(1f / 3f, textureAtlas.findRegions(s));
			animation.setPlayMode(PlayMode.LOOP_PINGPONG);
			animation.setFrameDuration(0.3f);
			animations.put(s, animation);
		}
		
		if(animNames.isEmpty())
			return;
		currentAnimation = animations.get(animNames.get(0));
		setRegion(currentAnimation.getKeyFrames()[0]);
		setSize(uniformWidth, uniformHeight);
		setOriginCenter();
	}
	
	@SuppressWarnings("unchecked")
	public AnimatedSprite(TextureAtlas textureAtlas, float width, float height) {
		this(textureAtlas);
	
		setSize(width, height);
		setOriginCenter();
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
	
	/**
	 * replaced with orign based position set (always centered)
	 */
	@Override
	public void setPosition(float x, float y) {
		//System.out.println("set position + ox= " + getOriginX());
		super.setPosition(x - getOriginX(), y - getOriginY());
	}
	
	@Override
	public void setOriginBasedPosition(float x, float y) {
		setPosition(x, y);
	}

	public void setPosition(Vector2 position) {
		setPosition(position.x, position.y);
	}
	
//	public void drawCentered(Batch batch) {
//		System.out.println(getOriginX());
//		setOriginBasedPosition(-12.5f, -11.5f);
//		batch.draw(getTexture(), getX() - getOriginX(), getY() - getOriginY(),
//			    getOriginX(), getOriginY(), getWidth(), getHeight(), 1.0f, 1.0f,
//			    0, getRegionX(), getRegionY(),
//			    getRegionWidth(), getRegionHeight(), false, false
//			);
//	}

	public void runAnimation(String s) {
		Animation<TextureRegion> a = animations.get(s);
		if (a == null)
			return;
		elapsedTime = 0;
		haltAt = -1;
		halted = false;
		currentAnimation = a;
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