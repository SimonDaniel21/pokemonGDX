package net.simondaniel.game.client.ui.masks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.simondaniel.game.client.ui.UImask;

public class IngameMenu extends UImask{

	Stage stage;
	BitmapFont font;
	
	Button bttnPokedex;
	Button bttnPokemon;
	Button bttnItems;
	Button bttnOptions;
	Button bttnExit;
	
	public IngameMenu(Skin skin){
		super(null, skin);
		int w = 200;
		int h = 500;
		bttnPokedex = new TextButton("fullscreen", skin, "big");
		bttnPokedex.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(Gdx.graphics.isFullscreen()) {
					Gdx.graphics.setWindowedMode(1280, 720);
				}
				else {
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				}
				
				
			}
		});
		add(bttnPokedex);
		row();
		
		bttnPokemon = new TextButton("pokemon", skin);
		add(bttnPokemon);
		row();
		
		bttnItems = new TextButton("items", skin);
		add(bttnItems);
		row();
		
		bttnOptions = new TextButton("options", skin);
		add(bttnOptions);
		row();
		
		bttnExit = new TextButton("exit", skin);
		add(bttnExit);
		
		font = new BitmapFont(Gdx.files.internal("font/small_letters_font.fnt"));
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
