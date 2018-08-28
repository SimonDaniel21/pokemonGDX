package net.simondaniel.entities;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.simondaniel.game.client.DrawableObject;

public class TestDrawer extends DrawableObject<OnlinePlayer>{

	Texture t;
	BitmapFont f;
	
	String displayName;
	
	public TestDrawer(OnlinePlayer p) {
		t = new Texture("gfx/hero.png");
		f = new BitmapFont();
		this.displayName = p.name;
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.draw(t, 0, 0);
		f.draw(sb, displayName, 0, 20);
	}
}
