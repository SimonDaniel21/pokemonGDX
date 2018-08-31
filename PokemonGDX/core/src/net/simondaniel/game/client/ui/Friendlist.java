package net.simondaniel.game.client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class Friendlist extends Table {

	private Skin skin;

	private static Texture onoff;
	private TextureRegion on, off, active;
	ScrollPane sp;

	Dialog d;
	Table scrollTable;
	Skin s;

	public Friendlist(Skin s) {
		super(s);
		this.s = s;
		scrollTable = new Table(s);
		// t.addActor(sp);
		// t.addActor(new TextButton("chat", s));
		
		add("Friendlist").row();
		
		sp = new AutoFocusScrollPane(scrollTable, s);
		sp.setScrollingDisabled(true, false);
		add(sp);
	}
	
	public void set(String... names) {
		for (int i = 0; i < names.length; i++) {
			FriendListEntry fle = new FriendListEntry(s, names[i] + i, true);
			scrollTable.add(fle);
			scrollTable.row();
		}
	}
	
	public void addUser(String name) {
			FriendListEntry fle = new FriendListEntry(s, name, true);
			scrollTable.add(fle);
			scrollTable.row();
	}
}
