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

	AuthenticationSservice auth;
	UserTrackSservice track;
	MatchmakingSservice match;
	
	public PlayServer(String name) {
		this.SERVER_NAME = name;
		
		auth = new AuthenticationSservice(this);
		track = new UserTrackSservice(this);
		match = new MatchmakingSservice(this);
		
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
		auth.activate(c);
		return c;
	}
	
}
