package net.simondaniel.network.client;

import com.esotericsoftware.kryonet.Connection;

public interface MyListener {

	public abstract void received (Connection c, Object o);
}
