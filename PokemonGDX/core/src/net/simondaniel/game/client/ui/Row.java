package net.simondaniel.game.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.simondaniel.game.client.battle.input.BattleInput;

public class Row extends UIitem{

	private List<UIitem> content;
	
	public Row(int x, int y) {
		this.x = x;
		this.y = y;
		content = new ArrayList<UIitem>();
	}
	
	public void add(UIitem i) {
		i.setPosition(x + w, y);
		w += i.getWidht();
		if(i.getHeight() > h) {
			setHeight(i.getHeight());
		}
	
		content.add(i);
	}
	
	private void setHeight(int h) {
		this.h = h;
	}
	
	@Override
	public void handle(BattleInput input) {
		for(UIitem i : content) {
			i.handle(input);
		}
	}
	
	@Override
	public void render(SpriteBatch sb) {
		for(UIitem i : content) {
			i.render(sb);
		}
	}

	@Override
	public void render(ShapeRenderer sr, Color c) {
		
		for(UIitem i : content) {
			sr.setColor(Color.ORANGE);
			sr.rect(i.x, i.y, i.w, i.h);
		}
		
		sr.setColor(c);
		sr.rect(x, y, w, h);
	}
	
	@Override
	public void setPosition(int x, int y) {
		int dx = x - this.x;
		int dy = y - this.y;
		for(UIitem i : content) {
			i.setPosition(i.x + dx, i.y + dy);
		}
		this.x = x;
		this.y = y;
	}

}
