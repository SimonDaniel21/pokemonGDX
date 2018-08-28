package net.simondaniel.game.client.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class UImaskHandler extends Stage {
	private Table backgroundtable;
	
	public UImaskHandler(TextureRegion tr) {
		super();
		backgroundtable = new Table();
		backgroundtable.background(new Image(tr).getDrawable());
		backgroundtable.setFillParent(true);
		addActor(backgroundtable);
	}
	
	@Override
	public void clear() {
		super.clear();
		addActor(backgroundtable);
	}
}
