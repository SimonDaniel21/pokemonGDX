package net.simondaniel.game.client.ui.masks;

import java.util.List;

import net.simondaniel.GameMode;
import net.simondaniel.network.client.GameClient;
import net.simondaniel.network.client.PlayClient;

public class LobbyMaskInfo extends ShowMaskInfo{

	public String lobbyName;
	public String[] others;
	public int[] team;
	public GameMode mode;
	public boolean joinLobby;
	public PlayClient client;
	

	public boolean isComplete() {
	
		return !(lobbyName == null || others == null || mode == null || client == null || team == null);
	}

}
