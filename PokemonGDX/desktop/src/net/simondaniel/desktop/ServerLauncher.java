package net.simondaniel.desktop;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;

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
			
			server.addListener(new GameServerManager(server));
			System.out.println("launched server!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
