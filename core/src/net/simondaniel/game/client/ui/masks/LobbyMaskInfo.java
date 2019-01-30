package net.simondaniel.game.client.ui.masks;

import java.util.List;

import net.simondaniel.GameMode;
import net.simondaniel.network.UserTracker;
import net.simondaniel.network.client.GameClient;

public class LobbyMaskInfo extends ShowMaskInfo{

	public String lobbyName;
	public String[][] others;
	public GameMode mode;
	public GameClient gc;
	public boolean joinLobby;
	
	public UserTracker userTracker;
	
	public boolean isComplete() {
	
		return !(lobbyName == null || others == null || mode == null || gc == null || userTracker == null);
	}

}
