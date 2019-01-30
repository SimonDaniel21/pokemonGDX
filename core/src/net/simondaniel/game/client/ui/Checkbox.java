package net.simondaniel.game.client.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Checkbox extends MyButton {

	private static Texture tex;
	
	private TextureRegion uncheckedImg, checkedImg;
	
	private boolean isChecked = false;
	
	public Checkbox(int x, int y) {
		super(x, y, 50, 50);
		if(tex == null) {
			tex = new Texture("gfx/checkbox50.png");
		}
		int size = tex.getHeight();
		checkedImg = new TextureRegion(tex, size, 0, size, size);
		uncheckedImg = new TextureRegion(tex, size, size);
		setRegion(uncheckedImg);
	}
	

	@Override
	public void getClicked() {
		if(isChecked) {
			setRegion(uncheckedImg);
		}
		else {
			setRegion(checkedImg);
		}
		isChecked = !isChecked;
	}


}
