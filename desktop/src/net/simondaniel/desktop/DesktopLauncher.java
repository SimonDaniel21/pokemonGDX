package net.simondaniel.desktop;

import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import net.simondaniel.Config;
import net.simondaniel.GameConfig;
import net.simondaniel.LaunchConfiguration;
import net.simondaniel.game.client.PokemonGDX;
import net.simondaniel.network.Download;
import net.simondaniel.network.UpdateDialog;
import net.simondaniel.network.server.GameServer;

public class DesktopLauncher {
	
	public static final String GIT_URI = "https://github.com/SimonDaniel21/pokemonGDX.git";
	
	public static void main(String[] args) {
		
		Gdx.files = new LwjglFiles();
		
		
		
//		try {
//			File f = FileSystem.loadFile("repo").file();
//			String res = Runtime.getRuntime().exec(f.getPath() + "\\gradlew.bat desktop:dist").getOutputStream().toString();
//
//			String fetchresult = Git.open(f).pull().call().getFetchResult().getMessages();
//			System.out.println("blaaa :   " + res);
//		} catch (InvalidRemoteException e) {
//			e.printStackTrace();
//		} catch (TransportException e) {
//			System.out.println(e.getMessage().equals("Nothing to fetch."));
//			System.out.println("nothing to fetch :)");
//		} catch (GitAPIException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		if(true) return;
//		try {
//			Git.cloneRepository()
//			.setURI(GIT_URI)
//			.setDirectory(FileSystem.loadFile("repo").file())
//			.call();
//		} catch (InvalidRemoteException e) {
//			e.printStackTrace();
//		} catch (TransportException e) {
//			e.printStackTrace();
//		} catch (GitAPIException e) {
//			e.printStackTrace();
//		}

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
		
		LaunchConfiguration.config = config;
		
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
			ServerLauncher.start();
			break;
		default:
			break;
		}

		
	}

	public static void launchClientDebug() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new PokemonGDX(LaunchConfiguration.DEBUG_CLIENT), config);
	}
	
	public static void launchClientRelease() {

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
		//server.addListener(new ServerListener(server));
		System.out.println("launching server!");
	}
	
	public static void launchServerRelease(GameServer server) {
		Gdx.files = new LwjglFiles();
		// PokemonGDXS serverProgram = new PokemonGDXS(server);
		// serverProgram.start();
		//server.addListener(new ServerListener(server));
		System.out.println("launching server!");
	} 
}
