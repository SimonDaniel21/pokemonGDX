package net.simondaniel.game.client.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public abstract class NamingDialog extends Dialog {

	TextField[] tfs;
	
	short ok, cancel;
	
	
	public NamingDialog(String title, Skin skin, Entry... entries) {
		this(title, skin, ButtonOption.OK_CANCEL, entries);
	}

	public NamingDialog(String title, Skin skin, ButtonOption buttonOption, Entry... entries) {
		super(title, skin);
		
		tfs = new TextField[entries.length];

		for (int i = 0; i < entries.length; i++) {
			Entry e = entries[i];
			if(e.title != null)
				this.text(e.title);
			
			tfs[i] = new TextField(e.default_text, skin);
			getContentTable().add(tfs[i]).width(300);
			if(!e.editable)
				tfs[i].setTouchable(Touchable.disabled);
			getContentTable().row();
		}

		if(buttonOption == ButtonOption.OK || buttonOption == ButtonOption.OK_CANCEL) {
			button("ok", ok);
		}
		

		if (buttonOption == ButtonOption.CANCEL || buttonOption == ButtonOption.OK_CANCEL) {
			button("cancel");
		}
		
		addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Input.Keys.ENTER) {
					result(ok);
					hide();
				}
				return false;
			}
		});
		
	}
	
	
	public void setTFwidth(int width) {
		for(TextField f : tfs) {
		}
	}
	public abstract void updateValues(String[] values);
	
	@Override
    protected void result(Object object) {
        super.result(object);
        if(object == null) return;
        if(object.equals(ok)){
        	System.out.println("Change");
			String[] values = new String[tfs.length];
			for (int i = 0; i < tfs.length; i++) {
				values[i] = tfs[i].getText();
			}
			updateValues(values);
        }
    }

	
	public static class Entry {
		String title, default_text;
		boolean editable;

		public Entry(String title, String default_text, boolean editable) {
			this.title = title;
			this.default_text = default_text;
			this.editable = editable;
		}
		
		public Entry(String title, String default_text) {
			this(title, default_text, true);
		}

		public Entry(String default_text) {
			this(null, default_text);
		}
	}
	
	public static enum ButtonOption{
		OK, OK_CANCEL, CANCEL;
	}
	
	
	@Override
	public Dialog show(Stage stage) {
		Dialog d = super.show(stage);
		if(tfs[0] != null) {
			stage.setKeyboardFocus(tfs[0]);
			tfs[0].setCursorPosition(tfs[0].getText().length());
		}
		
		return d;
	}
}

	
