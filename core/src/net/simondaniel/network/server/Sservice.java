package net.simondaniel.network.server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import net.simondaniel.screens.tempNet.Service;

public abstract class Sservice {
	private Connection con;
	
	private Listener listener;
	
	private boolean active;
	
	protected abstract void received(Connection c, Object o);
	
	public Sservice(Connection con) {
		this.con = con;
		
		final Sservice ref = this;
		listener = new Listener() {
			@Override
			public void received(Connection c, Object o) {
				ref.received(c, o);
			}
		};
	}
	
	public void activate() {
		if(active) return;
		
		con.addListener(listener);
		active = true;
	}
	
	public void deactivate() {
		if(!active) return;
		
		con.removeListener(listener);
		active = false;
	}
	
	/**
	 * TODO
	 * sends a tcp packet to all clients connected with the service. if the service is not active, this method will throw an error instead
	 */
	protected void send(Object o) {
		if(!active)
			throw new RuntimeException("tried to send a message on an inactive service");
		
		con.sendTCP(o);
	}
}
