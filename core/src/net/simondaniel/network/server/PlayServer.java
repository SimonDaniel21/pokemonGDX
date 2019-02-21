package net.simondaniel.network.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import net.simondaniel.network.Network;
import net.simondaniel.network.server.Response.ServerInfoS;

public class PlayServer extends Server{

	public final String SERVER_NAME;
	private ServerInfoS serverInfoPacket;
	
	List<NeuerUser> users;
	
	public PlayServer(String name) {
		users = new ArrayList<NeuerUser>();
		this.SERVER_NAME = name;
		serverInfoPacket = new ServerInfoS();
		serverInfoPacket.name = SERVER_NAME;
		Listener infoListener = new Listener() {
			public void connected(Connection c) {
				c.sendTCP(serverInfoPacket);
			};
		};
		this.addListener(infoListener);
	}
	
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

		NeuerUser c = new NeuerUser(this);
		users.add(c);
		c.authService.activate();
		
		return c;
	}
	
}
