package net.simondaniel.game.client.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public class Inbox extends Table{
	
	ScrollPane sp;
	Table scrollTable;
	
	public Inbox(Skin s) {
		super(s);
		scrollTable = new Table(s);
		// t.addActor(sp);
		// t.addActor(new TextButton("chat", s));
		sp = new AutoFocusScrollPane(scrollTable, s);
		//sp.setOverscroll(false, true);
		sp.setScrollingDisabled(true, false);
		this.getColor().a = 0.7f;
		add(sp).bottom();
		this.bottom();
	}
	
	public void addMail(String title, String msg, String[] data, MailListener listener) {
		InboxEntry ie = new InboxEntry(getSkin(), title, msg, data, listener);
		ie.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1)));
		scrollTable.add(ie);
		scrollTable.row();
	}

	public static interface MailListener{
		public void accept(String[] data);
		public void decline(String[] data);
	}
	
	class InboxEntry extends Table{
		Image image;
		Button delete;
		MailListener listener;
		String[] data;
		public InboxEntry(Skin s, String title, String msg, String[] dataa, MailListener l) {
			super(s);
			this.data = dataa;
			this.listener = l;
			this.getColor().a = 1.4f;
			TextArea ta = new TextArea(title + "\n" + msg, getSkin());
			ta.setTouchable(Touchable.disabled);
			add(ta).width(270).growY().colspan(3);
			row();
			TextButton accept = new TextButton("accept", getSkin(), "small");
			accept.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					listener.accept(data);
					addAction(Actions.sequence(Actions.fadeOut(0.3f),
							Actions.run(new Runnable() {
								@Override
								public void run() {
									remove();
								}
							})));
				}

			});
			add(accept);
			TextButton decline = new TextButton("decline", getSkin(), "small");
			decline.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					listener.decline(data);
					addAction(Actions.sequence(Actions.fadeOut(0.3f),
							Actions.run(new Runnable() {
								@Override
								public void run() {
									remove();
								}
							})));
				}

			});
			add(decline);
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
