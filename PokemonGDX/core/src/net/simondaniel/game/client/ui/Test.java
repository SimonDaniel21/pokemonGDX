package net.simondaniel.game.client.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import net.simondaniel.game.client.ui.masks.ShowMaskInfo;

public class Test extends UImask{

	public final int c;
	
	public Test(Skin skin) {
		this(skin, 0);
	}
	
	public Test(final Skin skin, final int c) {
		super(null, skin);
		this.c = c;
		add("top left " + c).expand();
		TextButton tb1 = new TextButton("next", skin);
		tb1.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				switchTo(new Test(skin, c+1));
			}
		});
		add(tb1);
		row();
		TextButton tb = new TextButton("back", skin);
		tb.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				goBack();
			}
		});
		add(tb).right();
		Friendlist fl = new Friendlist(skin);
		add(fl).size(300, 300);
	}

	@Override
	public void enter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leave() {
		// TODO Auto-generated method stub
		
	}

}
