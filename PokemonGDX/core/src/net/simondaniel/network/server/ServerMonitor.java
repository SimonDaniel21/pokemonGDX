package net.simondaniel.network.server;

public interface ServerMonitor {

	void connected(String u);

	void setVisible(boolean b);

	void updateUPS(float aCTUAL_UPS);

	void disConnected(String user);

	void messageReceived(String sender, String message);

	void addedLobby(String s);

	void removedLobby(String nAME);
	
	void packetReceived();
	void packetSend();
	
	void startLobby(String l);
	void stopLobby(String l);
}
