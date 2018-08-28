package net.simondaniel.game.client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.simondaniel.fabio.input.FabioInput.BattleInputType;
import net.simondaniel.game.client.battle.input.BattleInput;

public abstract class MyButton extends UIitem{

	TextureRegion tex;
	
	public MyButton(TextureRegion tex) {
		this(0,0,tex);
	}
	
	public MyButton(int x, int y, TextureRegion tex) {
		super(x,y);
		this.w = tex.getRegionWidth();
		this.h = tex.getRegionHeight();
		this.tex = tex;
	}
	
	public MyButton() {
		this(0,0);
	}
	
	public MyButton(int w, int h) {
		this(0,0,w,h);
	}
	
	public MyButton(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public abstract void getClicked();
	
	private boolean pressedLast = false;
	
	@Override
	public void handle(BattleInput input) {
		int mx = input.getXpixel();
		int my = input.getYpixel();
		boolean pressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
		if(!pressedLast && pressed
				&& mx  < x + w  && mx >= x && my < y + h && my >= y) {
			getClicked();
		}
		pressedLast = pressed;
	}
	
	protected void setRegion(TextureRegion tex) {
		this.tex = tex;
	}
	
	@Override
	public void render(SpriteBatch sb) {
		sb.draw(tex, x, y);
	}

}
