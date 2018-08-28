package net.simondaniel.game.server;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.fabio.GameMode;
import net.simondaniel.game.client.GameInstance;
import net.simondaniel.game.client.OneVsOneGame;
import net.simondaniel.network.server.GameServer;
import net.simondaniel.network.server.GameServer.Packet;
import net.simondaniel.network.server.MyServerlistener;
import net.simondaniel.network.server.Response.MoveToS;
import net.simondaniel.network.server.User;
import net.simondaniel.network.server.UserConnection;

public class Lobby implements MyServerlistener{

	public final String NAME;

	private int teamCount;
	private int[] teamSizes, teamindexStart;
	
	private final ServerGame gameInstance;
	
	private UserConnection[] userSlots; // specific slots
	private List<UserConnection> users; // those who have no slot yet
	private UserConnection admin;
	
	private GameMode mode;
	
	
	public Lobby(String name, GameMode mode, GameServer gs) {
		users = new ArrayList<UserConnection>();
		this.NAME = name;
		this.mode = mode;
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
			gameInstance = new ServerGame(gs, new OneVsOneGame(gs));
			break;
		default:
			gameInstance = null;
			break;
		}
		if(gameInstance != null) {
			
		}
	}
	
	public boolean startGameInstance() {
		for(UserConnection u : userSlots) {
			if(u == null) return false;
		}
		gameInstance.start(this);
		return true;
	}

	@Override
	public void receivedFromClient(UserConnection con, Object o) {
		gameInstance.packetBuffer.add(new Packet(con, o));
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

	public UserConnection[] getUsers() {
		return userSlots;
	}
	
	public int size() {
		return userSlots.length;
	}

	public void putUserInSlot(int slot, UserConnection c) {
		
		userSlots[slot] = c;
	}
	
	public boolean addUser(UserConnection c) {
		
		if(isFull()) return false;
		
		users.add(c);
		return true;
	}

	public GameMode getMode() {
		return mode;
	}

	public String[][] getNames() {
		String[][] names = new String[teamCount + 1][];
		
		names[0] = new String[users.size()];
		for(int i = 0; i < names[0].length; i++) {
			names[0][i] = users.get(i).user.name;
		}
		
		int index = 0;
		for(int i = 1; i < teamCount+1; i++) {
			names[i] = new String[teamSizes[i-1]];
			for(int j = 0; j < teamSizes[j]; j++) {
				if(userSlots[index] != null)
					names[i][j] = userSlots[index].user.name;
				else
					names[i][j] = null;
				index++;
			}
		}
		return names;
	}
	
	public static final int NO_SUCH_USER = -1, LOBBY_FULL = -2;

	public int joinTeam(UserConnection user, int id) {
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
}
