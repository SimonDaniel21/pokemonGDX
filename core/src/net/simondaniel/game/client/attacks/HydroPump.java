package net.simondaniel.game.client.attacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.simondaniel.util.MyInterpolation;
import net.simondaniel.game.client.battle.Position;

public class HydroPump extends Attack{

	Animation<TextureRegion> shoot;
	Animation<TextureRegion> fly;
	Animation<TextureRegion> explosion;
	Animation<TextureRegion> current;
	float elapsed;
	float dir;
	int range = 10;
	int speed = 8;
	
	Position start, dest;
	float maxFlyTime = 3.0f;
	
	public HydroPump(float dir, int x, int y) {
		super(x, y);
		start = new Position(this.x, this.y);
		dest = new Position(x, y);
		this.dir = dir;
		TextureAtlas a = new TextureAtlas(Gdx.files.internal("gfx/atlases/hydro_pump/hydro_pump.atlas"));
		shoot = new Animation<TextureRegion>(0.08f, a.findRegions("hydro_pump"));
		fly = new Animation<TextureRegion>(0.5f, a.findRegions("hydro_pump_fly"));
		explosion = new Animation<TextureRegion>(0.1f, a.findRegions("hydro_pump_hit"));
		fly.setPlayMode(PlayMode.LOOP);;
		explosion.setPlayMode(PlayMode.NORMAL);
		current = shoot;
	}


	public void update(float delta) {
		elapsed += delta;
		if(!exploded){
			if(shoot.isAnimationFinished(elapsed)) {
				current = fly;
			}
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
//		System.out.println("draw hydro!");
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
