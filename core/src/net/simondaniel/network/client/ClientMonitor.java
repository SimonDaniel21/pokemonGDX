package net.simondaniel.network.client;

public interface ClientMonitor {

	void setVisible(boolean b);

	void messageReceived(String sender, String message);

}
