package net.simondaniel.game.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.simondaniel.game.client.battle.input.BattleInput;

public class MyTable extends UIitem{

	public List<Row> content;
	
	private int currentRow = -1;
	
	public MyTable(int x, int y) {
		content = new ArrayList<Row>();
		setPosition(x, y);
		row();
	}
	
	public void addItem(UIitem i) {
		Row r = content.get(currentRow);
		r.add(i);
		if(r.getWidht() > w) {
			w = r.getWidht();
		}
		int nh = 0;
		for(Row row : content) {
			nh += row.getHeight();
		}
		h = nh;
	}
	
	public void row() {
		int newY = 0;
		for(Row r : content) {
			newY += r.getHeight();
		}
		
		content.add(new Row(x, y + newY));
		currentRow++;
	}
	
	@Override
	public void render(SpriteBatch sb) {
		for(Row r : content) {
			r.render(sb);
		}
	}

	@Override
	public void render(ShapeRenderer sr, Color c) {
		if(true) return;
		for(Row r : content) {
			r.render(sr, Color.RED);
		}
		sr.setColor(c);
		sr.rect(x, y, w, h);
	}
	
	@Override
	public void setPosition(int x, int y) {
		if(content == null) return;
		int dx = x - this.x;
		int dy = y - this.y;
		for(Row i : content) {
			i.setPosition(i.x + dx, i.y + dy);
		}
		this.x = x;
		this.y = y;
	}
	
	
	private int scrollFactor = 8;
	@Override
	public void handle(BattleInput input) {
		for(Row r : content) {
			r.handle(input);
		}
		int dy = input.getScroll()*scrollFactor;
		setPosition(x, y - dy);
	}

	public enum Strategy{
		LEFT, CENTER, RIGHT;
	}

}
