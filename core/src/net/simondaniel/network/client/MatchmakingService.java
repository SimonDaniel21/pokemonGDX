package net.simondaniel.network.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.network.Synchronized;
import net.simondaniel.network.client.Request.LobbyCreateC;
import net.simondaniel.network.client.Request.LobbyJoinC;
import net.simondaniel.network.client.Request.TeamJoinC;
import net.simondaniel.network.server.Response.LobbyAddedS;
import net.simondaniel.network.server.Response.LobbyJoinS;
import net.simondaniel.network.server.Response.LobbyUserJoinedS;
import net.simondaniel.util.SyncableList;

public class MatchmakingService extends Service{

	public SyncableList<String> lobbyNames;
	public Synchronized<LobbyJoinS> lobbyToJoin;

	public MatchmakingService(Client c) {
		super(c);
		lobbyNames = new SyncableList<String>();
		lobbyToJoin = new Synchronized<LobbyJoinS>();
	}

	@Override
	protected void received(Connection c, Object o) {
		if(o instanceof LobbyAddedS) {
			LobbyAddedS p = (LobbyAddedS)o;
			lobbyNames.add(p.name);
		}
		
		if(o instanceof LobbyJoinS) {
			LobbyJoinS p = (LobbyJoinS)o;

			lobbyToJoin.set(p);
		}
	}
	
	public void addLobby(String name, int type) {
		LobbyCreateC p = new LobbyCreateC();
		p.gameMode = type;
		p.name = name;
		send(p);
		System.err.println("Sent ADDLOBBY");
	}

	public void joinLobby(String name) {
		LobbyJoinC p = new LobbyJoinC();
		p.lobbyName = name;
		send(p);
	}

	public void joinTeam(int id) {
		TeamJoinC p = new TeamJoinC();
		p.teamID = id;
		send(p);
	}

}
