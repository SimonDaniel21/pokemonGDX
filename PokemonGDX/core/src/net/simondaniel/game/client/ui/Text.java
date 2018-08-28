package net.simondaniel.game.client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.simondaniel.game.client.battle.input.BattleInput;

public class Text extends UIitem{

	private String text;
	
	private static BitmapFont font = new BitmapFont(Gdx.files.internal("font/small_letters_font.fnt"));
	static {
		font.getData().setScale(4);
	}
	
	public Text(String s) {
		this.text = s;
		GlyphLayout layout = new GlyphLayout(font, s);
		w = (int) layout.width;
		h = (int) layout.height;
	}
	
	@Override
	public void handle(BattleInput input) {
		
	}
	
	@Override
	public void render(SpriteBatch sb) {
		font.draw(sb, text, x, y + h);
	}

}
