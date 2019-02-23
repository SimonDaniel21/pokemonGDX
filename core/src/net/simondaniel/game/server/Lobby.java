package net.simondaniel.game.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.simondaniel.GameMode;
import net.simondaniel.LaunchConfiguration;
import net.simondaniel.network.server.GameServer;
import net.simondaniel.network.server.MatchmakingSservice;
import net.simondaniel.network.server.MyServerlistener;
import net.simondaniel.network.server.NeuerUser;
import net.simondaniel.network.server.Response.InviteAnswerS;
import net.simondaniel.network.server.Response.InviteUserToLobbyS;
import net.simondaniel.network.server.Response.LobbyJoinS;
import net.simondaniel.network.server.Response.LobbyStartTimerS;
import net.simondaniel.network.server.Response.LobbyUserJoinedS;
import net.simondaniel.network.server.Response.LobbyUserLeftS;
import net.simondaniel.network.server.Response.UserReadyS;
import net.simondaniel.network.server.UserConnection;

public class Lobby{
	
	private String name;

	NeuerUser[] userSlots;
	int[] team;
	

	boolean full;
	
	//[teamindex][teamSlot]
	int[] teamSizes;
	
	int[] fullness;
	
	List<NeuerUser> pendingRequests;
	
	public Lobby(int[] teamSizes, String name) {
		this.name = name;
		this.teamSizes = teamSizes;
		int neededSlots = 0;
		for(int i = 0; i < teamSizes.length; i++) {
			neededSlots += teamSizes[i];
		}
		userSlots = new NeuerUser[neededSlots];
		team = new int[neededSlots];
		for(int i = 0; i < team.length; i++) {
			team[i] = -1;
		}
		fullness = new int[teamSizes.length];
		
		enterPacket = new LobbyJoinS();
		enterPacket.name = name;
		enterPacket.others = new String[userSlots.length];
		enterPacket.gameMode = 0;
		enterPacket.team = team;
		
		lobbyFullPacket = new LobbyJoinS();
		lobbyFullPacket.gameMode = -1;
	}
	
	/**
	 * tries to add a user to the lobby
	 * @return wether the join was successfull
	 */
	public boolean tryToJoin(NeuerUser u) {
		
		if(full) {
			sendLobbyFull(u);
			return false;
		}
		
		for(int i = 0; i < userSlots.length; i++) {
			if(userSlots[i] == null) {
				informJoin(u);
				informOthersJoin(u);
				setSlot(i, u);
				return true;
			}
		}
		full = true;
		sendLobbyFull(u);
		return false;
	}
	

	/**
	 * tries to add a user to the lobby
	 * @return wether the join was successfull
	 */
	public boolean tryToJoinTeam(NeuerUser u, int t) {
		if(!contains(u)) return false;
		
		if(t < 0 || t >= teamSizes.length)
			return false;
		
		if(isTeamFull(t))
			return false;
		
		int id = getID(u);
		team[id] = t;
		fullness[id] ++;
		return true;
	}
	
	/**
	 * removes a user from the lobby if exists
	 */
	public void remove(NeuerUser u) {
		
		for(int i = 0; i < userSlots.length; i++) {
			if(userSlots[i] == u) {
				userSlots[i] = null;
				full = false;
				sendUserLeft(u);
				return;
			}
		}
	}
	
	public boolean isFull() {
		return full;
	}
	
	private boolean isTeamFull(int i) {
		return fullness[i] >= teamSizes[i];
	}
	
	private int getID(NeuerUser u) {
		for(int i = 0; i < userSlots.length; i++) {
			if(userSlots[i] == u) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean contains(NeuerUser u) {
		for(NeuerUser u_ : userSlots) {
			if(u == u_)
				return true;
		}
		return false;
	}
	
	public String getName() {
		return name;
	}
	
	private void setSlot(int i, NeuerUser u) {
		userSlots[i] = u;
		team[i] = -1;
		enterPacket.others[i] = (u == null) ? null : u.account.getName();
	}
	
	//-----------------------------------------------
	
	LobbyUserJoinedS joinPacket = new LobbyUserJoinedS();
	LobbyJoinS enterPacket;
	LobbyJoinS lobbyFullPacket;
	LobbyUserLeftS leavePacket = new LobbyUserLeftS();
	
	private void sendToLobby(Object o) {
		for(NeuerUser c : userSlots) {
			if(c == null) continue;
			c.sendTCP(o);
		}
	}
	
	private void sendLobbyFull(NeuerUser u) {
		u.sendTCP(lobbyFullPacket);
	}
	
	private void informJoin(NeuerUser u) {
		u.sendTCP(enterPacket);
	}
	
	private void informOthersJoin(NeuerUser u) {
		joinPacket.name = u.account.getName();
		sendToLobby(joinPacket);
	}
	
	private void sendUserLeft(NeuerUser u) {
		leavePacket.name = u.account.getName();
		sendToLobby(leavePacket);
	}
	
	
	InviteUserToLobbyS invitePacket = new InviteUserToLobbyS();
	private void invite(NeuerUser sender, NeuerUser receiver) {
		if(pendingRequests.contains(receiver)) {
		}
		else {
			invitePacket.lobby = name;
			invitePacket.sender = sender.account.getName();
			receiver.sendTCP(invitePacket);
			pendingRequests.add(receiver);
		}
	}
	
}
