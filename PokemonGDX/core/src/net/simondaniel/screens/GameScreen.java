package net.simondaniel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import net.simondaniel.game.client.battle.Schiggy;
import net.simondaniel.network.client.GameClient;

public abstract class GameScreen<T extends InputProcessor> implements Screen{

	
	public final int width;

	public final int height;
	
	GameClient client;
	protected T input;
	
	
	public GameScreen(GameClient client, T input, int w, int h) {
		this.client = client;
		this.input = input;
		Stage s;
		this.width = w;
		this.height = h;
	}
	
	@Override
	public final void render(float delta) {
		handlePackets();
		handleInput(input);
		update(delta);
		render();
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(input);
	}
	
	private void handlePackets(){
		//client.handlePacketBuffer();
	}
	
	public abstract void update(float delta);
	public abstract void render();
	public abstract void handleInput(T input);
	

	@Override
	public void resize(int width, int height) {
		
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
}
