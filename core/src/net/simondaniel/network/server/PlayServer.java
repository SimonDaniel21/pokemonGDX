package net.simondaniel.network.server;

import java.io.IOException;
import java.net.ServerSocket;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import net.simondaniel.network.Network;
import net.simondaniel.network.protocols.InitialListener;

public class PlayServer extends Server{

	
	@Override
	public void start(){
		
		Network.register(this);
	
		try {
			super.start();
			bind(Network.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected Connection newConnection() {

		NeuerUser c = new NeuerUser();
		c.authService.activate();
		return c;
	}
}
