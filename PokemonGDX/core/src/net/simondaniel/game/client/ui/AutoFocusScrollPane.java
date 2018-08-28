package net.simondaniel.game.client.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;

public class AutoFocusScrollPane extends ScrollPane {

	public AutoFocusScrollPane(Actor widget, Skin skin) {
		super(widget, skin);
		final AutoFocusScrollPane instance = this;
		addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (event instanceof InputEvent) {
					Type type = ((InputEvent) event).getType();
					if (type == InputEvent.Type.enter)
						event.getStage().setScrollFocus(instance);
					if (type == InputEvent.Type.exit)
						event.getStage().setScrollFocus(null);
				}
				return false;
			};
		});
	}
}
