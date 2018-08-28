package net.simondaniel.game.client.ui;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.simondaniel.game.client.ui.masks.ShowMaskInfo;
import net.simondaniel.network.client.GameClient;

public abstract class UImask<T extends ShowMaskInfo> extends Table {

	protected final T info;
	
	private final float FADE_DELAY = 0.1f;
	
	private Stage currentStage = null;

	private UImask<?> lastMask = null;
	
	private boolean visable = false, ready = false;

	public UImask(T info, Skin skin) {
		super(skin);
		this.info = info;
		//addAction(Actions.alpha(0));
		setTouchable(Touchable.disabled);
		setColor(1, 1, 1, 0);
	}

	public void switchTo(final UImask<?> otherMask) {
		final Stage s = currentStage;
		final UImask<T> instance = this;

		addAction(Actions.sequence(disappear(), Actions.run(new Runnable() {
			@Override
			public void run() {
				instance.hide();
				otherMask.show(s);
				otherMask.lastMask = instance;
			}
		})));
	}

	public void goBack() {
		
		if(lastMask == null) {
			return;
		}
		
		final Stage s = currentStage;
		final UImask<T> instance = this;

		addAction(Actions.sequence(disappear(), Actions.run(new Runnable() {
			@Override
			public void run() {
				instance.hide();
				lastMask.show(s);
				//lastMask.addAction(Actions.alpha(1));
				//lastMask.addAction(Actions.scaleTo(1.4f, 1.4f, 3));
			}
		})));
	}

	public void show(Stage s) {
		List<String> missing = info.getMissingFields();
		if(!missing.isEmpty()){
			System.err.println("cant show UImask [" + this.getClass().getSimpleName() + "] because following fields are missing: ");
			String string = "[";
			for(String m : missing) {
				string += m + ",";
			}
			string = string.substring(0, string.length() -1);
			string += "]";
			System.err.println(string);
			
			return;
		}
		if (currentStage != null)
			hide();
		s.addActor(this);
		enter();
		setFillParent(true);
		currentStage = s;
		addAction(appear());
		System.out.println("showing");
	}
	
	
	private Action appear() {
		return Actions.sequence(Actions.fadeIn(FADE_DELAY), Actions.touchable(Touchable.enabled), Actions.run(new Runnable() {
			
			@Override
			public void run() {
				visable = true;
			}
		}));
	}
	private Action disappear() {
		return Actions.sequence(Actions.touchable(Touchable.disabled), Actions.fadeOut(FADE_DELAY), Actions.run(new Runnable() {
			
			@Override
			public void run() {
				visable = false;
			}
		}));
	}

	public void hide() {
		leave();
		currentStage.clear();
		currentStage = null;
		visable = false;
	}
	
	protected final void ready() {
		ready = true;
	}
	
	public abstract void enter();
	
	public abstract void leave();
	
	@Override
	public boolean isVisible() {
		return visable;
	}
	

	public T getInfo() {
		return info;
	}
}
