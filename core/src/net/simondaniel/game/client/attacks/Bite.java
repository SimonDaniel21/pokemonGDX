package net.simondaniel.game.client.attacks;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;

public class Bite extends Attack{

	private Animation<TextureRegion>[] animations;
	private Animation<TextureRegion> animation;
	float elapsed;
	
	AnimationDirection dir;
	@SuppressWarnings("unchecked")
	public Bite(int x, int y, AnimationDirection dir) {
		super(x , y );
		this.dir = dir;
		TextureAtlas a = new TextureAtlas(Gdx.files.internal("gfx/atlases/bite/bite.atlas"));
		animations = new Animation[AnimationDirection.values().length];
		for(AnimationDirection d : AnimationDirection.values()){
			animations[d.ordinal()] = new Animation<TextureRegion>(0.4f, a.findRegions("bite_" + d.name));
			System.err.println("putting " + d.name + " @" + d.ordinal());
		}
		animation = animations[this.dir.ordinal()];
	}


	public void render(SpriteBatch sb) {
		//sb.draw(animation.getKeyFrame(elapsed), 80, 80);
		TextureRegion frame = animation.getKeyFrame(elapsed);
		sb.draw(frame, 666 - (frame.getRegionWidth() >> 1) +8, 666 - (frame.getRegionHeight() >> 1) + 8);
	}


	public void update(float delta) {
		elapsed += delta;
		if(animation.isAnimationFinished(elapsed)){
			//kill();
		}
	}

	public void render(ShapeRenderer sr) {
		// TODO Auto-generated method stub
		
	}

}
