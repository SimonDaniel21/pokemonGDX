package net.simondaniel.network.server;


import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.simondaniel.game.server.Lobby;
import net.simondaniel.network.client.Request.*;
import net.simondaniel.network.server.Response.*;
public class ServerListener extends Listener{
	
	//int id;
	
	private GameServer gs;
	public ServerListener(GameServer gs) {

		this.gs = gs;
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
			if(c != null && c.lobby != null)
				c.lobby.receivedFromClient(c, o);
		}
		if(o instanceof LobbyListC) {
			LobbyListS lls = new LobbyListS();
			lls.lobbys = gs.getLobbyList();
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
			s.sender = c.name;
			gs.window.messageReceived(s.sender, s.message);
			gs.sendToAllTCP(s);
		}
		
		if(o instanceof TeamJoinC) {
			TeamJoinC t = (TeamJoinC) o;
			
			int teamSlot = c.lobby.joinTeam(c, t.teamID);
			//System.out.println("trying to add to team " + t.teamID + " got slot " + teamSlot);
			
			if(teamSlot == Lobby.LOBBY_FULL || teamSlot == Lobby.NO_SUCH_USER) {
				TeamJoinedS p = new TeamJoinedS();
				p.id = teamSlot;
				c.sendTCP(p);
			}
			else {
				TeamJoinedS p = new TeamJoinedS();
				p.id = t.teamID;
				p.name = c.name;
				p.slotID = teamSlot;
				c.lobby.sendToAllTCP(p);
			}
		}
		else if(o instanceof LobbyCreateC){
			LobbyCreateC p = (LobbyCreateC) o;
		
			if(gs.isLobbyNameTaken(p.name)) {
					MessageS m = new MessageS();
					m.sender = "server";
					m.message = "lobby name already exists";
					m.type = 1;
					c.sendTCP(m);
			}
			else {
				Lobby l = gs.addLobby(p.name, p.gameMode);
				l.addUser(c);
			}
		}
		else if(o instanceof LobbyJoinC) {
			LobbyJoinC p = (LobbyJoinC) o;
			Lobby l = gs.getLobby(p.lobbyName);
			if(l != null) {
				l.addUser(c);
			}
		}
		else if(o instanceof LobbyLeaveC) {
			LobbyLeaveC p = (LobbyLeaveC) o;
			Lobby l = c.lobby;
			
			if(l != null) {
				l.removeUser(c);
			}
		}
		else if(o instanceof InviteUserToLobbyC) {
			InviteUserToLobbyC p = (InviteUserToLobbyC)o;
			InviteUserToLobbyS s = new InviteUserToLobbyS();
			
			Lobby l = gs.getLobby(p.lobby);
			if(l != null) {
				UserConnection user = gs.getUser(p.user);
				System.out.println("lobby is not null");
				
				if(user != null) {
					l.invite(user, c.name);
				}
			}
		}
		else if(o instanceof InviteAnswerC) {
			InviteAnswerC p = (InviteAnswerC)o;
			
			Lobby l = gs.getLobby(p.lobby);
			if(l != null) {
				l.inviteAnswer(c, p.answer);
			}
		}
		else if(o instanceof UserReadyC) {
			UserReadyC p = (UserReadyC) o;
			Lobby l = c.lobby;
			if(l != null) {
				l.ready(c, p.ready);
			}
		}
	}
	
	
	@Override
	public void disconnected(Connection c) {
		UserConnection user = (UserConnection) c;
		gs.logout(user);
		if(user != null) {
			Lobby l = user.lobby;
			System.out.println("removing user " + user.getName());
			if(l != null) {
				l.removeUser(user);
				if(l.isEmpty()) {
					gs.lobbys.remove(l);
					gs.window.removedLobby(l.NAME);
				}
			}
		}
	}
}
