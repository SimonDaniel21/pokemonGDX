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

public class Lobby implements MyServerlistener{

	public final String NAME;

	private int teamCount;
	private int[] teamSizes, teamindexStart;
	
	//private final ServerGame gameInstance = null;
	
	private NeuerUser[] userSlots; // specific slots
	private boolean[] readys;
	private List<NeuerUser> users; // those who have no slot yet
	private HashMap<NeuerUser, Integer> invitedUsers;
	private GameMode mode;
	
	Timer timer;
	long startTime;
	boolean timerActive;
	
	
	public Lobby(String name, GameMode mode) {
		if(LaunchConfiguration.config == LaunchConfiguration.DEBUG_SERVER) {
		
		}
		users = new ArrayList<NeuerUser>();
		invitedUsers = new HashMap<NeuerUser, Integer>();
		this.NAME = name;
		this.mode = mode;
		timer = new Timer();
		switch (mode) {
		case ONE_VS_ONE:
			teamCount = 2;
			teamSizes = new int[] {1,1};
			teamindexStart = new int[teamCount];
			int indexoffset = 0;;
			for(int i = 0; i < teamCount; i++) {
				teamindexStart[i] = indexoffset;
				indexoffset+= teamSizes[i];
			}
			userSlots = new NeuerUser[2];
			//gameInstance = new ServerGame(gs, new OneVsOneGame(gs));
			break;
		default:
			//gameInstance = null;
			break;
		}
//		if(gameInstance != null) {
//			
//		}
		readys = new boolean[userSlots.length];
	}
	
	public boolean startGameInstance() {
		for(NeuerUser u : userSlots) {
			if(u == null) return false;
		}
//		gameInstance.start(this);
//		gameInstance.server.window.startLobby(NAME);
		return true;
	}

	@Override
	public void receivedFromClient(UserConnection con, Object o) {
	
	}

	public NeuerUser getUser(int i) {
		
		return userSlots[i];
	}

	public void sendToAllTCP(Object s) {
		for(NeuerUser c : userSlots) {
			if(c != null) {
				c.sendTCP(s);
			}
		}
		for(NeuerUser c : users) {
			c.sendTCP(s);
		}
	}

	private void sendToAllTCPexcept(Object p,NeuerUser con) {
		for(NeuerUser c : userSlots) {
			if(c != null && c != con) {
				c.sendTCP(p);
			}
		}
		for(NeuerUser c : users) {
			if(c != con)
				c.sendTCP(p);
		}
	}

	public NeuerUser[] getUsers() {
		return userSlots;
	}
	
	public int size() {
		return userSlots.length;
	}

	public void putUserInSlot(int slot, NeuerUser c) {
		
		userSlots[slot] = c;
	}
	
	private String[] getInvitedUsersWithState(int state) {
		return null;
//		ArrayList<String> l = new ArrayList<String>();
//		for(NeuerUser s : invitedUsers.keySet()) {
//			if(invitedUsers.get(s) == state)
//				//l.add(s.name);
//		}
//		String[] sa = new String[l.size()];
//		for(int i = 0; i < l.size(); i++) {
//			sa[i] = l.get(i);
//		}
//		return sa;
	}
	
	public void addUser(NeuerUser u) {
		System.out.println("Lobby adds user ");
		LobbyJoinS ps = new LobbyJoinS();
		
		if(isFull()) {
			ps.gameMode = -1;
		}
		else {
			users.add(u);
			
			//u.lobby = this;
			LobbyUserJoinedS luj = new LobbyUserJoinedS();
			//luj.name = u.name;
			sendToAllTCPexcept(luj, u);
			
			ps.name = NAME;
			ps.gameMode = mode.ordinal();
			ps.others = getNames(); 
			ps.invitedAccepted = getInvitedUsersWithState(ACCEPTED);
			ps.invitedDeclined = getInvitedUsersWithState(DECLINED);
			ps.invitedPending = getInvitedUsersWithState(PENDING);
		}
		
		u.sendTCP(ps);
	}

	public void removeUser(UserConnection c) {
		boolean removed = false;
		for(int i = 0; i < userSlots.length; i++) {
//			if(userSlots[i] == c) {
//				userSlots[i] = null;
//				readys[i] = false;
//				removed = true;
//				break;
//			}
		}
		if(!removed)
			removed = users.remove(c);
		
		if(removed) {
			LobbyUserLeftS p = new LobbyUserLeftS();
			p.name = c.name;
			sendToAllTCP(p);
			c.sendTCP(p);
		}
	}

	public GameMode getMode() {
		return mode;
	}

