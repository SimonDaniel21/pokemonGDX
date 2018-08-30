package net.simondaniel.game.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
	
	protected Stage currentStage = null;

	private UImask<?> lastMask = null;
	
	private boolean visable = false;
	
	// can be set to false if waiting for a server respsonse.
	//play a beep sound if not active or some feedback
	private boolean active;
	
	private static Sound unActiveSound;

	public UImask(T info, Skin skin) {
		super(skin);
		active = true;
		this.info = info;
		if(unActiveSound == null)
			unActiveSound = Gdx.audio.newSound(Gdx.files.internal("sfx/menu/50561__broumbroum__sf3-sfx-menu-select.wav"));
		//addAction(Actions.alpha(0));
		setTouchable(Touchable.disabled);
		setColor(1, 1, 1, 0);
	}

	public void switchTo(final UImask<?> otherMask) {
		final Stage s = currentStage;
		final UImask<T> instance = this;
		
		instance.hide();
		otherMask.show(s);
		otherMask.lastMask = instance;

//		addAction(Actions.sequence(disappear(), Actions.run(new Runnable() {
//			@Override
//			public void run() {
//				instance.hide();
//				otherMask.show(s);
//				otherMask.lastMask = instance;
//			}
//		})));
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
		currentStage = s;
		s.addActor(this);
		enter();
		
		setFillParent(true);
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
	
	public void deactivateUntilResponse() {
		active = false;
		startTimeout();
	}
	
	public void reActivateUI() {
		active = true;
		stopTimeout();
	}
	
	/**plays a beep sound if not active
	 * 
	 * @return false if waiting for a server response and critical actions should be disabled
	 */
	public boolean isActive() {
		if(!active) beep();
		return active;
	}
	
	public void beep() {
		unActiveSound.play();
	}
	
	boolean waitingForAnswer = false;
	float waitingTime = 0;
	float TIMEOUT = 40.0f;

	private void startTimeout() {
		waitingTime = 0f;
		waitingForAnswer = true;
	}
	private void stopTimeout() {
		waitingForAnswer = false;
	}
	
	int i = 0;
	@Override
	public void act(float delta) {
		super.act(delta);
		
		i++;
		if(waitingForAnswer) {
			waitingTime += delta;
			if(waitingTime >= TIMEOUT) {
				System.err.println("TIMEOUT");
			}
		}
	}
	
	
}
