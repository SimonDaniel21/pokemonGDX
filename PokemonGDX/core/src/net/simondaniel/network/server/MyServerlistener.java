package net.simondaniel.network.server;

import com.esotericsoftware.kryonet.Connection;

public interface MyServerlistener {
	public void receivedFromClient(UserConnection con, Object o);
}
