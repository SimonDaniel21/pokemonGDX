package net.simondaniel.game.client.attacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.simondaniel.MyInterpolation;
import net.simondaniel.game.client.battle.Pikachu;
import net.simondaniel.game.client.battle.Position;
import net.simondaniel.game.client.gfx.AnimationType.AnimationDirection;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Fireblast extends Attack{

	Animation<TextureRegion> fireblast;
	Animation<TextureRegion> explosion;
	Animation<TextureRegion> current;
	float elapsed;
	float dir;
	int range = 10;
	int speed = 1;
	
	//Entity shooter;
	
	Position start, dest;
	float maxFlyTime = 4.0f;
	
	public Fireblast(float dir, int x, int y) {
		super(x, y);
		start = new Position(this.x, this.y);
		dest = new Position(x, y);
		this.dir = dir;
		TextureAtlas a = new TextureAtlas(Gdx.files.internal("gfx/atlases/fireblast/fireblast.atlas"));
		fireblast = new Animation<TextureRegion>(0.1f, a.findRegions("fireblast"));
		fireblast.setPlayMode(PlayMode.LOOP);
		explosion = new Animation<TextureRegion>(0.1f, a.findRegions("fireblast_explosion"));
		explosion.setPlayMode(PlayMode.NORMAL);
		current = fireblast;
	}

	public void update(float delta) {
		elapsed += delta;
		if(!exploded){
			x = MyInterpolation.linear(elapsed / maxFlyTime, start.x, dest.x);
			y = MyInterpolation.linear(elapsed / maxFlyTime, start.y, dest.y);
			if(elapsed >= maxFlyTime) {
				explode();
			}
		}
		else{
			if(explosion.isAnimationFinished(elapsed)){
				//kill();
			}
		}
	}

	public void render(SpriteBatch sb) {
//		if(!isAlive()) return;
//		TextureRegion frame = current.getKeyFrame(elapsed);
//		sb.draw(frame, getScreenX() - (frame.getRegionWidth() >> 1) +8, getScreenY() - (frame.getRegionHeight() >> 1) + 8);
	}
	
	public void render(ShapeRenderer sr) {
		sr.setColor(Color.ORANGE);
		//sr.rect(getScreenX() + 2, getScreenY() + 2, 14, 14);
	}
	
	private void explode(){
		exploded = true;
		elapsed = 0;
		current = explosion;
	}
	boolean exploded = false;
	
	
}
