package net.simondaniel.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;

import net.simondaniel.Config;
import net.simondaniel.LaunchConfiguration;
import net.simondaniel.game.client.PokemonGDX;

public class ClientLauncher {

	public static void start() {
		
		Gdx.files = new LwjglFiles();
		Config.init();
		
		//UpdateDialog d = new UpdateDialog(GameConfig.gameConfig.DOWNLOAD_URL, GameConfig.gameConfig.CONFIG_URL, GameConfig.gameConfig.FILENAME);	
	
		
		PokemonGDX.init();
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		
		new LwjglApplication(new PokemonGDX(LaunchConfiguration.DEBUG_CLIENT), config);
		//new HeadlessApplication(new PokemonGDX(LaunchConfiguration.DEBUG_CLIENT));
	}
}
