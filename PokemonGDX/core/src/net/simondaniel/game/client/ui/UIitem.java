package net.simondaniel.game.client.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.simondaniel.entities.Drawer;
import net.simondaniel.fabio.input.BattleInput;
import net.simondaniel.fabio.input.InputHandler;

public abstract class UIitem implements Drawer, InputHandler<BattleInput>{

	protected int x, y, w, h;
	
	public UIitem() {
		x = 0;
		y = 0;
	}
	
	public UIitem(int x, int y) {
		setPosition(x, y);
	}
	
	protected void setBounds(int w, int h) {
		this.w = w;
		this.h= h;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getWidht() {
		return w;
	}
	public int getHeight() {
		return h;
	}
	
	public void render(ShapeRenderer sr, Color c) {
		sr.setColor(c);
		sr.rect(x, y, w, h);
		
	}
	
	@Override
	public void render(ShapeRenderer sr) {
		sr.setColor(Color.DARK_GRAY);
		sr.rect(x, y, w, h);
		
	}
}
