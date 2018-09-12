package net.simondaniel.desktop;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.esotericsoftware.kryonet.Listener.LagListener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

import net.simondaniel.GameMode;
import net.simondaniel.network.server.GameServer;
import net.simondaniel.network.server.ServerListener;

public class ServerLauncher {

	public static void start() {
		Gdx.files = new LwjglFiles();
		GameServer server;
		try {
			server = new GameServer();
			server.bindMonitor(new ServerFrame(server));
			server.addLobby("testLobby", GameMode.ONE_VS_ONE.ordinal());
			server.openConsole();
			server.start();
			
			boolean lag = true;
			
			if(lag) {
				server.addListener(new LagListener(50, 200, new ServerListener(server)));
			}
			else {
				server.addListener(new ThreadedListener(new ServerListener(server)));
			}
			
			System.out.println("launched server!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
