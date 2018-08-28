package net.simondaniel.desktop;

import java.io.IOException;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;

import net.simondaniel.Config;
import net.simondaniel.GameConfig;
import net.simondaniel.MyRandom;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.network.UpdateDialog;
import net.simondaniel.network.server.GameServer;
import net.simondaniel.screens.MainMenuScreen;

public class ClientLauncher {

	public static void main(String[] arg) {
		
		Gdx.files = new LwjglFiles();
		Config.init();
		
		UpdateDialog d = new UpdateDialog(GameConfig.gameConfig.DOWNLOAD_URL, GameConfig.gameConfig.CONFIG_URL, GameConfig.gameConfig.FILENAME);	
	
		
		PokemonGDX.init();
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		
		MainMenuScreen menu = new MainMenuScreen();
		new LwjglApplication(new PokemonGDX(menu), config);
	}
}