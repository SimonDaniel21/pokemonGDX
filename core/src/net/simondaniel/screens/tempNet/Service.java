package net.simondaniel.screens.tempNet;

import org.w3c.dom.ls.LSInput;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public abstract class Service {
	
	private Client client;
	
	private Listener listener;
	
	private boolean active;
	
	protected abstract void received(Connection c, Object o);
	
	public Service(Client c) {
		this.client = c;
		
		final Service ref = this;
		listener = new Listener() {
			@Override
			public void received(Connection c, Object o) {
				ref.received(c, o);
			}
		};
	}
	
	public void activate() {
		if(active) return;
		
		client.addListener(listener);
		active = true;
	}
	
	public void deactivate() {
		if(!active) return;
		
		client.removeListener(listener);
		active = false;
	}
	
	/**
	 * sends a tcp packet to the server. if the service is not active, this method will throw an error instead
	 */
	protected void send(Object o) {
		if(!active)
			throw new RuntimeException("tried to send a message on an inactive service");
		
		client.sendTCP(o);
	}
}
