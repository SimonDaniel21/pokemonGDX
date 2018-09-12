package net.simondaniel.network.server;

import com.esotericsoftware.kryonet.Connection;

import net.simondaniel.game.server.Lobby;

public class UserConnection extends Connection{


	public String name;
	public Lobby lobby = null;
	
	public UserConnection() {
		super();
		name = null;
	}
	
	public UserConnection(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public boolean isLoggedIn() {
		return name != null;
	}
	
	public void login(String name){
		this.name = name;
	}
	
	public void logout() {
		this.name = null;
	}
}
