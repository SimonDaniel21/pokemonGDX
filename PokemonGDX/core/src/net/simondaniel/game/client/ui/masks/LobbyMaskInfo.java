package net.simondaniel.game.client.ui.masks;

import java.lang.reflect.Field;

import net.simondaniel.fabio.GameMode;
import net.simondaniel.game.client.ui.Friendlist;
import net.simondaniel.network.client.GameClient;

public class LobbyMaskInfo extends ShowMaskInfo{

	public String lobbyName;
	public String[][] others;
	public GameMode mode;
	public GameClient gc;
	
	public String[] inviteableUsers;
	
	public boolean isComplete() {
	
		return !(lobbyName == null || others == null || mode == null || gc == null || inviteableUsers == null);
	}

}
