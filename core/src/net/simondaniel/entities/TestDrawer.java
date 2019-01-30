package net.simondaniel.entities;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import net.simondaniel.game.client.DrawableObject;
import net.simondaniel.game.client.GameInstance;
import net.simondaniel.game.client.gfx.PokemonAnimation;
import net.simondaniel.pokes.Pokemon;

public class TestDrawer extends DrawableObject<OnlinePlayer>{

	BitmapFont f;
	PokemonAnimation anim;
	
	String displayName;
	OnlinePlayer p;
	Vector2 pos;
	
	float w2,h2;
	
	public TestDrawer(OnlinePlayer p) {
		//anim = new PokemonAnimation(Pokemon.squirtle);
		f = new BitmapFont();
		this.displayName = p.getName();
		this.p = p;
		//anim.setScale(3.0f);
		pos = p.body.getPosition();
	}

	@Override
	public void render(SpriteBatch sb) {
		float x = pos.x*GameInstance.PIXELS_PER_METER-w2;
		float y = pos.y*GameInstance.PIXELS_PER_METER-h2;
		f.draw(sb, displayName, x-w2, y+h2+20);
		//anim.draw(sb, pos.x*GameInstance.PIXELS_PER_METER, pos.y*GameInstance.PIXELS_PER_METER);
	}
}
