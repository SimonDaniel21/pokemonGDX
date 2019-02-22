package net.simondaniel.network.server;

import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.GameMode;
import net.simondaniel.game.server.Lobby;
import net.simondaniel.network.client.Request.LobbyCreateC;
import net.simondaniel.network.client.Request.LobbyJoinC;
import net.simondaniel.network.server.Response.LobbyAddedS;
import net.simondaniel.network.server.Response.LobbyJoinS;

public class MatchmakingSservice extends Sservice{

	ArrayList<Lobby> lobbies;
	
	private ArrayList<NeuerUser> trackingUsers;
	
	public MatchmakingSservice(PlayServer server) {
		super(server);
		lobbies = new ArrayList<Lobby>();
		trackingUsers = new ArrayList<NeuerUser>();
		
		Lobby standardLobby = new Lobby("standard lobby", GameMode.ONE_VS_ONE);
		lobbies.add(standardLobby);
	}

	@Override
	protected void received(Connection c, Object o) {
		if(o instanceof LobbyCreateC) {
			LobbyCreateC p = (LobbyCreateC)o;
			addLobby(p.name, GameMode.valueOf(p.gameMode));
		}
		if(o instanceof LobbyJoinC) {
			LobbyJoinC p = (LobbyJoinC) o;
		}
	}
	
	@Override
	protected void onActivation(Connection c) {
		System.err.println("Server ON ACTIVATION");
		for(Lobby l : lobbies) {
			addPacket.name = l.NAME;
			c.sendTCP(addPacket);
		}
		trackingUsers.add((NeuerUser) c);
	}

	@Override
	protected void onDeactivation(Connection c) {
		trackingUsers.remove((NeuerUser)c);
	}
	
	LobbyAddedS addPacket;
	
	private void addLobby(String name, GameMode mode) {
		Lobby l = new Lobby(name, mode);
		lobbies.add(l);
		addPacket = new LobbyAddedS();
		addPacket.name = name;
		for(NeuerUser u : trackingUsers) {
			u.sendTCP(addPacket);
		}
	}
	
	private void join(NeuerUser u, String lobby) {
		LobbyJoinS p = new LobbyJoinS();
		for(Lobby l : lobbies) {
			if(l.NAME.equals(lobby)) {
				l.addUser(u);
			}
		}
	}

}
