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
				userSlots[i] = u;
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
	
	//-----------------------------------------------
	
	LobbyUserJoinedS joinPacket = new LobbyUserJoinedS();
	LobbyJoinS enterPacket = new LobbyJoinS();
	LobbyUserLeftS leavePacket = new LobbyUserLeftS();
	
	private void sendToLobby(Object o) {
		for(NeuerUser c : userSlots) {
			if(c == null) continue;
			c.sendTCP(o);
		}
	}
	
	private void sendLobbyFull(NeuerUser u) {
		
	}
	
	private void informJoin(NeuerUser u) {
		enterPacket.name = name;
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
}
