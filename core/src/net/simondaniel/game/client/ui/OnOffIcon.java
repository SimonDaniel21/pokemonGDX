package net.simondaniel.game.client.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.simondaniel.fabio.input.BattleInput;

public class OnOffIcon extends Image{

	private static Texture onoff;
	
	private TextureRegion on, off, active;
	
	public OnOffIcon(boolean online) {
		super();
		if(onoff == null) {
			onoff = new Texture("gfx/onoff.png");
		}
		int h = onoff.getHeight(), w = h;
		
		on = new TextureRegion(onoff, w, h);
		off = new TextureRegion(onoff, w, 0, w, h);
		active = online ? on : off ;
	}
	


}
