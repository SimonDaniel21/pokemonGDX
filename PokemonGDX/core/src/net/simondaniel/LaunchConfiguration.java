package net.simondaniel;

public enum LaunchConfiguration {

	//Release Configurations
	RELEASE_CLIENT("c"), RELEASE_SERVER("s"),
	DEBUG_CLIENT("dc"), DEBUG_SERVER("ds"),
	//Debug Configurations
	LOGGED_IN("l"); 
	
	public final String command;
	
	public static LaunchConfiguration config;
	
	private LaunchConfiguration(String com) {
		this.command = com;
	}
	
	public static LaunchConfiguration configuration(String s) {
		for(LaunchConfiguration c : LaunchConfiguration.values()) {
			if(c.command.equals(s)) {
				return c;
			}
		}
		return RELEASE_CLIENT;
	}
}
