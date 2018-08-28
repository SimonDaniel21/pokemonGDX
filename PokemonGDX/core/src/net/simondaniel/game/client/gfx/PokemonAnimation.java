package net.simondaniel.game.client.gfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.simondaniel.game.client.gfx.AnimationLayout.AnimationNotSupportedException;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;

public class PokemonAnimation{

	private float x, y;
	private int width, height;
	
	Texture img;
	Animation<TextureRegion> currentAnimation;
	float elapsedTime;
	Animation<TextureRegion>[] animations;
	AnimationLayout layout;
	
	private boolean halted;
	private int haltAt = -1;
	
	Sprite sprite;

	@SuppressWarnings("unchecked")
	public PokemonAnimation(AnimationLayout layout, TextureAtlas textureAtlas) {

		sprite = new Sprite();
		this.layout = layout;
		int types = AnimationType.values().length;
		int directions = AnimationDirection.values().length;
		//ArrayList<Animation<TextureRegion>> list = new ArrayList<Animation<TextureRegion>>();
		this.animations = new Animation[types*directions];
		
		String[] sa = layout.animationNames;
		int l = sa.length;
		for(int i = 0; i < l; i++) {
			for(int d = 0; d < directions; d++){
				
				Animation<TextureRegion> animation = 
						new Animation<TextureRegion>(1f/3f, textureAtlas.findRegions(sa[i] + "_" +
								AnimationDirection.values()[d].name));
				animation.setPlayMode(PlayMode.LOOP);
				animation.setFrameDuration(0.1f);
				//System.err.println("loading animation: " + (sa[i] + "_" +
						//AnimationDirection.values()[d].name) + " with length " + animation.getKeyFrames().length);
				//this.animations.put(s, animation);
				this.animations[i + d*l] = animation;
			}
		}
		width = animations[0].getKeyFrames()[0].getRegionWidth();
		height = animations[0].getKeyFrames()[0].getRegionHeight();
		for(String s : layout.animationNames) {
			
		}
		
		currentAnimation = this.animations[0];
	}
	
	public void update(float delta) {
		if(!halted) {
			elapsedTime += delta;
			if(currentAnimation.getKeyFrameIndex(elapsedTime) == haltAt) {
				halted = true;
			}
		}
			
	}
	
	public void render(SpriteBatch sb) {	
		//sb.draw(currentAnimation.getKeyFrame(elapsedTime), x - (width/2), y - (height/2) );
		
		sprite.setRegion(currentAnimation.getKeyFrame(elapsedTime));
		sprite.setSize(300, 200);
		sprite.setOriginCenter();
		sprite.setOriginBasedPosition(x, y);
		sprite.draw(sb);
	}
	
	public void runAnimation(AnimationType t, AnimationDirection d) {
		elapsedTime = 0;
		haltAt = -1;
		halted = false;
		try {
			currentAnimation = animations[layout.getAnimationIndex(t)  + d.ordinal()*layout.animationNames.length];
		} catch (AnimationNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void haltAnimation() {
		halted = true;
	}
	
	public void haltAnimation(int frame) {
		haltAt = frame;
	}
	
	public int getAnimationWidth(){
		return width;
	}
	
	public int getAnimationHeight(){
		return height;
	}

	public void draw(SpriteBatch sb, float x, float y) {
		update(0.005f);
		this.setPosition(x, y);
		render(sb);
	}

}
