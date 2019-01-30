package net.simondaniel.game.client.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class FriendListEntry extends Table{

	private static Texture onoff;
	Image image;
	ImageButton chatBttn;
	
	public FriendListEntry(Skin s, String name, boolean online) {
		super(s);
		if(onoff == null) {
			onoff = new Texture("gfx/onoff.png");
		}
		int h = onoff.getHeight(), w = h;
		
		TextureRegion on = new TextureRegion(onoff, w, h);
		TextureRegion off = new TextureRegion(onoff, w, 0, w, h);
		this.add(name).width(220);
		image = new Image(online ? on : off);
		this.add(image);
		chatBttn = new ImageButton(new Image(new Texture("gfx/chat_bubbles.png")).getDrawable());
		add(chatBttn);
	}
}
