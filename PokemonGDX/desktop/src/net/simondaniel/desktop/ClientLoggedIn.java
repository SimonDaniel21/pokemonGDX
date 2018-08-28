package net.simondaniel.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import net.simondaniel.Config;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.game.client.ui.masks.GameMenu;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.screens.MainMenuScreen;

public class ClientLoggedIn {


	public static void main(String[] arg) {
		
		
		Gdx.files = new LwjglFiles();
		Config.init();
		PokemonGDX.init();
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		PokemonGDX gdx = new PokemonGDX();
		MainMenuScreen menu = new MainMenuScreen(true);		
	
	
		new LwjglApplication(new PokemonGDX(menu), config);
		
		
		
	}
}
