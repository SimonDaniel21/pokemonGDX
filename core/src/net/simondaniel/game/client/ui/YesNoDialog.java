package net.simondaniel.game.client.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public abstract class YesNoDialog extends Dialog{
	
	State state;
	Button yesbttn, nobttn;

	public YesNoDialog(String title, Skin skin) {
		super(title, skin);
		state = State.HIDDEN;
		this.debug();
		
		button("yes", skin).addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	            System.out.println("Button yes Pressed");
	            hide();
	        }
	    });
		button("no", skin).addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	            System.out.println("Button no Pressed");
	            hide();
	        }
	    });
	}
	
	public abstract void yes();
	public abstract void no();
	
	enum State{
		YES, NO, HIDDEN, WAITING;
	}
}