	public String[][] getNames() {
		String[][] names = new String[teamCount + 1][];
		
		names[0] = new String[users.size()];
		for(int i = 0; i < names[0].length; i++) {
			//names[0][i] = users.get(i).name;
		}
		
		int index = 0;
		for(int i = 1; i < teamCount+1; i++) {
			names[i] = new String[teamSizes[i-1]];
			for(int j = 0; j < teamSizes[j]; j++) {
//				if(userSlots[index] != null)
//					names[i][j] = userSlots[index].name;
//				else
//					names[i][j] = null;
				index++;
			}
		}
		return names;
	}
	
	public static final int NO_SUCH_USER = -1, LOBBY_FULL = -2, NEXT_FREE_TEAM = -3;

	public int joinTeam(NeuerUser user, int id) {
		if(id == NEXT_FREE_TEAM) {
			for(int i = 0; i < teamCount; i++) {
				if(!isTeamFull(i)) {
					id = i+1;
					break;
				}
					
			}
		}
		if(id < 0 || id > teamCount) return -1;
		if(id == 0) {
			for(int i = 0; i < userSlots.length; i++) {
				if(userSlots[i] == user) {
					userSlots[i] = null;
					users.add(user);
					return 0;
				}
			}
			return NO_SUCH_USER;
		}
		int contained = -1;
		if(users.contains(user)) {
			contained = 0;
		}
		int teamID = id-1;
		for(int i = 0; i < teamCount; i++) {
			
			if(contained != -1) break;
			if(i == teamID) continue;
			
			for(int j = 0; j < teamSizes[i]; j++) {
				int index = j + teamindexStart[i];
				if(userSlots[index] == user) {
					contained = index + 1;
					break;
				}
			}
		}
		
		if(contained != -1) {
			int off = teamindexStart[teamID];
			for(int i = 0; i < teamSizes[teamID]; i++) {
				int index = off + i;
				if(userSlots[index] == null) {
					if(contained == 0)
						users.remove(user);
					else {
						userSlots[contained-1] = null;
					}
						
					userSlots[index] = user;
					return i;
				}
			}
			return LOBBY_FULL;
		}
		
		return NO_SUCH_USER;
	}
	
	public boolean isFull() {
		int count = 0, maxCount = 0;
		for(NeuerUser u : userSlots) {
			if(u != null)
				count++;
		}
		count += users.size();
		
		for(int i : teamSizes) {
			maxCount += teamSizes[i];
		}
		
		return count >= maxCount;
	}
	
	public boolean isEmpty() {
		for(NeuerUser u : userSlots) {
			if(u != null)
				return false;
		}
		return users.isEmpty();
	}
	
	public boolean contains(NeuerUser c) {
		
		for(NeuerUser c_ : users) 
			if(c_ == c)
				return true;
		for(NeuerUser c_ : userSlots) 
			if(c_ == c)
				return true;
		return false;
	}
	
	public void invite(NeuerUser c, String sender) {
		
		if(contains(c))return;
		
		if(invitedUsers.containsKey(c)) {
			if(invitedUsers.get(c) == PENDING) {
				System.out.println("contains key doppel lool");
				return;
			}
		}
		
		invitedUsers.put(c, PENDING);
		
		InviteUserToLobbyS p = new InviteUserToLobbyS();
		p.lobby = NAME;
		//p.name = c.name;
		sendToAllTCP(p);
		p.sender = sender;
		c.sendTCP(p);
	}
	
	public void inviteAnswer(UserConnection c, boolean answer) {
		
		if(!invitedUsers.containsKey(c)) return;
		
		//.put(c, answer ? ACCEPTED : DECLINED);
		
		InviteAnswerS s = new InviteAnswerS();
		s.answer = answer;
		s.name = c.name;
		sendToAllTCP(s);
		//if(answer)
			//addUser(c);
		
	}
	
	private static final int PENDING = 0, ACCEPTED = 1, DECLINED = 2;

	public void setReady(UserConnection c) {

	}
	
	public void setUnReady(UserConnection c) {
	
	}
	
	private boolean isEveryoneReady() {
		
		for(boolean b : readys) if(!b) return false;
		
		return true;
	}
	
	private void ready(int i, boolean ready) {
		
		if(ready) {
		
		}
		else {
			timerActive = false;
			
		}
	}
	

	public boolean isTeamFull(int i) {
		
		for(int j = 0; j < teamSizes[i]; j++) {
			int index = j + teamindexStart[i];
			if(userSlots[index] == null) {
				return false;
			}
		}
		return true;
	}
}
