package net.simondaniel.desktop;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.esotericsoftware.kryonet.Listener.LagListener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

import net.simondaniel.game.server.GameServerManager;
import net.simondaniel.network.server.GameServer;

public class ServerLauncher {

	public static void main(String[] args) {
		Gdx.files = new LwjglFiles();
		GameServer server;
		try {
			server = new GameServer();
			server.bindMonitor(new ServerFrame(server));
			server.openConsole();
			server.start();
			
			boolean lag = true;
			
			if(lag) {
				server.addListener(new LagListener(50, 200, new GameServerManager(server)));
			}
			else {
				server.addListener(new ThreadedListener(new GameServerManager(server)));
			}
			
			System.out.println("launched server!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
