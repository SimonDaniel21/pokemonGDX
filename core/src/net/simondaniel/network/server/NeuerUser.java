package net.simondaniel.network.server;

import com.esotericsoftware.kryonet.Connection;

public class NeuerUser extends Connection{

	AuthenticationSservice authService;
	
	public NeuerUser() {
		authService = new AuthenticationSservice(this);
	}
}
