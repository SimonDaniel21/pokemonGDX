package net.simondaniel.network.server;

import com.esotericsoftware.kryonet.Connection;

public class NeuerUser extends Connection{

	UserAccount account;
	
	AuthenticationSservice authService;
	UserTrackSservice trackService;
	
	public NeuerUser(PlayServer server) {
		authService = new AuthenticationSservice(this);
		trackService = new UserTrackSservice(this, server);
		account = new UserAccount();
	}
}
