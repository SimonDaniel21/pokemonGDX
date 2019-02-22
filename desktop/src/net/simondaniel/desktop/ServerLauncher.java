package net.simondaniel.desktop;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.simondaniel.LaunchConfiguration;
import net.simondaniel.game.client.PokemonGDX;

public class ServerLauncher {

	public static void start() {
//		Gdx.files = new LwjglFiles();
//		GameServer server;
//		try {
//			server = new GameServer();
//			server.bindMonitor(new ServerFrame(server));
//			server.addLobby("testLobby", GameMode.ONE_VS_ONE.ordinal());
//			server.openConsole();
//			server.start();
//			
//			boolean lag = true;
//			
//			if(lag) {
//				server.addListener(new LagListener(50, 200, new ServerListener(server)));
//			}
//			else {
//				server.addListener(new ThreadedListener(new ServerListener(server)));
//			}
//			
//			System.out.println("launched server!");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		new HeadlessApplication(new PokemonGDX(LaunchConfiguration.DEBUG_SERVER));
	}

}
