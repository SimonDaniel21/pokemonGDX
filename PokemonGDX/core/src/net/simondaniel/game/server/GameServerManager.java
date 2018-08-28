package net.simondaniel.game.server;


import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import net.simondaniel.fabio.GameMode;
import net.simondaniel.network.client.Request.AccountActivationC;
import net.simondaniel.network.client.Request.LobbyCreateC;
import net.simondaniel.network.client.Request.LobbyJoinC;
import net.simondaniel.network.client.Request.LobbyListC;
import net.simondaniel.network.client.Request.LoginC;
import net.simondaniel.network.client.Request.MessageC;
import net.simondaniel.network.client.Request.MoveToC;
import net.simondaniel.network.client.Request.RegisterUserC;
import net.simondaniel.network.client.Request.TeamJoinC;
import net.simondaniel.network.server.GameServer;
import net.simondaniel.network.server.UserConnection;
import net.simondaniel.network.server.Response.LobbyJoinS;
import net.simondaniel.network.server.Response.LobbyListS;
import net.simondaniel.network.server.Response.LobbyUserJoinedS;
import net.simondaniel.network.server.Response.LoginS;
import net.simondaniel.network.server.Response.MessageS;
import net.simondaniel.network.server.Response.MoveToS;
import net.simondaniel.network.server.Response.PlayerListS;
import net.simondaniel.network.server.Response.TeamJoinedS;
import net.simondaniel.network.server.User;

public class GameServerManager extends Listener{
	
	static int c = 0;
	int id;

	private List<Lobby> lobbys;
	
	private GameServer gs;
	public GameServerManager(GameServer gs) {
		id = c;
		c++;
		this.gs = gs;
		lobbys = new ArrayList<Lobby>();
		//lobbys.add(new Lobby("existing lobby", GameMode.ONE_VS_ONE, gs));
	}
	
	@Override
	public void received(Connection con, Object o) {
		
		UserConnection c = (UserConnection) con;

		if(o instanceof LoginC) {
			
			LoginC p = (LoginC) o;
			LoginS r = new LoginS();
			r.response = gs.login(c, p.name, p.pw);
			c.sendTCP(r);
		}
		else if(o instanceof RegisterUserC) {
			RegisterUserC p = (RegisterUserC) o;
			gs.registerUser(c, p.name, p.pw, p.email);
		}
		else if(o instanceof AccountActivationC) {
			AccountActivationC p = (AccountActivationC) o;
			gs.activateUser(c, p.name, p.code);
		}
		else {
			if(c.user != null && c.user.lobby != null)
				c.user.lobby.receivedFromClient(c, o);
		}
		if(o instanceof LobbyListC) {
			LobbyListS lls = new LobbyListS();
			lls.lobbys = new String[lobbys.size()];
			for(int i = 0; i < lobbys.size(); i++)
				lls.lobbys[i] = lobbys.get(i).NAME;
			c.sendTCP(lls);
			
			PlayerListS pls = new PlayerListS();
			pls.players = new String[gs.getUsers().size()];
			for(int i = 0; i < gs.getUsers().size(); i++)
				pls.players[i] = gs.getUsers().get(i).getName();
			c.sendTCP(pls);
		}
	
		if(o instanceof MessageC){
			MessageC p = (MessageC) o;
			MessageS s = new MessageS();
			s.message = p.message;
			s.sender = c.user.name;
			gs.window.messageReceived(s.sender, s.message);
			gs.sendToAllTCP(s);
		}
		
		if(o instanceof TeamJoinC) {
			TeamJoinC t = (TeamJoinC) o;
			int teamSlot = c.user.lobby.joinTeam(c, t.teamID);
			//System.out.println("trying to add to team " + t.teamID + " got slot " + teamSlot);
			
			if(teamSlot == Lobby.LOBBY_FULL || teamSlot == Lobby.NO_SUCH_USER) {
				TeamJoinedS p = new TeamJoinedS();
				p.id = teamSlot;
				c.sendTCP(p);
			}
			else {
				TeamJoinedS p = new TeamJoinedS();
				p.id = t.teamID;
				p.name = c.user.name;
				p.slotID = teamSlot;
				c.user.lobby.sendToAllTCP(p);
			}
		}
		else if(o instanceof LobbyCreateC){
			LobbyCreateC p = (LobbyCreateC) o;
			String name = p.name;
			boolean exists = false;
			for(Lobby l : lobbys) {
				if(l.NAME.equalsIgnoreCase(name)){
					MessageS m = new MessageS();
					m.sender = "server";
					m.message = "lobby name already exists";
					m.type = 1;
					c.sendTCP(m);
					exists = true;
					break;
				}
			}
			if(!exists) {
				Lobby l = new Lobby(name, GameMode.valueOf(p.gameMode), gs);
				l.addUser(c);
				c.user.lobby = l;
				
				LobbyJoinS s = new LobbyJoinS();
				s.name = name;
				s.gameMode = p.gameMode;
				s.others = l.getNames();
				c.sendTCP(s);
				lobbys.add(l);
				gs.window.addedLobby(name);
				LobbyListS lls = new LobbyListS();
				lls.lobbys = new String[lobbys.size()];
				for(int i = 0; i < lobbys.size(); i++)
					lls.lobbys[i] = lobbys.get(i).NAME;
				gs.sendAllOutsideLobbyTCP(lls);
			}
		}
		else if(o instanceof LobbyJoinC) {
			LobbyJoinC p = (LobbyJoinC) o;
			Lobby l = getLobby(p.lobbyName);
			if(l != null) {
				LobbyJoinS ps = new LobbyJoinS();
			
				if(l.addUser(c)) {
					c.user.lobby = l;
					
					LobbyUserJoinedS luj = new LobbyUserJoinedS();
					luj.name = c.user.name;
					l.sendToAllTCP(luj);
					ps.name = l.NAME;
					ps.gameMode = l.getMode().ordinal();
					ps.others = l.getNames();
				}
				else {
					ps.gameMode = -1;
				}
				c.sendTCP(ps);
			}
		}
	}
	

	/**
	 * 
	 * @param name lobbyName
	 * @return lobby with the given name or null if no such lobby exists
	 */
	private Lobby getLobby(String name) {
		for(Lobby l : lobbys) {
			if(l.NAME.equals(name)) {
				return l;
			}
		}
		return null;
	}
	
	@Override
	public void disconnected(Connection c) {
		UserConnection user = (UserConnection) c;
		gs.logout(user);
		if(user.user != null)
			System.out.println("removing user " + user.user.getName());
	}
	
}
