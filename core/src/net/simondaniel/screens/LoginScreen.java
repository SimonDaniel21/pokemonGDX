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
import net.simondaniel.game.client.ui.masks.ServerSelectionInfo;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.PlayClient;

public class LoginScreen implements Screen{


	public UImaskHandler stage;
	public ServerSelection serverSelection;
	PlayClient client;
	
	UImaskHandler handler;
	
	// DEBUG VALUES
	private final boolean autoLogin;
	
	
	
	public LoginScreen() {
		this(false);
	}
	public LoginScreen(boolean autoLogin) {
		this.autoLogin = autoLogin;
		client = new PlayClient();
	}
	
	
	@Override
	public void show() {
		
		Skin skin = new Skin(Gdx.files.internal("skins/sgx/sgx-ui.json"));

		stage = new UImaskHandler(new TextureRegion(new Texture("gfx/background.jpg")), skin);
		ServerSelectionInfo info = stage.server_select_mask.getInfo();
		info.client = client;
		info.greetingMessage = "";
		info.autoConnect = true;
		stage.server_select_mask.show();
		
		if(autoLogin) {
			
		}
		
	
		
		
		
		
		
		
		InfoDialog.init(skin);
		//stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);

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
