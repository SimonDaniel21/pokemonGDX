package net.simondaniel.desktop;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.files.FileHandle;

import net.simondaniel.Config;
import net.simondaniel.FileSystem;
import net.simondaniel.GameConfig;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.game.server.GameServerManager;
import net.simondaniel.game.server.PokemonGDXS;
import net.simondaniel.network.Download;
import net.simondaniel.network.UpdateDialog;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.server.GameServer;

public class DesktopLauncher {
	public static void main(String[] arg) {
		
		Gdx.files = new LwjglFiles();
		Config.init();
		PokemonGDX.init();
		int r = JOptionPane.YES_OPTION;
		// r = JOptionPane.showConfirmDialog(null, "start server", "INFO",
		// JOptionPane.YES_NO_OPTION);
		
		GameConfig cfg = Config.gameConfig;
		
		String s =  Download.downloadString(cfg.VERSION_URL);
		String sa[] = s.split(System.lineSeparator());
	
		String newestVersion = sa[0];
		
		if(!PokemonGDX.VERSION.equals(newestVersion)){
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			UpdateDialog.downloadSize = (Integer.parseInt(sa[1]) / 1000.0f / 1000.0f);
			String fileSize = df.format(UpdateDialog.downloadSize);
			
			int res = JOptionPane.showConfirmDialog(null,
					"die Version (" + newestVersion + ") steht bereit, die aktuelle Vesion lautet: " + PokemonGDX.VERSION +	
					"\nneue Version (ca." + fileSize +"mb) herunterladen?" ,
					"neue Version",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			
			if(res == JOptionPane.YES_OPTION) {
				UpdateDialog dialog = new UpdateDialog(cfg.DOWNLOAD_URL, cfg.CONFIG_URL, cfg.FILENAME);
			}
		}
		
		
	}
	
	

	public static void launchClient() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new PokemonGDX(), config);
	}

	public static void launchServer(GameServer server) {
		Gdx.files = new LwjglFiles();
		//PokemonGDXS serverProgram = new PokemonGDXS(server);
		//serverProgram.start();
		server.addListener(new GameServerManager(server));
		System.out.println("launching server!");
	}
}
