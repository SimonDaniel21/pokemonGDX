package net.simondaniel.network.server;

import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.network.server.UserTrackSservice.UserTrackServiceData;

public class NeuerUser extends Connection{

	UserAccount account;
	
	UserTrackServiceData track;
	
	PlayServer server;
	
	public NeuerUser(PlayServer server) {
		account = new UserAccount();
		track = new UserTrackServiceData();
	}
}
