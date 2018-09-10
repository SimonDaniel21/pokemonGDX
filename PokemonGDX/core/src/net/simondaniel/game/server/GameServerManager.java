package net.simondaniel.game.server;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.simondaniel.fabio.GameMode;
import net.simondaniel.network.FileTransfer;
import net.simondaniel.network.client.Request.*;
import net.simondaniel.network.server.GameServer;
import net.simondaniel.network.server.UserConnection;
import net.simondaniel.network.server.Response.*;
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
		
		gs.window.packetReceived();

		if(o instanceof LoginC) {
			
			LoginC p = (LoginC) o;
			LoginS r = new LoginS();
			r.response = gs.login(c, p.name, p.pw);
			c.sendTCP(r);
			
			//FileTransfer.sendFile(c, Gdx.files.internal("gfx/tim_hero.png"), "gfx/userPics/testTransfer.png");
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
		}
		else if (o instanceof UserListC) {
			PlayerListS pls = new PlayerListS();
			pls.joined = new UserJoinedS[gs.getUsers().size()];
			for(int i = 0; i < gs.getUsers().size(); i++) {
				UserJoinedS p = new UserJoinedS();
				p.user = gs.getUsers().get(i).getName();
				pls.joined[i] = p;
			}
			c.sendTCP(pls);
			gs.startTracking(c);
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
		
		}
		else if(o instanceof LobbyJoinC) {
			LobbyJoinC p = (LobbyJoinC) o;
			Lobby l = getLobby(p.lobbyName);
			if(l != null) {
				l.addUser(c);
			}
		}
		else if(o instanceof LobbyLeaveC) {
			LobbyLeaveC p = (LobbyLeaveC) o;
			Lobby l = c.user.lobby;
			
			if(l != null) {
				l.removeUser(c);
			}
		}
		else if(o instanceof InviteUserToLobbyC) {
			InviteUserToLobbyC p = (InviteUserToLobbyC)o;
			InviteUserToLobbyS s = new InviteUserToLobbyS();
			
			Lobby l = getLobby(p.lobby);
			if(l != null) {
				UserConnection user = gs.getUser(p.user);
				System.out.println("lobby is not null");
				
				if(user != null) {
					l.invite(user, c.user.name);
				}
			}
		}
		else if(o instanceof InviteAnswerC) {
			InviteAnswerC p = (InviteAnswerC)o;
			
			Lobby l = getLobby(p.lobby);
			if(l != null) {
				l.inviteAnswer(c, p.answer);
			}
		}
		else if(o instanceof UserReadyC) {
			UserReadyC p = (UserReadyC) o;
			Lobby l = c.user.lobby;
			if(l != null) {
				l.ready(c, p.ready);
			}
		}
	}
	
	private void addLobby(String name) {
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
			lobbys.add(l);
			l.addUser(c);
			
			gs.window.addedLobby(name);
			LobbyListS lls = new LobbyListS();
			lls.lobbys = new String[lobbys.size()];
			for(int i = 0; i < lobbys.size(); i++)
				lls.lobbys[i] = lobbys.get(i).NAME;
			gs.sendAllOutsideLobbyTCP(lls);
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
		if(user.user != null) {
			Lobby l = user.user.lobby;
			System.out.println("removing user " + user.user.getName());
			if(l != null) {
				l.removeUser(user);
				if(l.isEmpty()) {
					lobbys.remove(l);
					gs.window.removedLobby(l.NAME);
				}
			}
		}
	}
}
