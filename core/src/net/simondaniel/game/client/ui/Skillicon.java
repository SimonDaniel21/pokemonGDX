package net.simondaniel.game.client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.simondaniel.MyRandom;
import net.simondaniel.screens.MainMenuScreen;

public class Skillicon {

	
	private static final int ICON_WIDTH = 64, BORDER = 2;
	public static final int WIDTH = ICON_WIDTH + BORDER*2;
	private static TextureRegion icon_active, icon_cd;
	private TextureRegion tex;
	int x, y;
	private String text = "Hydropumpe";
	BitmapFont font = new BitmapFont(Gdx.files.internal("font/small_letters_font.fnt"));
	
	boolean active;
	
	public Skillicon(TextureRegion tex, int x, int y) {
		this.tex = tex;
		this.x = x;
		this.y = y;
		
		active = MyRandom.random.nextBoolean();
		
		if(icon_active == null) {
			icon_active = new TextureRegion(new Texture("gfx/skill_ready.png"));
		}
		if(icon_cd == null) {
			icon_cd = new TextureRegion(new Texture("gfx/skill_cd.png"));
		}
		font.getData().setScale(3);
		font.setColor(Color.RED);
	}
	
	public void render(SpriteBatch sb) {
		
		if(active) {
			sb.draw(tex, x+2, y+2);
			sb.draw(icon_active, x, y);
		}
		else {
			sb.setColor(Color.GRAY);
			sb.draw(tex, x+2, y+2);
			sb.setColor(Color.WHITE);
			font.draw(sb, "15", x+ 5, y + 30);
			sb.draw(icon_cd, x, y);
		}
		
		int mx = Gdx.input.getX() - MainMenuScreen.WIDTH/2;
		int my = - (Gdx.input.getY() - MainMenuScreen.HEIGHT/2);
		
		if(mx  < x + WIDTH  && mx >= x && my < y + WIDTH && my >= y) {
			font.draw(sb, text, x, y + 100);
		}
	}

	public void render(ShapeRenderer sr) {
		
	}
	
}
