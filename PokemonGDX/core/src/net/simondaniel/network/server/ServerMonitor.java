package net.simondaniel.network.server;

public interface ServerMonitor {

	void connected(User u);

	void setVisible(boolean b);

	void updateUPS(float aCTUAL_UPS);

	void disConnected(User user);

	void messageReceived(String sender, String message);

	void addedLobby(String s);

	void removedLobby(String nAME);
	
	void packetReceived();
	void packetSend();
	
}
