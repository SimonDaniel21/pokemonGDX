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
import net.simondaniel.network.server.Response.InviteAnswerS;
import net.simondaniel.network.server.Response.InviteUserToLobbyS;
import net.simondaniel.network.server.Response.LobbyJoinS;
import net.simondaniel.network.server.Response.LobbyStartTimerS;
import net.simondaniel.network.server.Response.LobbyUserJoinedS;
import net.simondaniel.network.server.Response.LobbyUserLeftS;
import net.simondaniel.network.server.Response.UserReadyS;
import net.simondaniel.network.server.UserConnection;

public class Lobby implements MyServerlistener{

	public static int STARTING_TIME = 10*1000, EXTRA_TIME = 1*1000;
	
	public final String NAME;

	private int teamCount;
	private int[] teamSizes, teamindexStart;
	
	//private final ServerGame gameInstance = null;
	
	private UserConnection[] userSlots; // specific slots
	private boolean[] readys;
	private List<UserConnection> users; // those who have no slot yet
	private HashMap<UserConnection, Integer> invitedUsers;
	private GameMode mode;
	
	Timer timer;
	long startTime;
	boolean timerActive;
	
	
	public Lobby(String name, GameMode mode, GameServer gs) {
		if(LaunchConfiguration.config == LaunchConfiguration.DEBUG_SERVER) {
			STARTING_TIME = 100;
			EXTRA_TIME = 10;
		}
		users = new ArrayList<UserConnection>();
		invitedUsers = new HashMap<UserConnection, Integer>();
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
			userSlots = new UserConnection[2];
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
		for(UserConnection u : userSlots) {
			if(u == null) return false;
		}
//		gameInstance.start(this);
//		gameInstance.server.window.startLobby(NAME);
		return true;
	}

	@Override
	public void receivedFromClient(UserConnection con, Object o) {
	
	}

	public UserConnection getUser(int i) {
		
		return userSlots[i];
	}

	public void sendToAllTCP(Object s) {
		for(UserConnection c : userSlots) {
			if(c != null) {
				c.sendTCP(s);
			}
		}
		for(UserConnection c : users) {
			c.sendTCP(s);
		}
	}

	private void sendToAllTCPexcept(Object p, UserConnection con) {
		for(UserConnection c : userSlots) {
			if(c != null && c != con) {
				c.sendTCP(p);
			}
		}
		for(UserConnection c : users) {
			if(c != con)
				c.sendTCP(p);
		}
	}

	public UserConnection[] getUsers() {
		return userSlots;
	}
	
	public int size() {
		return userSlots.length;
	}

	public void putUserInSlot(int slot, UserConnection c) {
		
		userSlots[slot] = c;
	}
	
	private String[] getInvitedUsersWithState(int state) {
		ArrayList<String> l = new ArrayList<String>();
		for(UserConnection s : invitedUsers.keySet()) {
			if(invitedUsers.get(s) == state)
				l.add(s.name);
		}
		String[] sa = new String[l.size()];
		for(int i = 0; i < l.size(); i++) {
			sa[i] = l.get(i);
		}
		return sa;
	}
	
	public void addUser(UserConnection c) {
		System.out.println("Lobby adds user " + c.name);
		LobbyJoinS ps = new LobbyJoinS();
		
		if(isFull()) {
			ps.gameMode = -1;
		}
		else {
			users.add(c);
			
			c.lobby = this;
			LobbyUserJoinedS luj = new LobbyUserJoinedS();
			luj.name = c.name;
			sendToAllTCPexcept(luj, c);
			
			ps.name = NAME;
			ps.gameMode = mode.ordinal();
			ps.others = getNames(); 
			ps.invitedAccepted = getInvitedUsersWithState(ACCEPTED);
			ps.invitedDeclined = getInvitedUsersWithState(DECLINED);
			ps.invitedPending = getInvitedUsersWithState(PENDING);
		}
		
		c.sendTCP(ps);
	}

	public void removeUser(UserConnection c) {
		boolean removed = false;
		for(int i = 0; i < userSlots.length; i++) {
			if(userSlots[i] == c) {
				userSlots[i] = null;
				readys[i] = false;
				removed = true;
				break;
			}
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
			names[0][i] = users.get(i).name;
		}
		
		int index = 0;
		for(int i = 1; i < teamCount+1; i++) {
			names[i] = new String[teamSizes[i-1]];
			for(int j = 0; j < teamSizes[j]; j++) {
				if(userSlots[index] != null)
					names[i][j] = userSlots[index].name;
				else
					names[i][j] = null;
				index++;
			}
		}
		return names;
	}
	
	public static final int NO_SUCH_USER = -1, LOBBY_FULL = -2, NEXT_FREE_TEAM = -3;

	public int joinTeam(UserConnection user, int id) {
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
		for(UserConnection u : userSlots) {
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
		for(UserConnection u : userSlots) {
			if(u != null)
				return false;
		}
		return users.isEmpty();
	}
	
	public boolean contains(UserConnection c) {
		
		for(UserConnection c_ : users) 
			if(c_ == c)
				return true;
		for(UserConnection c_ : userSlots) 
			if(c_ == c)
				return true;
		return false;
	}
	
	public void invite(UserConnection c, String sender) {
		
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
		p.name = c.name;
		sendToAllTCP(p);
		p.sender = sender;
		c.sendTCP(p);
	}
	
	public void inviteAnswer(UserConnection c, boolean answer) {
		
		if(!invitedUsers.containsKey(c)) return;
		
		invitedUsers.put(c, answer ? ACCEPTED : DECLINED);
		
		InviteAnswerS s = new InviteAnswerS();
		s.answer = answer;
		s.name = c.name;
		sendToAllTCP(s);
		if(answer)
			addUser(c);
		
	}
	
	private static final int PENDING = 0, ACCEPTED = 1, DECLINED = 2;

	public void setReady(UserConnection c) {
		ready(c, true);
	}
	
	public void setUnReady(UserConnection c) {
		ready(c, false);
	}
	
	private boolean isEveryoneReady() {
		
		for(boolean b : readys) if(!b) return false;
		
		return true;
	}
	
	private void ready(int i, boolean ready) {
		
		if(ready) {
			if(isEveryoneReady()) {
				startTimer();
			}
		}
		else {
			timerActive = false;
			
		}
	}
	
	public void ready(UserConnection c, boolean ready) {
		System.err.println("try: ready " + ready);
		for(int i = 0; i < userSlots.length; i++) {
			if(userSlots[i] == c && readys[i] != ready) {
				readys[i] = ready;
				UserReadyS p = new UserReadyS();
				p.user =  c.name;
				p.ready = ready;
				sendToAllTCP(p);
				if(!ready)
					timer.cancel();
			}
		}
		if(isEveryoneReady())
			startTimer();
		
	}

	private void startTimer() {
		LobbyStartTimerS p = new LobbyStartTimerS();
		p.start = System.currentTimeMillis() + STARTING_TIME;
		sendToAllTCP(p);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				//System.out.println("STARTING GAME");
				startGameInstance();
				timer.cancel();
			}
		}, (STARTING_TIME + EXTRA_TIME));
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
