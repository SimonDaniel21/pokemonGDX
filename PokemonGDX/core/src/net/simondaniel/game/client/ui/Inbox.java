package net.simondaniel.game.client.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public class Inbox extends Table{
	
	ScrollPane sp;
	
	public Inbox(Skin s) {
		super(s);
		Table scrollTable = new Table(s);
		// t.addActor(sp);
		// t.addActor(new TextButton("chat", s));
		for (int i = 0; i < 6; i++) {
			InboxEntry ie = new InboxEntry(s, "player " + i);
			scrollTable.add(ie);
			scrollTable.row();
		}
		sp = new AutoFocusScrollPane(scrollTable, s);
		//sp.setOverscroll(false, true);
		sp.setScrollingDisabled(true, false);
		this.getColor().a = 0.6f;
		add(sp).bottom();
		this.bottom();
	}

	class InboxEntry extends Table{
		Image image;
		Button delete;
		public InboxEntry(Skin s, String name) {
			super(s);
			this.getColor().a = 1.4f;
			add("Game Invition\n" + name, "small").width(270).colspan(3);
			row();
			add(new TextButton("accept", getSkin(), "small"));
			add(new TextButton("decline", getSkin(), "small"));
			delete = new Button(getSkin(), "close");
			delete.addListener(new ChangeListener() {
				
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					addAction(Actions.sequence(Actions.fadeOut(0.3f),
							Actions.run(new Runnable() {
								@Override
								public void run() {
									remove();
								}
							})));
					System.out.println("removed");
				}
			});
			add(delete);
			
		}
	}
}
