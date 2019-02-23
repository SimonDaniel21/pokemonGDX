package net.simondaniel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import net.simondaniel.util.MyRandom;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.game.client.ui.InfoDialog;
import net.simondaniel.game.client.ui.UImaskHandler;
import net.simondaniel.game.client.ui.masks.GameMenu;
import net.simondaniel.game.client.ui.masks.ServerSelection;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.PlayClient;

public class MainMenuScreen implements Screen{

	public static final int WIDTH = 1280, HEIGHT = 720;
	
	public UImaskHandler stage;
	public ServerSelection serverSelection;
	GameClient gc;
	
	UImaskHandler handler;
	
	// DEBUG VALUES
	private final boolean autoLogin;
	
	public MainMenuScreen() {
		autoLogin = false;
	}
	public MainMenuScreen(boolean autoLogin) {
		this.autoLogin = autoLogin;
	}
	
	@Override
	public void show() {
		Skin skin = new Skin(Gdx.files.internal("skins/sgx/sgx-ui.json"));
	
//			PlayClient gc = new GameClient("localhost", "AutoConnectServer");
//			gc.sendConnectRequest();
//			if(gc.waitForConnection()) {
//				gc.sendLoginRequest("user " + MyRandom.random.nextInt(1000), "development");
//				if(gc.waitForLogin()) {
//					PokemonGDX.game.client = gc;
//					//m.getInfo().client = gc;
//					serverSelection.switchTo(m);
//				}
//			}
		}
	

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}


	@Override
	public void pause() {
	}


	@Override
	public void resume() {
	}


	@Override
	public void hide() {
	}


	@Override
	public void dispose() {
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

}
