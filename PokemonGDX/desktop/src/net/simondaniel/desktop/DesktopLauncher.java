package net.simondaniel.desktop;

import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import net.simondaniel.Config;
import net.simondaniel.GameConfig;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.game.server.GameServerManager;
import net.simondaniel.network.Download;
import net.simondaniel.network.UpdateDialog;
import net.simondaniel.network.server.GameServer;

public class DesktopLauncher {
	public static void main(String[] args) {

		if(args.length > 1) {
			String commands = "[";
			for(int i = 0; i < LaunchConfiguration.values().length; i++) {
				commands += LaunchConfiguration.values()[i].command;
				if(i != LaunchConfiguration.values().length -1) commands += "|";
			}
			commands += "]";
			System.err.println("usage : " + commands);
			return;
		}
		LaunchConfiguration config;
		if(args.length == 0)
			config = LaunchConfiguration.RELEASE_CLIENT;
		else
			config = LaunchConfiguration.configuration(args[0]);
		
		switch (config) {
		case RELEASE_CLIENT:
			launchClientRelease();
			break;
		case RELEASE_SERVER:
			ServerLauncher.start();
			break;
		case LOGGED_IN:
			ClientLoggedIn.start();
			break;
		case DEBUG_CLIENT:
			ClientLauncher.start();
			break;
		case DEBUG_SERVER:
			break;
		default:
			break;
		}

		
	}

	public static void launchClientDebug() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new PokemonGDX(), config);
	}
	
	public static void launchClientRelease() {

		Gdx.files = new LwjglFiles();
		Config.init();
		PokemonGDX.init();

		GameConfig cfg = Config.gameConfig;

		String s = Download.downloadString(cfg.VERSION_URL);
		String sa[] = s.split(System.lineSeparator());

		String newestVersion = sa[0];

		if (!PokemonGDX.VERSION.equals(newestVersion)) {
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			UpdateDialog.downloadSize = (Integer.parseInt(sa[1]) / 1000.0f / 1000.0f);
			String fileSize = df.format(UpdateDialog.downloadSize);
			int res = JOptionPane.showConfirmDialog(null,
					"die Version (" + newestVersion + ") steht bereit, die aktuelle Vesion lautet: "
							+ PokemonGDX.VERSION + "\nneue Version (ca." + fileSize + "mb) herunterladen?",
					"neue Version", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

			if (res == JOptionPane.YES_OPTION) {
				UpdateDialog dialog = new UpdateDialog(cfg.DOWNLOAD_URL, cfg.CONFIG_URL, cfg.FILENAME);
			}
			ClientLauncher.start();
		}
	}

	public static void launchServerDebug(GameServer server) {
		Gdx.files = new LwjglFiles();
		// PokemonGDXS serverProgram = new PokemonGDXS(server);
		// serverProgram.start();
		server.addListener(new GameServerManager(server));
		System.out.println("launching server!");
	}
	
	public static void launchServerRelease(GameServer server) {
		Gdx.files = new LwjglFiles();
		// PokemonGDXS serverProgram = new PokemonGDXS(server);
		// serverProgram.start();
		server.addListener(new GameServerManager(server));
		System.out.println("launching server!");
	} 
}
