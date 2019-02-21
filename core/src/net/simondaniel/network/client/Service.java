package net.simondaniel.network.client;

import java.util.HashMap;

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
	
	public final void activate() {
		if(active) return;
		
		client.addListener(listener);
		active = true;
		onActivation();
	}
	
	public final void deactivate() {
		if(!active) return;
		
		onDeactivation();
		client.removeListener(listener);
		active = false;
	}
	
	/**
	 * sends a tcp packet to the server. if the service is not active, this method will throw an error instead
	 */
	protected final void send(Object o) {
		if(!active)
			throw new RuntimeException("tried to send a message on an inactive service");
		
		client.sendTCP(o);
	}
	
	public final boolean isActive() {
		return active;
	}
	
	protected void onActivation() {
		
	}
	
	protected void onDeactivation() {
		
	}
}
