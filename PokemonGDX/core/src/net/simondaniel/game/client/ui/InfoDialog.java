package net.simondaniel.game.client.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class InfoDialog extends Dialog {

	private static InfoDialog d;
	private Label text;
	
	public InfoDialog(String msg, Skin skin) {
		super("info", skin);
		text = new Label(msg, skin);
		text(text);
		button("ok", true);
	}
	
	public static void init(Skin s) {
		d = new InfoDialog("", s);
	}
	
	public static void show(String msg, Stage stage) {
		d.text.setText(msg);
		d.show(stage);
	}
}
