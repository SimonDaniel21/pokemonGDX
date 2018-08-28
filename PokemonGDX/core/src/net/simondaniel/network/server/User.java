package net.simondaniel.network.server;

import net.simondaniel.game.server.Lobby;

public class User {

	public final String name;
	public Lobby lobby = null;
	
	public User(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
