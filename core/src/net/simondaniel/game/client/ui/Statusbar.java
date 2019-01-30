package net.simondaniel.game.client.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.simondaniel.MyRandom;
import net.simondaniel.entities.Drawer;

public class Statusbar implements Drawer{

	private static TextureRegion barTex;
	private int x, y, hx, hy, ex, ey;
	private TextureRegion playerIcon, healthbar, expbar;
	
	private static final int HBAR_XOFF = 61, HBAR_YOFF = 43, EXP_XOFF = 77, EXP_YOFF = 21;
	
	public Statusbar(int x, int y) {
		
		setPosition(x, y);
		
		playerIcon = new TextureRegion(new Texture("gfx/pikachu_icon.png"));
		
		healthbar = new TextureRegion(new Texture("gfx/healthbar.png"), 190, 38);
		expbar = new TextureRegion(new Texture("gfx/expbar.png"), (int) (312*0.75), 11);
		
		if(barTex == null) {
			barTex = new TextureRegion(new Texture("gfx/statusbar.png"));
		}
	}
	
	@Override
	public void render(SpriteBatch sb) {
		sb.draw(playerIcon, x, y);
		sb.draw(barTex, x, y);
		sb.draw(healthbar, hx, hy);
		sb.draw(expbar, ex, ey);
	}

	@Override
	public void render(ShapeRenderer sr) {
		// TODO Auto-generated method stub
		
	}
	
	private void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.hx = x + HBAR_XOFF;
		this.hy = y + HBAR_YOFF;
		this.ex = x + EXP_XOFF;
		this.ey = y + EXP_YOFF;
	}
}
