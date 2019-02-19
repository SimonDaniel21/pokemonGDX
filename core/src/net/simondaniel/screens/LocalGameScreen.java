package net.simondaniel.screens;

import com.badlogic.gdx.Screen;

import net.simondaniel.network.server.GameServer;

public abstract class LocalGameScreen implements Screen{
	
	public LocalGameScreen(GameServer server) {
		
	}

	public abstract void receivePacket(Object packet);
	
	//take care of playerInput
	public abstract void handleInput();
	
	//prediction interpolization and local events take place here!
	public abstract void update(float delta);
	
	public abstract void draw(float delta);
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
