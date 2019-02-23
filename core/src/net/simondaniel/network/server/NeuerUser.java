package net.simondaniel.network.server;

import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.network.server.MatchmakingSservice.MatchServiceData;
import net.simondaniel.network.server.UserTrackSservice.UserTrackServiceData;

public class NeuerUser extends Connection{

	public UserAccount account;
	
	UserTrackServiceData track;
	MatchServiceData match;
	
	PlayServer server;
	
	public NeuerUser(PlayServer server) {
		account = new UserAccount();
		track = new UserTrackServiceData();
		match = new MatchServiceData();
	}
	
	public void onLogout() {
		account.logout();
		match.logout();
	}
}
