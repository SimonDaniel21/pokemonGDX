package net.simondaniel.game.client.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class InfoDialog extends Dialog {

	private static InfoDialog d;
	private Label text;
	
	int okInt = 1;
	
	public InfoDialog(String msg, Skin skin) {
		super("info", skin);
		text = new Label(msg, skin);
		text(text);
		button("ok", okInt);
		InfoDialog info = this;
		addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Input.Keys.ENTER) {
					hide();
				}
				return false;
			}
		});
	}
	
	public static void init(Skin s) {
		d = new InfoDialog("", s);
	}
	
	public static void show(String msg, Stage stage) {
		if(d.isVisible())
			d.hide();
		d.text.setText(msg);
		d.show(stage);
	}
}
