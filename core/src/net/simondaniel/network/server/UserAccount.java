package net.simondaniel.network.server;

public class UserAccount {
	
	private boolean loggedIn;

	private String name;
	
	public UserAccount() {
		
	}
	
	public void login(String name) {
		this.name = name;
		loggedIn = true;
	}
	
	public void logout() {
		this.name = null;
		loggedIn = false;
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}

	public String getName() {
		return name;
	}
}
